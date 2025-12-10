package com.mingshiu.engine.service.upload;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mingshiu.engine.common.Utility;
import com.mingshiu.engine.mapper.UploadTempFileMapper;
import com.mingshiu.engine.model.UploadTempFile;
import com.mingshiu.engine.service.upload.dto.UploadResponse;
import com.mingshiu.engine.service.upload.field.UploadFormField;
import com.mingshiu.engine.service.upload.field.UploadImgFileField;
import com.mingshiu.engine.validation.ValidatorFile;
import com.mingshiu.engine.validation.ValidatorForm;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * UpLoadFileService Class
 */
@Service
@RequiredArgsConstructor
public class UploadService {

  private static final Logger log = LoggerFactory.getLogger(UploadService.class);

  // MyBatis(Mapper)
  private final UploadTempFileMapper mapper;

  // Validator
  private final ValidatorForm formValidator;
  private final ValidatorFile fileValidator;

  /**
   * File Upload 処理（画像）
   *
   * @param file    入力値(ファイル)
   * @param params  入力値
   * @param session Http Session
   * @return 処理結果
   */
  public UploadResponse uploadImg(MultipartFile file, Map<String, String> params, HttpSession session) {
    UploadResponse rtn = new UploadResponse();

    Map<String, String> errors = validate(file, params);
    if (errors == null) {
      return rtn;
    }

    if (!errors.isEmpty()) {
      rtn.error(errors);
      return rtn;
    }

    String userId = "0123456789";
    String sessionId = session.getId();
    String uniqueId = UUID.randomUUID().toString();
    String base64 = Utility.toBase64(file);
    String extension = Utility.getFileExtension(file);
    Integer fileType = Utility.toInt(params.get("FileType"));

    UploadTempFile savedFile = saveTempFile(userId, sessionId, uniqueId, fileType, extension, base64);
    if (savedFile == null) {
      rtn.error("system", "Failed to save uploaded image");
      return rtn;
    }

    rtn.uniqueId = savedFile.getUniqueId();
    rtn.fileName = Utility.isEmpty(file.getOriginalFilename()) ? savedFile.getUniqueId() : file.getOriginalFilename();
    rtn.base64Data = savedFile.getFileBase64();
    rtn.mimeType = resolveMimeType(file, extension);
    rtn.success();
    return rtn;
  }

  /**
   * insert 処理
   *
   * @param userId        userId
   * @param sessionId     sessionId
   * @param uniqueId      uniqueId
   * @param fileType      1:画像(visual)、2:画像(face)
   * @param fileExtension File拡張子
   * @param base64        base64文字列
   * @return insert result
   */
  public UploadTempFile saveTempFile(String userId, String sessionId, String uniqueId, Integer fileType,
      String fileExtension, String base64) {
    try {
      UploadTempFile tmp = UploadTempFile.builder().userId(userId).sessionId(sessionId).uniqueId(uniqueId)
          .fileType(fileType).fileExtension(fileExtension).fileBase64(base64).build();
      int inserted = mapper.insert(tmp);
      if (inserted <= 0) {
        return null;
      }
      return mapper.selectByKey(userId, sessionId, uniqueId, fileType);
    } catch (Exception e) {
      log.error("Failed to insert temp file", e);
    }
    return null;
  }

  /**
   * validate 処理
   *
   * @param file   入力値(ファイル)
   * @param params 入力値
   * @return 判定結果
   */
  public Map<String, String> validate(MultipartFile file, Map<String, String> params) {
    Map<String, String> rtn = new LinkedHashMap<String, String>();
    try {
      Map<String, String> err = null;

      err = formValidator.validate(UploadFormField.class, params);
      rtn.putAll(err);

      err = fileValidator.validate(UploadImgFileField.class, file);
      rtn.putAll(err);
    } catch (Exception e) {
      log.error("Validation execution error", e);
      return null;
    }
    return rtn;
  }

  private String resolveMimeType(MultipartFile file, String extension) {
    String contentType = file.getContentType();
    if (!Utility.isEmpty(contentType)) {
      return contentType;
    }
    if (!Utility.isEmpty(extension)) {
      return "image/" + extension;
    }
    return "image/png";
  }
}
