package com.mingshiu.engine.service.uploadtempfile.field;

import com.mingshiu.engine.validation.FormField;
import com.mingshiu.engine.validation.annotation.IntValues;
import com.mingshiu.engine.validation.annotation.Numeric;
import com.mingshiu.engine.validation.annotation.Required;

public enum FileField implements FormField {

  @Required(message = "FileType is empty")
  @Numeric(message = "FileType is no number")
  @IntValues(value = { 1, 2 }, message = "FileType is not define")
  FILE_TYPE("FileType");

  private final String paramName;

  FileField(String paramName) {
    this.paramName = paramName;
  }

  @Override
  public String paramName() {
    return paramName;
  }
}
