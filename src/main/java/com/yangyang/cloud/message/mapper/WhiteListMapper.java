package com.yangyang.cloud.message.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author yangwenshuai
 */


@Mapper
public interface WhiteListMapper {

    List<Map> queryWhiteTypesByUserId(@Param("userId") String userId, @Param("email") String email, @Param("mobile") String mobile);
 }
