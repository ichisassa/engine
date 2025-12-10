package com.mingshiu.engine.validation.file;

public class FileValidationResult {

  public boolean isError;
  public String message;
  public String base64;

  public void success(String base64) {
    this.isError = false;
    this.message = "";
    this.base64 = base64;
  }

  public void error(String msg) {
    this.isError = true;
    this.message = msg;
    this.base64 = null;
  }
}

