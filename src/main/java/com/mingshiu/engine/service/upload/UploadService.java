package com.mingshiu.engine.service.upload;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mingshiu.engine.common.Utility;
import com.mingshiu.engine.mapper.UploadFileMapper;
import com.mingshiu.engine.model.UploadFile;
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
  private final UploadFileMapper mapper;

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
      rtn.error("system", "zzz");
      return rtn;
    }

    if (!errors.isEmpty()) {
      rtn.error(errors);
      return rtn;
    }

    String userId = "0123456789";
    String sessionId = session.getId();
    String uniqueId = UUID.randomUUID().toString();
    Integer fileType = Utility.toInt(params.get("FileType"));

    UploadFile rs = saveImgFile(userId, sessionId, uniqueId, fileType, file);
    if (rs == null) {
      rtn.error("system", "zzz");
      return rtn;
    }

    rtn.fileBase64 = rs.getFileBase64();
    rtn.contentType = rs.getFileContentType();
    rtn.success();
    return rtn;
  }

  /**
   * insert 処理
   *
   * @param userId    userId
   * @param sessionId sessionId
   * @param uniqueId  uniqueId
   * @param fileType  1:画像(visual)、2:画像(face)
   * @param file      MultipartFile
   * @return 判定結果
   */
  public UploadFile saveImgFile(String userId, String sessionId, String uniqueId, Integer fileType,
      MultipartFile file) {
    UploadFile rtn = null;
    try {
      String base64 = Utility.toBase64(file);
      String exten = Utility.getExtension(file);
      String ctype = Utility.getContentType(file);

      UploadFile tmp = UploadFile.builder().userId(userId).sessionId(sessionId).uniqueId(uniqueId).fileType(fileType)
          .fileExtension(exten).fileContentType(ctype).fileBase64(base64).fileData(null).build();
      int rs = mapper.insert(tmp);
      if (rs > 0) {
        rtn = mapper.selectByKey(userId, sessionId, uniqueId, fileType);
      } else {
        log.error("xxx");
      }
    } catch (Exception e) {
      log.error("xxx", e);
    }
    return rtn;
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
      log.error("xxx", e);
      rtn = null;
    }
    return rtn;
  }
}
