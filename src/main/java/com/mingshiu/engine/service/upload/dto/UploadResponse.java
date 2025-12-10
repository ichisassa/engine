package com.mingshiu.engine.service.upload.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class UploadResponse {

  public boolean isError;
  public Map<String, String> messages;

  public UploadResponse() {
    this.isError = false;
    this.messages = new LinkedHashMap<>();
  }

  public void success() {
    this.isError = false;
  }

  public void error(Map<String, String> map) {
    this.isError = true;
    this.messages.putAll(map);
  }

  public void error(String key, String val) {
    this.isError = true;
    this.messages.put(key, val);
  }

}
