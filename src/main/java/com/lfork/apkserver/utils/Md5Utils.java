 package com.lfork.apkserver.utils;
 
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.IOException;
 import java.security.MessageDigest;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public final class Md5Utils
 {
   private Md5Utils() {
     throw new UnsupportedOperationException("cannot be instantiated");
   }
 
 
 
 
 
 
   
   public static String getFileMD5(File file) {
     if (file == null || !file.exists()) {
       return "";
     }
     FileInputStream fis = null;
     try {
       MessageDigest digest = MessageDigest.getInstance("MD5");
       fis = new FileInputStream(file);
       byte[] buffer = new byte[8192];
       int len;
       while ((len = fis.read(buffer)) != -1) {
         digest.update(buffer, 0, len);
       }
       return bytes2Hex(digest.digest());
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       if (fis != null) {
         try {
           fis.close();
         } catch (IOException iOException) {}
       }
     } 
     
     return "";
   }
 
 
 
 
 
 
   
   private static String bytes2Hex(byte[] src) {
     char[] res = new char[src.length << 1];
     char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
     for (int i = 0, j = 0; i < src.length; i++) {
       res[j++] = hexDigits[src[i] >>> 4 & 0xF];
       res[j++] = hexDigits[src[i] & 0xF];
     } 
     return new String(res);
   }
 }


