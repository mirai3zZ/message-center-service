package com.yangyang.cloud.message.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Pattern;
import java.math.BigInteger;
import java.util.Date;

/**
 * 消息接收人设置，user_message_receiver_setting表的bean
 *
 * @author chenxinyi
 * @date 2018/9/28 10:29
 */
public class UserMessageReceiverSettingEntity {

    private BigInteger id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
    private String createdUserId;
    private String updatedUserId;
    private BigInteger messageSettingId;
    private BigInteger receiverId;
    @Pattern(regexp="^(Y|N|y|n)$", message="status只能为Y、y或者N、n")
    private String status;

    public UserMessageReceiverSettingEntity() {
        super();
    }

    public UserMessageReceiverSettingEntity(BigInteger messageSettingId, BigInteger receiverId, String status) {
        super();
        this.messageSettingId = messageSettingId;
        this.receiverId = receiverId;
        this.status = status;
    }

    public UserMessageReceiverSettingEntity(BigInteger id, Date createdTime, Date updatedTime, String createdUserId, String updatedUserId, BigInteger messageSettingId, BigInteger receiverId, String status) {
        this.id = id;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.createdUserId = createdUserId;
        this.updatedUserId = updatedUserId;
        this.messageSettingId = messageSettingId;
        this.receiverId = receiverId;
        this.status = status;
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

    public String getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(String updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public BigInteger getMessageSettingId() {
        return messageSettingId;
    }

    public void setMessageSettingId(BigInteger messageSettingId) {
        this.messageSettingId = messageSettingId;
    }

    public BigInteger getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(BigInteger receiverId) {
        this.receiverId = receiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserMessageReceiverSettingEntity{" +
                "id=" + id +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", createdUserId='" + createdUserId + '\'' +
                ", updatedUserId='" + updatedUserId + '\'' +
                ", messageSettingId=" + messageSettingId +
                ", receiverId=" + receiverId +
                ", status='" + status + '\'' +
                '}';
    }
}
