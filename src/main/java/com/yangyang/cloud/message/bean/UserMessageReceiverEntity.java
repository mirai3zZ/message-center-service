package com.yangyang.cloud.message.bean;

import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigInteger;
import java.util.Date;

/**
 * description: 表user_message_receiver 对应实体
 *
 * @author renhaixiang
 * @version 1.0
 * @date 2018/9/29 15:59
 **/
public class UserMessageReceiverEntity {
    /**
     * description: 唯一标识
     **/
    /*    @NotNull*/
    private BigInteger id;
    /**
     * description: 发送时间
     **/
    private Date createdTime;
    /**
     * description: 更新时间
     **/
    private Date updatedTime;
    /**
     * description: 创建人id
     **/
    private String createdUserId;
    /**
     * description: 创建人
     **/
    private String createdUserName;
    /**
     * description: 接收人名称
     **/
    @NotBlank
    private String receiverName;
    /**
     * description: 接收人邮箱
     **/
    @NotBlank
    @Email
    private String receiverEmail;
    /**
     * description: 接收人手机
     **/
    @NotBlank
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$")
    private String receiverMobile;
    /**
     * description: 职位
     **/
    private String receiverJob;
    /**
     * description: 邮箱验证。默认N
     **/
    private String emailCheckPass;
    /**
     * description: 手机验证。默认N
     **/
    private String mobileCheckPass;
    /**
     * description: 是否有效（Y/N）
     **/
    private String status;
    /**
     * descripttion:是否为账号联系人
     */
    private String isUserSelf;

    public String getIsUserSelf() {
        return isUserSelf;
    }

    public void setIsUserSelf(String isUserSelf) {
        this.isUserSelf = isUserSelf;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (StringUtils.isEmpty(status)) {
            this.status = "Y";
        }
        this.status = status;
    }
}
