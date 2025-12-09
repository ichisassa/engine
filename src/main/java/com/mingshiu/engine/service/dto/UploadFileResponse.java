package com.mingshiu.engine.service.dto;

public class UploadFileResponse {

  public boolean isError;
  public String message;

  public void success(String msg) {
    this.isError = false;
    this.message = msg;
  }

  public void error(String msg) {
    this.isError = true;
    this.message = msg;
  }
}
