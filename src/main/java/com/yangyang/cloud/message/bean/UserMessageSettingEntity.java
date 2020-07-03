package com.yangyang.cloud.message.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 用户接收消息设置，user_message_setting表的bean
 *
 * @author chenxinyi
 * @date 2018/9/28 10:29
 */
@Data
public class UserMessageSettingEntity {
    private BigInteger id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
    private String createdUserId;
    private BigInteger messageTypeId;
    private String messageTypeName;
    private BigInteger mainTypeId;
    private String mainTypeName;
    @NotBlank
    private String sendMailSetting;
    @NotBlank
    private String sendEmailSetting;
    @NotBlank
    private String sendMobileSetting;
    private String status;
    private String canEdit;

    private List<UserMessageReceiverInfoEntity> data;
    private Integer priority;
    private String explainOption;

    private String explainText;
}
