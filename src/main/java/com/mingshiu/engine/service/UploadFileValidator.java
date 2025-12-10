package com.mingshiu.engine.service;

import java.util.Map;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mingshiu.engine.common.Utills;
import com.mingshiu.engine.service.dto.UploadValidationResult;
import com.mingshiu.engine.validation.FormValidator;
import com.mingshiu.engine.validation.file.FileValidationResult;
import com.mingshiu.engine.validation.file.FileValidator;
import com.mingshiu.engine.validation.upload.UploadFileField;
import com.mingshiu.engine.validation.upload.UploadTempFileField;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UploadFileValidator {

  private final FormValidator formValidator;
  private final FileValidator fileValidator;

  public UploadValidationResult validateImageUpload(MultipartFile file, Map<String, String> params) {
    UploadValidationResult rtn = new UploadValidationResult();

    Map<String, String> errors = formValidator.validate(UploadTempFileField.class, params);
    if (!errors.isEmpty()) {
      rtn.error(errors.values().iterator().next());
      return rtn;
    }

    String val = params != null ? params.get(UploadTempFileField.FILE_TYPE.paramName()) : null;
    int num = Utills.toInt(val, 0);

    FileValidationResult fileResult = fileValidator.validate(UploadFileField.class, file);
    if (fileResult.isError) {
      rtn.error(fileResult.message);
      return rtn;
    }

    rtn.success(num, fileResult.base64);
    return rtn;
  }
}
