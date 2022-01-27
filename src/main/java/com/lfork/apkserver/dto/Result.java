 package com.lfork.apkserver.dto;
 
 public class Result<T> {
   private int code;
   private String message;
   private T data;
   
   public int getCode() {
     return this.code;
   }
   
   public void setCode(int code) {
     this.code = code;
   }
   
   public String getMessage() {
     return this.message;
   }
   
   public void setMessage(String message) {
     this.message = message;
   }
   
   public T getData() {
     return this.data;
   }
   
   public void setData(T data) {
     this.data = data;
   }
 }


