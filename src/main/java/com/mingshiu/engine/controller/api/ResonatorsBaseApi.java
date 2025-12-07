package com.mingshiu.engine.controller.api;

import java.util.Map;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mingshiu.engine.model.TempFile;
import com.mingshiu.engine.service.TempFileService;

@RestController
@RequestMapping("/api/resonators/base")
@RequiredArgsConstructor
public class ResonatorsBaseApi {

    private static final String DEFAULT_USER_ID = "demo-user";

    private final TempFileService tempFileService;

    @PostMapping("/temp-file/upload")
    public ResponseEntity<?> uploadTempFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("imageType") Short imageType,
            HttpSession session) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "File must be provided."));
        }
        if (imageType == null || !isSupportedImageType(imageType)) {
            return ResponseEntity.badRequest().body(Map.of("message", "imageType is invalid."));
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body(Map.of("message", "Only image/* files are allowed."));
        }

        try {
            String sessionId = session.getId();
            TempFile tempFile = tempFileService.saveTempFile(DEFAULT_USER_ID, sessionId, imageType, file);
            TempFileUploadResponse response = new TempFileUploadResponse(
                    tempFile.getUserId(),
                    tempFile.getSessionId(),
                    tempFile.getUniqueId(),
                    tempFile.getImageType(),
                    tempFile.getFileName(),
                    tempFile.getContentType());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to upload image."));
        }
    }

    private boolean isSupportedImageType(Short imageType) {
        return imageType == 1 || imageType == 2;
    }

    public record TempFileUploadResponse(
            String userId,
            String sessionId,
            String uniqueId,
            Short imageType,
            String fileName,
            String contentType) {
    }
}
