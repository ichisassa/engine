package com.mingshiu.engine.validation;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mingshiu.engine.validation.annotation.ImageContentType;
import com.mingshiu.engine.validation.annotation.RequiredFile;

@Component
public class FileValidator {

  public <E extends Enum<E> & FileField> Map<String, String> validate(Class<E> fieldEnumClass, MultipartFile file) {
    Map<String, String> errors = new LinkedHashMap<>();

    if (fieldEnumClass == null) {
      return errors;
    }
    E[] fields = fieldEnumClass.getEnumConstants();
    if (fields == null) {
      return errors;
    }

    for (E field : fields) {
      Field enumField = resolveField(fieldEnumClass, field);
      String fieldName = ((Enum<?>) field).name();

      RequiredFile requiredFile = enumField.getAnnotation(RequiredFile.class);
      if (requiredFile != null) {
        validateRequiredFile(fieldName, requiredFile.message(), file, errors);
      }

      ImageContentType imageContentType = enumField.getAnnotation(ImageContentType.class);
      if (imageContentType != null) {
        validateImageContentType(fieldName, imageContentType.message(), file, errors);
      }
    }

    return errors;
  }

  private <E extends Enum<E> & FileField> Field resolveField(Class<E> enumClass, E constant) {
    try {
      return enumClass.getField(((Enum<?>) constant).name());
    } catch (NoSuchFieldException ex) {
      throw new IllegalStateException("Enum field not found: " + constant.name(), ex);
    }
  }

  private void validateRequiredFile(String fieldName, String message, MultipartFile file,
      Map<String, String> errors) {
    if (file == null || file.isEmpty()) {
      if (!errors.containsKey(fieldName)) {
        errors.put(fieldName, message);
      }
    }
  }

  private void validateImageContentType(String fieldName, String message, MultipartFile file,
      Map<String, String> errors) {
    if (file == null || file.isEmpty() || errors.containsKey(fieldName)) {
      return;
    }
    String contentType = file.getContentType();
    if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
      errors.put(fieldName, message);
    }
  }
}
