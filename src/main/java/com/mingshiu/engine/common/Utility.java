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
    try {
      return Integer.parseInt(val);
    } catch (Exception e) {
      return null;
    }
  }

  public static Short toShort(String val) {
    try {
      return Short.parseShort(val);
    } catch (Exception e) {
      return null;
    }
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
}
