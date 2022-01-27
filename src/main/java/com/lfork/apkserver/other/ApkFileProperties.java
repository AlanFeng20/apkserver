 package com.lfork.apkserver.other;
 
 import org.springframework.boot.context.properties.ConfigurationProperties;
 
 @ConfigurationProperties(prefix = "file")
 public class ApkFileProperties
 {
   public static String nameFormat = "[pkgname]-[versioncode] eg:com.paradise.facertc-1";
   
   public static String separator = "-";
   
   private String uploadDir;
   
   public String getUploadDir() {
     return this.uploadDir;
   }
   
   public void setUploadDir(String uploadDir) {
     this.uploadDir = uploadDir;
   }
 }


