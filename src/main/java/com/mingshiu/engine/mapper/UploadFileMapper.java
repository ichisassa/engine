package com.mingshiu.engine.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mingshiu.engine.model.UploadFile;

@Mapper
public interface UploadFileMapper {
  int insert(UploadFile file);

  UploadFile selectByKey(@Param("userId") String userId, @Param("sessionId") String sessionId,
      @Param("uniqueId") String uniqueId, @Param("fileType") Integer fileType);
}
