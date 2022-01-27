 package com.lfork.apkserver.dto.autoupdate;
 
 import com.fasterxml.jackson.annotation.JsonProperty;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class CheckVersionResult
 {
   public static final int NO_NEW_VERSION = 0;
   public static final int HAVE_NEW_VERSION = 1;
   public static final int HAVE_NEW_VERSION_FORCED_UPLOAD = 2;
   private int Code;
   private String Msg;
   private int UpdateStatus;
   private int VersionCode;
   private String VersionName;
   private String UploadTime;
   private String ModifyContent;
   private String DownloadUrl;
   private String ApkMd5;
   private long ApkSize;
   
   @JsonProperty("Code")
   public int getCode() {
     return this.Code;
   }
   
   public com.lfork.apkserver.dto.autoupdate.CheckVersionResult setCode(int code) {
     this.Code = code;
     return this;
   }
   @JsonProperty("Msg")
   public String getMsg() {
     return this.Msg;
   }
   
   public com.lfork.apkserver.dto.autoupdate.CheckVersionResult setMsg(String msg) {
     this.Msg = msg;
     return this;
   }
   @JsonProperty("UpdateStatus")
   public int getUpdateStatus() {
     return this.UpdateStatus;
   }
   
   public com.lfork.apkserver.dto.autoupdate.CheckVersionResult setRequireUpgrade(int updateStatus) {
     this.UpdateStatus = updateStatus;
     return this;
   }
   @JsonProperty("UploadTime")
   public String getUploadTime() {
     return this.UploadTime;
   }
   
   public com.lfork.apkserver.dto.autoupdate.CheckVersionResult setUploadTime(String uploadTime) {
     this.UploadTime = uploadTime;
     return this;
   }
   @JsonProperty("VersionCode")
   public int getVersionCode() {
     return this.VersionCode;
   }
   
   public com.lfork.apkserver.dto.autoupdate.CheckVersionResult setVersionCode(int versionCode) {
     this.VersionCode = versionCode;
     return this;
   }
   @JsonProperty("VersionName")
   public String getVersionName() {
     return this.VersionName;
   }
   
   public com.lfork.apkserver.dto.autoupdate.CheckVersionResult setVersionName(String versionName) {
     this.VersionName = versionName;
     return this;
   }
   @JsonProperty("ModifyContent")
   public String getModifyContent() {
     return this.ModifyContent;
   }
   
   public com.lfork.apkserver.dto.autoupdate.CheckVersionResult setModifyContent(String modifyContent) {
     this.ModifyContent = modifyContent;
     return this;
   }
   @JsonProperty("DownloadUrl")
   public String getDownloadUrl() {
     return this.DownloadUrl;
   }
   
   public com.lfork.apkserver.dto.autoupdate.CheckVersionResult setDownloadUrl(String downloadUrl) {
     this.DownloadUrl = downloadUrl;
     return this;
   }
   @JsonProperty("ApkMd5")
   public String getApkMd5() {
     return this.ApkMd5;
   }
   
   public com.lfork.apkserver.dto.autoupdate.CheckVersionResult setApkMd5(String apkMd5) {
     this.ApkMd5 = apkMd5;
     return this;
   }
   
   @JsonProperty("ApkSize")
   public long getApkSize() {
     return this.ApkSize;
   }
   
   public com.lfork.apkserver.dto.autoupdate.CheckVersionResult setApkSize(long apkSize) {
     this.ApkSize = apkSize;
     return this;
   }
 
   
   public String toString() {
     return "CheckVersionResult{Code=" + this.Code + ", Msg='" + this.Msg + '\'' + ", UpdateStatus=" + this.UpdateStatus + ", VersionCode=" + this.VersionCode + ", VersionName='" + this.VersionName + '\'' + ", UploadTime='" + this.UploadTime + '\'' + ", ModifyContent='" + this.ModifyContent + '\'' + ", DownloadUrl='" + this.DownloadUrl + '\'' + ", ApkMd5='" + this.ApkMd5 + '\'' + ", ApkSize=" + this.ApkSize + '}';
   }
 }


