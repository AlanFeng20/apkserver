 package com.lfork.apkserver.bo.autoupdate;
 
 public class LatestApkInfo {
   private String latestApkPackageName;
   private int latestApkVersionCode;
   private String clientApkPackageName;
   private int clientApkVersionCode;
   private String md5;
   private long apkSizeKB;
   private String downloadUrl;
   private String uploadTime;
   private String updateDescription = "还没有更新描述";
   
   public String getUploadTime() {
     return this.uploadTime;
   }
   
   public void setUploadTime(String uploadTime) {
     this.uploadTime = uploadTime;
   }
   
   public String getUpdateDescription() {
     return this.updateDescription;
   }
   
   public void setUpdateDescription(String updateDescription) {
     this.updateDescription = updateDescription;
   }
   
   public String getDownloadUrl() {
     return this.downloadUrl;
   }
   
   public void setDownloadUrl(String downloadUrl) {
     this.downloadUrl = downloadUrl;
   }
   
   public String getMd5() {
     return this.md5;
   }
   
   public void setMd5(String md5) {
     this.md5 = md5;
   }
   
   public long getApkSizeKB() {
     return this.apkSizeKB;
   }
   
   public void setApkSizeKB(long apkSizeKB) {
     this.apkSizeKB = apkSizeKB;
   }
   
   public String getLatestApkPackageName() {
     return this.latestApkPackageName;
   }
 
   
   public void setLatestApkPackageName(String latestApkPackageName) {
     this.latestApkPackageName = latestApkPackageName;
   }
 
   
   public int getLatestApkVersionCode() {
     return this.latestApkVersionCode;
   }
 
   
   public void setLatestApkVersionCode(int latestApkVersionCode) {
     this.latestApkVersionCode = latestApkVersionCode;
   }
 
   
   public String getClientApkPackageName() {
     return this.clientApkPackageName;
   }
 
   
   public void setClientApkPackageName(String clientApkPackageName) {
     this.clientApkPackageName = clientApkPackageName;
   }
 
   
   public int getClientApkVersionCode() {
     return this.clientApkVersionCode;
   }
 
   
   public void setClientApkVersionCode(int clientApkVersionCode) {
     this.clientApkVersionCode = clientApkVersionCode;
   }
 
 
 
 
 
   
   public boolean isLatestApkEmpty() {
     return (this.latestApkPackageName == null || this.latestApkVersionCode == 0);
   }
 
 
 
 
 
   
   public boolean isClientApkLatest() {
     if (isLatestApkEmpty()) {
       return false;
     }
     return (this.clientApkVersionCode >= this.latestApkVersionCode);
   }
 }


