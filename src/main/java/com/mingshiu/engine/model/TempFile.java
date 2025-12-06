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
public class TempFile {

    private String userId;
    private String sessionId;
    private String uniqueId;
    private Short imageType;
    private String fileName;
    private String contentType;
    private String fileBase64;
    private LocalDateTime createdAt;
}
