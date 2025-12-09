package com.mingshiu.engine.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mingshiu.engine.common.Utills;
import com.mingshiu.engine.mapper.UploadTempFileMapper;
import com.mingshiu.engine.model.UploadTempFile;
import com.mingshiu.engine.service.dto.UploadFileResponse;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * UpLoadFileService Class
 */
@Service
@RequiredArgsConstructor
public class UploadTempFileService {

  private static enum FileType {

    VISUAL(1), FACE(2);

    private final int type;

    FileType(int type) {
      this.type = type;
    }

    public int getType() {
      return this.type;
    }

    public static FileType toFileType(int type) {
      FileType rtn = null;
      for (FileType t : values()) {
        if (t.type == type) {
          rtn = t;
        }
      }
      return rtn;
    }
  }

  // MyBatis(Mapper)
  private final UploadTempFileMapper mapper;

  /**
   * File Upload 処理(画像)
   * 
   * @param file    入力値(ファイル)
   * @param params  入力値
   * @param session Http Session
   * @return 処理結果
   */
  public UploadFileResponse uploadImg(MultipartFile file, Map<String, String> params, HttpSession session) {

    UploadFileResponse rtn = new UploadFileResponse();

    String val = params.get("FileType");
    if (Utills.isEmpty(val)) {
      rtn.error("FileType is empty");
      return rtn;
    }

    int num = Utills.toInt(val, 0);
    if (num == 0) {
      rtn.error("FileType is no number");
      return rtn;
    }

    FileType type = FileType.toFileType(num);
    if (type == null) {
      rtn.error("FileType is not define");
      return rtn;
    }

    if (file == null || file.isEmpty()) {
      rtn.error("File is empty");
      return rtn;
    }

    if (file == null || file.isEmpty()) {
      rtn.error("File is empty");
      return rtn;
    }

    String contentType = file.getContentType();
    if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
      rtn.error("File is no image");
      return rtn;
    }

    String base64 = Utills.toBase64(file);
    if (file == null || file.isEmpty()) {
      rtn.error("to base64 error");
      return rtn;
    }

    String userId = "0123456789";
    String sessionId = session.getId();
    String uniqueId = UUID.randomUUID().toString();
    int fileType = type.getType();

    int rs = saveTempFile(userId, sessionId, uniqueId, fileType, base64);
    if (rs < 0) {
      rtn.error("to base64 error");
      return rtn;
    }

    rtn.success("");
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
}
