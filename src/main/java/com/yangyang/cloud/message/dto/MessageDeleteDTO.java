package com.yangyang.cloud.message.dto;

import com.yangyang.cloud.keycloak.SecurityContextUtil;
import lombok.Data;

import java.util.Date;

@Data
public class MessageDeleteDTO {
    private Date updatedTime;
    /**
     * 默认为当前登录用户
     */
    private String updatedUserId = SecurityContextUtil.getLoginUser().getId();
    private String deleteFlag;
    private String readStatus;
    /**
     * 默认为当前登录用户
     */
    private String receiverId = SecurityContextUtil.getLoginUser().getId();
    private String readTime;
}
