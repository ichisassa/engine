package com.mingshiu.engine.validation;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mingshiu.engine.validation.annotation.IntValues;
import com.mingshiu.engine.validation.annotation.MaxLength;
import com.mingshiu.engine.validation.annotation.Numeric;
import com.mingshiu.engine.validation.annotation.Required;

@Component
public class FormValidator {

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
        validateRequired(paramName, value, required.message(), rtn);
      }

      Numeric numeric = enumField.getAnnotation(Numeric.class);
      if (numeric != null) {
        validateNumeric(paramName, value, numeric.message(), rtn);
      }

      IntValues intValues = enumField.getAnnotation(IntValues.class);
      if (intValues != null) {
        validateIntValues(paramName, value, intValues.value(), intValues.message(), rtn);
      }

      MaxLength maxLength = enumField.getAnnotation(MaxLength.class);
      if (maxLength != null) {
        validateLength(paramName, value, maxLength.value(), maxLength.message(), rtn);
      }
    }

    return rtn;
  }

  private <E extends Enum<E> & FormField> Field resolveField(Class<E> enumClass, E constant) {
    try {
      return enumClass.getField(((Enum<?>) constant).name());
    } catch (NoSuchFieldException ex) {
      throw new IllegalStateException("Enum field not found: " + constant.name(), ex);
    }
  }

  private void validateRequired(String fieldName, String value, String message, Map<String, String> errors) {
    if (value.isEmpty() && !errors.containsKey(fieldName)) {
      errors.put(fieldName, message);
    }
  }

  private void validateNumeric(String fieldName, String value, String message, Map<String, String> errors) {
    if (value.isEmpty() || errors.containsKey(fieldName)) {
      return;
    }
    try {
      Integer.parseInt(value);
    } catch (NumberFormatException ex) {
      errors.put(fieldName, message);
    }
  }

  private void validateIntValues(String fieldName, String value, int[] candidates, String message,
      Map<String, String> errors) {
    if (value.isEmpty() || errors.containsKey(fieldName)) {
      return;
    }
    try {
      int v = Integer.parseInt(value);
      for (int c : candidates) {
        if (v == c) {
          return;
        }
      }
      errors.put(fieldName, message);
    } catch (NumberFormatException ex) {
      errors.put(fieldName, message);
    }
  }

  private void validateLength(String fieldName, String value, int maxLength, String message,
      Map<String, String> errors) {
    if (!value.isEmpty() && value.length() > maxLength && !errors.containsKey(fieldName)) {
      errors.put(fieldName, message);
    }
  }

  private String normalize(String value) {
    return value == null ? "" : value.trim();
  }
}
