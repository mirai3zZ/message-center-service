package com.yangyang.cloud.message.bean;

import org.apache.commons.lang.StringUtils;

/**
 * description: 接收人管理参数
 *
 * @author renhaixiang
 * @version 1.0
 * @date 2018/9/29 20:02
 **/
public class UserMessageReceiverVo {
    /**
     * description: 接收人名称
     **/
    private String receiverName;
    /**
     * description: 接收人邮箱
     **/
    private String receiverEmail;
    /**
     * description: 接收人手机
     **/
    private String receiverMobile;
    /**
     * description: 职位
     **/
    private String emailCheckPass;
    /**
     * description: 分类ID
     **/
    private String mobileCheckPass;
    /**
     * description: 职位
     **/
    private String receiverJob;
    /**
     * description: 分类ID
     **/
    private String messageTypeIds;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverJob() {
        return receiverJob;
    }

    public void setReceiverJob(String receiverJob) {
        this.receiverJob = receiverJob;
    }

    public String getMessageTypeIds() {
        return messageTypeIds;
    }

    public void setMessageTypeIds(String messageTypeIds) {
        this.messageTypeIds = messageTypeIds;
    }

    public String getEmailCheckPass() {
        return emailCheckPass;
    }

    public void setEmailCheckPass(String emailCheckPass) {
        if (StringUtils.isEmpty(emailCheckPass)) {
            this.emailCheckPass = "N";
        }
        this.emailCheckPass = emailCheckPass;
    }

    public String getMobileCheckPass() {
        return mobileCheckPass;
    }

    public void setMobileCheckPass(String mobileCheckPass) {
        if (StringUtils.isEmpty(mobileCheckPass)) {
            this.mobileCheckPass = "N";
        }
        this.mobileCheckPass = mobileCheckPass;
    }
}
