package com.mingshiu.engine.validation;

import java.util.Map;

public class ValidatorBase {

  /**
   * Error 判定
   * 
   * @param errors    Error Message Map(参照値)
   * @param fieldName 項目名
   * @return 入力値
   */
  protected boolean hasError(Map<String, String> errors, String fieldName) {
    return errors.containsKey(fieldName);
  }
}
