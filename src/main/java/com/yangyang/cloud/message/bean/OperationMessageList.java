package com.yangyang.cloud.message.bean;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/**
 * 对应数据库operation_message_list 表的实体类
 * @author haoxiaopeng
 * @date 2018年12月18日15:38:23
 */
@Data
public class OperationMessageList {
    private BigInteger id;
    private Date createdTime;
    private String createdUserId;
    private String emailSubject;
    private String mailContent;
    private String mailTitle;
    private String emailContent;
    private String smsContent;
    private String messageTemplateType;
    private BigInteger messageMainType;
    private String messageMainName;
    private BigInteger messageType;
    private String messageTypeName;
    private String receiverId;
    private String sendEmailMessage;
    private String sendMailMessage;
    private String sendSmsMessage;
    private String sendByHand;
    private String sendTarget;
    private String status;
    private BigInteger messageId;
}
