package com.yangyang.cloud.common;

import com.yangyang.cloud.common.bean.CommonConfigBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author from huazhen
 * Desc: get common config from table common_config
 */
@Repository
@Mapper
public interface CommonConfig {
    /**
     * get common config by name and groupName
     * @param groupName
     * @return
     */
    CommonConfigBean getConfig(@Param("name") String name, @Param("groupName") String groupName);


    /**
     * @param groupName
     * @return
     */
    List<CommonConfigBean> getConfigList(@Param("groupName") String groupName);

    /**
     * @param id
     * @return
     */
   int updateConfigValueById(@Param("id") BigInteger id,@Param("value") Integer value);
}
