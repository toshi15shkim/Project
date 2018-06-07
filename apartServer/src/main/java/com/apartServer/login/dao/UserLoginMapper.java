package com.apartServer.login.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserLoginMapper {

    Map<String, Object> selectUserInfo(@Param("id") String userId);

    void updateLoginUser(@Param("userNo") String userNo);
    
}
