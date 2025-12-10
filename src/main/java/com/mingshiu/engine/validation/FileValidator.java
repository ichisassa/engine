package com.mingshiu.engine.validation;

import java.lang.reflect.Field;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mingshiu.engine.common.Utills;
import com.mingshiu.engine.validation.annotation.FileBase64;
import com.mingshiu.engine.validation.annotation.ImageContentType;
import com.mingshiu.engine.validation.annotation.RequiredFile;

@Component
public class FileValidator {

  public <E extends Enum<E> & FileField> FileValidationResult validate(Class<E> fieldEnumClass, MultipartFile file) {
    FileValidationResult result = new FileValidationResult();

    if (fieldEnumClass == null) {
      return result;
    }
    E[] fields = fieldEnumClass.getEnumConstants();
    if (fields == null) {
      return result;
    }

    for (E field : fields) {
      Field enumField = resolveField(fieldEnumClass, field);

      RequiredFile requiredFile = enumField.getAnnotation(RequiredFile.class);
      if (requiredFile != null) {
        String err = validateRequiredFile(requiredFile.message(), file);
        if (err != null) {
          result.error(err);
          return result;
        }
      }

      ImageContentType imageContentType = enumField.getAnnotation(ImageContentType.class);
      if (imageContentType != null) {
        String err = validateImageContentType(imageContentType.message(), file);
        if (err != null) {
          result.error(err);
          return result;
        }
      }

      FileBase64 fileBase64 = enumField.getAnnotation(FileBase64.class);
      if (fileBase64 != null) {
        String err = convertToBase64(fileBase64.message(), file, result);
        if (err != null) {
          result.error(err);
          return result;
        }
      }
    }

    return result;
  }

  private <E extends Enum<E> & FileField> Field resolveField(Class<E> enumClass, E constant) {
    try {
      return enumClass.getField(((Enum<?>) constant).name());
    } catch (NoSuchFieldException ex) {
      throw new IllegalStateException("Enum field not found: " + constant.name(), ex);
    }
  }

  private String validateRequiredFile(String message, MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return message;
    }
    return null;
  }

  private String validateImageContentType(String message, MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return null;
    }
    String contentType = file.getContentType();
    if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
      return message;
    }
    return null;
  }

  private String convertToBase64(String message, MultipartFile file, FileValidationResult result) {
    if (file == null || file.isEmpty()) {
      return message;
    }
    try {
      String base64 = Utills.toBase64(file);
      if (Utills.isEmpty(base64)) {
        return message;
      }
      result.success(base64);
      return null;
    } catch (Exception e) {
      return message;
    }
  }
}
