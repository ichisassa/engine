package com.mingshiu.engine.validation;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mingshiu.engine.validation.annotation.ImageContentType;
import com.mingshiu.engine.validation.annotation.RequiredFile;

@Component
public class ValidatorFile extends ValidatorBase {

  /**
   * validation 処理
   * 
   * @param fieldEnumClass 入力項目 enum
   * @param params         入力値
   * @return Error Message Map
   * @throws IllegalStateException
   */
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

      RequiredFile required = enumField.getAnnotation(RequiredFile.class);
      if (required != null) {
        required(fieldName, file, required.message(), errors);
      }

      ImageContentType imageContentType = enumField.getAnnotation(ImageContentType.class);
      if (imageContentType != null) {
        imageContentType(fieldName, file, imageContentType.message(), errors);
      }
    }

    return errors;
  }

  /**
   * Field定数取得処理
   * 
   * @param <E>       enum型
   * @param enumClass enum Class
   * @param constant  定数
   * @return enum定数
   * @throws IllegalStateException
   */
  private <E extends Enum<E> & FileField> Field resolveField(Class<E> enumClass, E constant) {
    try {
      return enumClass.getField(((Enum<?>) constant).name());
    } catch (NoSuchFieldException ex) {
      throw new IllegalStateException("Enum field not found: " + constant.name(), ex);
    }
  }

  /**
   * Validation 処理(必須)
   * 
   * @param fieldName 項目名
   * @param file      File
   * @param message   Error Message
   * @param errors    Error Message Map(参照値)
   */
  private void required(String fieldName, MultipartFile file, String message, Map<String, String> errors) {
    if (file == null || file.isEmpty()) {
      if (!errors.containsKey(fieldName)) {
        errors.put(fieldName, message);
      }
    }
  }

  /**
   * Validation 処理(ContentType)
   * 
   * @param fieldName 項目名
   * @param file      File
   * @param message   Error Message
   * @param errors    Error Message Map(参照値)
   */
  private void imageContentType(String fieldName, MultipartFile file, String message, Map<String, String> errors) {
    if (hasError(errors, fieldName)) {
      return;
    }
    if (file == null || file.isEmpty()) {
      return;
    }
    String contentType = file.getContentType();
    if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
      errors.put(fieldName, message);
    }
  }
}
