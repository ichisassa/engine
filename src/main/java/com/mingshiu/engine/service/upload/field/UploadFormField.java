package com.mingshiu.engine.service.upload.field;

import com.mingshiu.engine.validation.annotation.FileBase64;
import com.mingshiu.engine.validation.annotation.ImageContentType;
import com.mingshiu.engine.validation.annotation.RequiredFile;
import com.mingshiu.engine.validation.FileField;

public enum UploadFormField implements FileField {

  @RequiredFile(message = "File is empty")
  @ImageContentType(message = "File is no image")
  @FileBase64(message = "to base64 error")
  IMAGE;
}
