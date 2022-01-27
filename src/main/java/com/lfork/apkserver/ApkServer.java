 package com.lfork.apkserver;
 
 import com.lfork.apkserver.other.ApkFileProperties;
 import org.springframework.boot.SpringApplication;
 import org.springframework.boot.autoconfigure.SpringBootApplication;
 import org.springframework.boot.context.properties.EnableConfigurationProperties;
 
 
 
 @SpringBootApplication
 @EnableConfigurationProperties({ApkFileProperties.class})
 public class ApkServer
 {
   public static void main(String[] args) {
     SpringApplication.run(com.lfork.apkserver.ApkServer.class, args);
   }
 }


