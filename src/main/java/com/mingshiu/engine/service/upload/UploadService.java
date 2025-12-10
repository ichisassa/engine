package com.mingshiu.engine.service.upload;

import java.util.Map;
import java.util.UUID;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mingshiu.engine.common.Utility;
import com.mingshiu.engine.mapper.UploadTempFileMapper;
import com.mingshiu.engine.model.UploadTempFile;
import com.mingshiu.engine.service.upload.dto.UploadResponse;
import com.mingshiu.engine.service.upload.field.UploadFileField;
import com.mingshiu.engine.service.upload.field.UploadFormField;
import com.mingshiu.engine.validation.FileValidator;
import com.mingshiu.engine.validation.FormValidator;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * UpLoadFileService Class
 */
@Service
@RequiredArgsConstructor
public class UploadService {

  // MyBatis(Mapper)
  private final UploadTempFileMapper mapper;

  // Validator
  private final FormValidator formValidator;
  private final FileValidator fileValidator;

  /**
   * File Upload 処理(画像)
   * 
   * @param file    入力値(ファイル)
   * @param params  入力値
   * @param session Http Session
   * @return 処理結果
   */
  public UploadResponse uploadImg(MultipartFile file, Map<String, String> params, HttpSession session) {

    UploadResponse rtn = new UploadResponse();

    Map<String, String> errors = validate(file, params);
    if (!errors.isEmpty()) {
      rtn.error(errors);
      return rtn;
    }

    String userId = "0123456789";
    String sessionId = session.getId();
    String uniqueId = UUID.randomUUID().toString();
    String base64 = Utility.toBase64(file);
    int fileType = Utility.toInt(params.get("FileType"), 0);

    int rs = saveTempFile(userId, sessionId, uniqueId, fileType, base64);
    if (rs < 0) {
      rtn.error("db", "save file error");
      return rtn;
    }

    rtn.success();
    return rtn;
  }

  /**
   * insert 処理
   * 
   * @param userId    userId
   * @param sessionId sessionId
   * @param uniqueId  uniqueId
   * @param fileType  fileType
   * @param base64    base64文字列
   * @return 処理結果
   */
  public int saveTempFile(String userId, String sessionId, String uniqueId, int fileType, String base64) {
    int rtn = 0;
    try {
      UploadTempFile tmp = UploadTempFile.builder().userId(userId).sessionId(sessionId).uniqueId(uniqueId)
          .fileType(fileType).fileBase64(base64).build();
      rtn = mapper.insert(tmp);
      return rtn;
    } catch (Exception e) {
      rtn = -1;
    }
    return rtn;
  }

  /**
   * validate 処理
   * 
   * @param file   入力値(ファイル)
   * @param params 入力値
   * @return 処理結果
   */
  public Map<String, String> validate(MultipartFile file, Map<String, String> params) {
    Map<String, String> rtn = new LinkedHashMap<String, String>();
    Map<String, String> err = null;

    err = formValidator.validate(UploadFileField.class, params);
    rtn.putAll(err);

    err = fileValidator.validate(UploadFormField.class, file);
    rtn.putAll(err);

    return rtn;
  }
}
