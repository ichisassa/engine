package com.mingshiu.engine.service.dto;

public class UploadValidationResult {

  public boolean isError;
  public String message;
  public Integer fileType;
  public String base64;

  public void success(Integer fileType, String base64) {
    this.isError = false;
    this.message = "";
    this.fileType = fileType;
    this.base64 = base64;
  }

  public void error(String msg) {
    this.isError = true;
    this.message = msg;
    this.fileType = null;
    this.base64 = null;
  }
}

