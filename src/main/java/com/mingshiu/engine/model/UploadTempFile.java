package com.mingshiu.engine.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadTempFile {
  private String userId;
  private String sessionId;
  private String uniqueId;
  private Integer fileType;
  private String fileExtension;
  private String fileBase64;
  private LocalDateTime createdAt;
}
