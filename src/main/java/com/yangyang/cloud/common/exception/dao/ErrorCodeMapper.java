package com.yangyang.cloud.common.exception.dao;

import com.yangyang.cloud.common.exception.model.ErrorCodeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Database Access Layer
 *
 * @author sunguangtao 2018-10-22
 */
@Mapper
@Repository
public interface ErrorCodeMapper {
    @Select("SELECT * FROM common_error_code WHERE project_code = #{projectCode} AND module_code=#{moduleCode} AND error_code=#{errorCode}")
    ErrorCodeEntity getErrorCodeEntity(@Param("projectCode") String projectCode,
                                       @Param("moduleCode") String moduleCode,
                                       @Param("errorCode") String errorCode);
}
