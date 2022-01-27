package com.lfork.apkserver.controller;

import com.lfork.apkserver.bo.autoupdate.LatestApkInfo;
import com.lfork.apkserver.dto.autoupdate.CheckVersionResult;
import com.lfork.apkserver.dto.autoupdate.UploadApkResponse;
import com.lfork.apkserver.service.ApkFileService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
public class UpdateController {
    @Autowired
    private ApkFileService apkFileService;

    @GetMapping({"/checkUpdate"})
    public CheckVersionResult checkUpdate(@RequestParam("packageName") String packageName, @RequestParam("versionCode") int versionCode) {
        CheckVersionResult result = new CheckVersionResult();
        System.out.println("packageName:" + packageName + " versionCode: " + versionCode);
        if (packageName == null) {
            result.setMsg("Error,parameter packageName cannot be null. ");
            result.setCode(-1);
            return result;
        }
        LatestApkInfo latestApkInfo = this.apkFileService.getLatestApkInfo(packageName, versionCode);
        if (latestApkInfo == null) {
            result.setMsg("Failed");
            result.setCode(0);
        } else {
            result.setRequireUpgrade(latestApkInfo.isClientApkLatest() ? 0 : 2);
            result.setVersionCode(latestApkInfo.getLatestApkVersionCode());
            result.setVersionName(latestApkInfo.getLatestApkPackageName() + "-" + latestApkInfo.getLatestApkVersionCode());
            result.setDownloadUrl(latestApkInfo.getDownloadUrl());
            result.setApkMd5(latestApkInfo.getMd5());
            result.setUploadTime(latestApkInfo.getUploadTime());
            result.setApkSize(latestApkInfo.getApkSizeKB());
            result.setModifyContent(latestApkInfo.getUpdateDescription());

            result.setMsg("Success");
            result.setCode(0);
        }

        return result;
    }


    private static final Logger logger = LoggerFactory.getLogger(com.lfork.apkserver.controller.UpdateController.class);


    @PostMapping({"/uploadFile"})
    public UploadApkResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = this.apkFileService.storeApkFile(file);


        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadApk/").path(fileName).toUriString();

        return new UploadApkResponse(fileName, fileDownloadUri, file
                .getContentType(), file.getSize());
    }


    @PostMapping({"/uploadMultipleFiles"})
    public List<UploadApkResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return (List<UploadApkResponse>) Arrays.<MultipartFile>stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }


    @GetMapping({"/downloadApk/{fileName:.+}"})
    public ResponseEntity<Resource> downloadApk(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = this.apkFileService.loadFileAsResource(fileName);


        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }


        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ((ResponseEntity.BodyBuilder) ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header("Content-Disposition", new String[]{"attachment; filename=\"" + resource.getFilename() + "\""
                })).body(resource);
    }
}


