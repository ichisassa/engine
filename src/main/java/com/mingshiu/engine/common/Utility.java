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

  public static int toInt(String val, int def) {
    int rtn = def;
    try {
      return Integer.parseInt(val);
    } catch (Exception e) {
      rtn = def;
    }
    return rtn;
  }

  public static short toShort(String val, short def) {
    short rtn = def;
    try {
      return Short.parseShort(val);
    } catch (Exception e) {
      rtn = def;
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
}
