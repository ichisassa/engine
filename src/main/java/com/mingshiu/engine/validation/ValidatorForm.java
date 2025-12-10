package com.mingshiu.engine.validation;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mingshiu.engine.common.Utility;
import com.mingshiu.engine.validation.annotation.FileTypes;
import com.mingshiu.engine.validation.annotation.MaxLength;
import com.mingshiu.engine.validation.annotation.Numeric;
import com.mingshiu.engine.validation.annotation.Required;

@Component
public class ValidatorForm extends ValidatorBase {

  /**
   * validation 処理
   * 
   * @param fieldEnumClass 入力項目 enum
   * @param params         入力値
   * @return Error Message Map
   * @throws IllegalStateException
   */
  public <E extends Enum<E> & FormField> Map<String, String> validate(Class<E> fieldEnumClass,
      Map<String, String> params) {

    Map<String, String> rtn = new LinkedHashMap<>();
    if (fieldEnumClass == null) {
      return rtn;
    }

    E[] fields = fieldEnumClass.getEnumConstants();
    if (fields == null) {
      return rtn;
    }

    for (E field : fields) {
      Field enumField = resolveField(fieldEnumClass, field);
      String paramName = field.paramName();
      String value = normalize(params != null ? params.get(paramName) : null);

      Required required = enumField.getAnnotation(Required.class);
      if (required != null) {
        required(paramName, value, required.message(), rtn);
      }

      Numeric numeric = enumField.getAnnotation(Numeric.class);
      if (numeric != null) {
        numeric(paramName, value, numeric.message(), rtn);
      }

      FileTypes typies = enumField.getAnnotation(FileTypes.class);
      if (typies != null) {
        fileTypies(paramName, value, typies.value(), typies.message(), rtn);
      }

      MaxLength maxLength = enumField.getAnnotation(MaxLength.class);
      if (maxLength != null) {
        length(paramName, value, maxLength.value(), maxLength.message(), rtn);
      }
    }

    return rtn;
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
  protected <E extends Enum<E> & FormField> Field resolveField(Class<E> enumClass, E constant) {
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
   * @param value     入力値
   * @param message   Error Message
   * @param errors    Error Message Map(参照値)
   */
  private void required(String fieldName, String value, String message, Map<String, String> errors) {
    if (hasError(errors, fieldName)) {
      return;
    }
    if (Utility.isEmpty(value)) {
      errors.put(fieldName, message);
    }
  }

  /**
   * Validation 処理(数値)
   * 
   * @param fieldName 項目名
   * @param value     入力値
   * @param message   Error Message
   * @param errors    Error Message Map(参照値)
   */
  private void numeric(String fieldName, String value, String message, Map<String, String> errors) {
    if (hasError(errors, fieldName) || Utility.isEmpty(value)) {
      return;
    }
    Integer v = Utility.toInt(value);
    if (v == null) {
      errors.put(fieldName, message);
      return;
    }
  }

  /**
   * Validation 処理(FileType)
   * 
   * @param fieldName  項目名
   * @param value      入力値
   * @param candidates FileType
   * @param message    Error Message
   * @param errors     Error Message Map(参照値)
   */
  private void fileTypies(String fieldName, String value, int[] candidates, String message,
      Map<String, String> errors) {
    if (hasError(errors, fieldName) || Utility.isEmpty(value)) {
      return;
    }
    Integer v = Utility.toInt(value);
    if (v == null) {
      errors.put(fieldName, message);
      return;
    }
    for (int c : candidates) {
      if (v == c) {
        return;
      }
    }
    errors.put(fieldName, message);
  }

  /**
   * Validation 処理(文字列長)
   * 
   * @param fieldName 項目名
   * @param value     入力値
   * @param message   Error Message
   * @param errors    Error Message Map(参照値)
   */
  private void length(String fieldName, String value, int maxLength, String message, Map<String, String> errors) {
    if (hasError(errors, fieldName) || Utility.isEmpty(value)) {
      return;
    }
    if (value.length() > maxLength) {
      errors.put(fieldName, message);
      return;
    }
  }

  /**
   * 正規化処理
   * 
   * @param params 入力値
   * @return 入力値
   */
  private String normalize(String value) {
    return value == null ? "" : value.trim();
  }
}
