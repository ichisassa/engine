package com.mingshiu.engine.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mingshiu.engine.model.UploadTempFile;

@Mapper
public interface UploadTempFileMapper {
  int insert(UploadTempFile file);

  UploadTempFile selectByKey(@Param("userId") String userId, @Param("sessionId") String sessionId,
      @Param("uniqueId") String uniqueId, @Param("fileType") Short imageType);
}
