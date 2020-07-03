package com.yangyang.cloud.common.dictionary.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/**
 * description:数据字典查询返回项
 * @author zhangyongxue
 * @date 2018/11/26 10:13
 */
@Data
public class CommonDictItemVo {
    /** 字典项id*/
    @JsonIgnore
    private String id;
    /** 字典项code*/
    private String code;

    /** 字典项name */
    private String name;
}
