package com.yangyang.cloud.common.dictionary.vo;

import lombok.Data;

/**数据字典查询返回项
 * @author Administrator
 * @ClassName CommonDictVo
 **/
@Data
public class CommonDictVo {
    /** 字典项code*/
    private String code;

    /** 字典项name */
    private String name;

    /** 父级code*/
    private String parentCode;
}
