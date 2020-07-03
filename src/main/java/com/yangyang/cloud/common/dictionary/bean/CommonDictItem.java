package com.yangyang.cloud.common.dictionary.bean;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/**
 * description:数据字典
 * @author zhangyongxue
 * @date 2018/11/23 15:26
 */
@Data
public class CommonDictItem {

    /** 主键ID */
    private BigInteger id;

    /** 字典项code*/
    private String itemId;

    /** 字典项组ID */
    private String groupId;

    /** 字典项name */
    private String value;

    /** 字典项所属区域 */
    private String regionCode;

    /**  */
    private String azCode;

    /**  字典项描述 */
    private String description;

    /** 字典项排列顺序 */
    private int sort;

    /** 字典项是否生效;1 有效 0无效 */
    private int enabled;

    /** 添加时间 */
    private Date createdTime;

    /** 修改时间 */
    private Date updatedTime;
}
