package com.yangyang.cloud.common.bean;

import lombok.Data;

/**
 * @author yuanye
 * DESC:get user info
 */
@Data
public class UserInfoBean {
    private String userId;
    private String userName;
    private String createdTime;
    private String userLevel;
    private String provinceId;
    private String cityId;
    private String status;
    private String statusStr;
    private String knowFrom;
    private String consumptionLevel;
    private String userType;
    private String userTypeLevel;
    private String mobile;
    private String email;
    private String role;
    private String accountId;
    private String companyName;
    private int start = 0;
    private int length = 10;
}
