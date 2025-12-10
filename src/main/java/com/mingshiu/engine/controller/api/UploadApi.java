package com.mingshiu.engine.controller.api;

import java.util.Map;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.mingshiu.engine.service.uploadtempfile.UploadTempFileService;
import com.mingshiu.engine.service.uploadtempfile.dto.UploadFileResponse;

/**
 * UploadApi Class
 */
@RestController
@RequiredArgsConstructor
public class UploadTempFileApi {

  private final UploadTempFileService service;

  /**
   * File Upload 処理(画像)
   * 
   * @param file    入力値(ファイル)
   * @param params  入力値
   * @param session Http Session
   * @return 処理結果
   */
  @PostMapping("/api/upload/file/image")
  public ResponseEntity<?> uploadImg(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params,
      HttpSession session) {
    UploadFileResponse response = service.uploadImg(file, params, session);
    return ResponseEntity.ok(response);
  }
}
