package com.mingshiu.engine.service.upload.field;

import com.mingshiu.engine.validation.annotation.FileBase64;
import com.mingshiu.engine.validation.annotation.ImageContentType;
import com.mingshiu.engine.validation.annotation.RequiredFile;
import com.mingshiu.engine.validation.FileField;

public enum UploadImgFileField implements FileField {

  @RequiredFile(message = "file is empty")
  @ImageContentType(message = "file is no image")
  @FileBase64(message = "file to base64 error")
  IMAGE;
}
