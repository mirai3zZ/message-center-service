package com.yangyang.cloud.message.bean;

import java.math.BigInteger;
import java.util.Date;

/**
 *
 * 功能描述: 表common_user_message表 对应实体
 *
 * @author chenxinyi
 * @date 2018/9/29 17:12
 */
public class CommonUserMessageEntity {
    /**
     * description: 唯一标识
     **/
    private BigInteger id;
    /**
     * description: 发送内容
     **/
    private String content;
    /**
     * description: 创建时间
     **/
    private Date createdTime;
    /**
     * description: 手机号
     **/
    private String mobile;
    /**
     * description: 邮箱
     **/
    private String email;
    /**
     * description: 发送人ID
     **/
    private String sentUserId;
    /**
     * description: 发送错误次数
     **/
    private int sendErrorTimes;
    /**
     * description: 发送时间
     **/
    private Date sendTime;
    /**
     * description: 主题（邮件）
     **/
    private String subject;
    /**
     * description: 类型（EMAIL/SMS）
     **/
    private String type;
    /**
     * description: 状态（Y成功 N失败）
     **/
    private String status;

    /**
     *失败原因
     */
    private String smsFailedReason;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSentUserId() {
        return sentUserId;
    }

    public void setSentUserId(String sentUserId) {
        this.sentUserId = sentUserId;
    }

    public int getSendErrorTimes() {
        return sendErrorTimes;
    }

    public void setSendErrorTimes(int sendErrorTimes) {
        this.sendErrorTimes = sendErrorTimes;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSmsFailedReason() {
        return smsFailedReason;
    }

    public void setSmsFailedReason(String smsFailedReason) {
        this.smsFailedReason = smsFailedReason;
    }

    @Override
    public String toString() {
        return "CommonUserMessageEntity{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdTime=" + createdTime +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", sentUserId='" + sentUserId + '\'' +
                ", sendErrorTimes=" + sendErrorTimes +
                ", sendTime=" + sendTime +
                ", subject='" + subject + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public CommonUserMessageEntity(BigInteger id, String content, Date createdTime, String mobile, String email, Date sendTime, String subject, String type,String status,String smsFailedReason) {
        this.id = id;
        this.content = content;
        this.createdTime = createdTime;
        this.mobile = mobile;
        this.email = email;
        this.sendTime = sendTime;
        this.subject = subject;
        this.type = type;
        this.status=status;
        this.smsFailedReason= smsFailedReason;
    }
}
