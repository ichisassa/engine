package com.mingshiu.engine.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mingshiu.engine.model.TempFile;

@Mapper
public interface TempFileMapper {
    int insert(TempFile file);
    TempFile selectByKey(
            @Param("userId") String userId,
            @Param("sessionId") String sessionId,
            @Param("uniqueId") String uniqueId,
            @Param("imageType") Short imageType);
}
