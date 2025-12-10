package com.mingshiu.engine.common;

import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

public class Utility {

  public static boolean isEmpty(String val) {
    boolean rtn = false;
    if (val == null || val.isEmpty()) {
      rtn = true;
    }
    return rtn;
  }

  public static Integer toInt(String val) {
    Integer rtn = null;
    try {
      rtn = Integer.parseInt(val);
    } catch (Exception e) {
    }
    return rtn;
  }

  public static Short toShort(String val) {
    Short rtn = null;
    try {
      rtn = Short.parseShort(val);
    } catch (Exception e) {
    }
    return rtn;
  }

  public static String toBase64(MultipartFile file) {
    String rtn = null;
    try {
      byte[] bytes = file.getBytes();
      rtn = Base64.getEncoder().encodeToString(bytes);
    } catch (Exception e) {
    }
    return rtn;
  }

  public static String getExtension(MultipartFile file) {
    String rtn = null;
    if (file != null) {
      String fname = file.getOriginalFilename();
      if (!isEmpty(fname)) {
        int idx = fname.lastIndexOf('.');
        if (idx >= 0 && idx < fname.length() - 1) {
          rtn = fname.substring(idx + 1).toLowerCase();
        }
      }
    }
    return rtn;
  }

  public static String getContentType(MultipartFile file) {
    String rtn = null;
    if (file != null) {
      String contentType = file.getContentType();
      if (!Utility.isEmpty(contentType)) {
        rtn = contentType;
      }
    }
    return rtn;
  }
}
