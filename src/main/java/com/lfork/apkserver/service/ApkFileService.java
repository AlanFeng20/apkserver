package com.lfork.apkserver.service;

import com.lfork.apkserver.bo.autoupdate.LatestApkInfo;
import com.lfork.apkserver.other.ApkFileProperties;
import com.lfork.apkserver.other.FileException;
import com.lfork.apkserver.utils.Md5Utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Date;
import java.util.Enumeration;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ApkFileService {
    private final Path fileStorageLocation;

    @Autowired
    public ApkFileService(ApkFileProperties fileProperties) {
        this.fileStorageLocation = Paths.get(fileProperties.getUploadDir(), new String[0]).toAbsolutePath().normalize();
        System.out.println("ApkFileService:" + this.fileStorageLocation);
        try {
            Files.createDirectories(this.fileStorageLocation, (FileAttribute<?>[]) new FileAttribute[0]);
        } catch (Exception ex) {
            throw new FileException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }


    public String storeApkFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        File localApkFile = null;
        ApkFile apkFile = null;


        if (fileName.contains("..")) {
            throw new FileException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        if (!fileName.contains(".apk")) {
            throw new FileException("Sorry! 只能上传apk文件。" + fileName + "的格式有问题");
        }


        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            file.getInputStream().close();

            localApkFile = new File(this.fileStorageLocation.resolve(fileName).normalize().toString());

            apkFile = new ApkFile(localApkFile);

            ApkMeta apkMeta = apkFile.getApkMeta();
            String apkNewName = apkMeta.getPackageName() + ApkFileProperties.separator + apkMeta.getVersionCode() + ".apk";
            apkFile.close();
            apkFile = null;
            boolean result = localApkFile.renameTo(new File(localApkFile.getParent() + File.separator + apkNewName));
            return apkNewName;

        } catch (Exception ex) {
            if (localApkFile != null) {
                localApkFile.delete();
            }
            throw new FileException("出错   (" + ex.getMessage() + ")", ex);
        } finally {
            try {
                if (apkFile != null) {
                    apkFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            UrlResource urlResource = new UrlResource(filePath.toUri());
            if (urlResource.exists()) {
                return (Resource) urlResource;
            }
            throw new FileException("File not found " + fileName);
        } catch (MalformedURLException ex) {
            throw new FileException("File not found " + fileName, ex);
        }
    }


    public LatestApkInfo getLatestApkInfo(String clientPkgName, int clientVersionCode) {
        LatestApkInfo latestApkInfo = new LatestApkInfo();
        latestApkInfo.setClientApkPackageName(clientPkgName);
        latestApkInfo.setClientApkVersionCode(clientVersionCode);

        try {
            if (clientPkgName == null || clientPkgName.isEmpty()) {
                throw new Exception("pkgName cannot be null");
            }

            File[] files = this.fileStorageLocation.toFile().listFiles();

            if (files == null || files.length < 1) {
                return null;
            }


            File latestFile = null;
            for (File file : files) {

                String pkgName = parsePackageName(file.getName());
                if (pkgName != null && pkgName.equals(clientPkgName)) {
                    int versionCode = parseVersionCode(file.getName());
                    if (versionCode > clientVersionCode) {
                        latestFile = file;
                    }
                }
            }

            if (latestFile != null) {
                String latestApkName = latestFile.getName();

                latestApkInfo.setLatestApkVersionCode(parseVersionCode(latestApkName));
                latestApkInfo.setLatestApkPackageName(parsePackageName(latestApkName));
                latestApkInfo.setApkSizeKB(latestFile.length() / 1024L);
                latestApkInfo.setMd5(Md5Utils.getFileMD5(latestFile));
                latestApkInfo.setDownloadUrl("http://" + getLocalHostLANAddress().getHostAddress() + ":8089/downloadApk/" + latestApkName);
                latestApkInfo.setUploadTime((new Date(latestFile.lastModified())).toString());
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return latestApkInfo;
    }

    //https://www.cnblogs.com/starcrm/p/7071227.html
    private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }

    private int parseVersionCode(String apkFileName) {
        return Integer.parseInt(apkFileName.substring(apkFileName.indexOf(ApkFileProperties.separator) + 1, apkFileName.length() - 4));
    }


    private String parsePackageName(String apkFileName) {
        String pkgName = null;
        try {
            pkgName = apkFileName.substring(0, apkFileName.indexOf(ApkFileProperties.separator));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pkgName;
    }
}


