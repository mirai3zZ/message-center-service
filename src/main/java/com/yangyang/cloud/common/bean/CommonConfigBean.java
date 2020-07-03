package com.yangyang.cloud.common.bean;

import lombok.Data;

import java.math.BigInteger;

/**
 * @author yuanye
 * @date 2018/11/8
 */
@Data
public class CommonConfigBean {
    private BigInteger id;
    private String name;
    private String value;
    private String regionCode;
    private String azCode;
    private String groupName;
    private String useStatus;
    private String deleteStatus;
}
