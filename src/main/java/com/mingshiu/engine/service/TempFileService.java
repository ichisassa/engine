package com.mingshiu.engine.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mingshiu.engine.mapper.TempFileMapper;
import com.mingshiu.engine.model.TempFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TempFileService {

    private final TempFileMapper tempFileMapper;

    public TempFile saveTempFile(String userId, String sessionId, Short imageType, MultipartFile file) {
        try {
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isBlank()) {
                fileName = "uploaded-image";
            }
            String contentType = file.getContentType();
            if (contentType == null || contentType.isBlank()) {
                contentType = "application/octet-stream";
            }
            TempFile tempFile = TempFile.builder()
                    .userId(userId)
                    .sessionId(sessionId)
                    .uniqueId(UUID.randomUUID().toString())
                    .imageType(imageType)
                    .fileName(fileName)
                    .contentType(contentType)
                    .fileBase64(base64)
                    .build();
            tempFileMapper.insert(tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read uploaded file", e);
        }
    }
}
