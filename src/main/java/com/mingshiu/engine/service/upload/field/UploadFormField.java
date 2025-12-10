package com.mingshiu.engine.service.upload.field;

import com.mingshiu.engine.validation.FormField;
import com.mingshiu.engine.validation.annotation.FileTypes;
import com.mingshiu.engine.validation.annotation.Numeric;
import com.mingshiu.engine.validation.annotation.Required;

public enum UploadFormField implements FormField {

  @Required(message = "fileType is empty")
  @Numeric(message = "fileType is no number")
  @FileTypes(value = { 1, 2 }, message = "fileType is not define")
  FILE_TYPE("FileType");

  private final String paramName;

  UploadFormField(String paramName) {
    this.paramName = paramName;
  }

  @Override
  public String paramName() {
    return paramName;
  }
}
