package com.yangyang.cloud.message.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/**
 * description: 表 operation_message 对应实体
 *
 * @author renhaixiang
 * @version 1.0
 * @date 2018/9/28 10:49
 **/
@Data
public class OperationMessageEntity implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3942787515690477591L;
    /**
     * description: 唯一标识
     **/
    private BigInteger id;
    /**
     * description: 发送人
     **/
    private String sendUserId;
    /**
     * description: 发送人名称
     **/
    private String sendUserName;
    /**
     * description: 发送时间
     **/
    private Date createdTime;
    /**
     * description: 更新时间
     **/
    private Date updatedTime;
    /**
     * description: 修改人
     **/
    private String updatedUserId;
    /**
     * description: 信息标题
     **/
    private String title;
    /**
     * description: 信息内容
     **/
    private String content;
    /**
     * description: 消息模板类型（mail站内信，email，message）
     **/
    @JsonIgnore
    private String messageTemplateType;
    /**
     * description: 发送信息状态(success/failure)
     **/
    @JsonIgnore
    private String status;
    /**
     * description: 标记状态，取值：未标记N,已标记Y。默认N
     **/
    private String isTag;
    /**
     * description: 接受用户ID
     **/
    private String receiverId;
    /**
     * description: 阅读时间
     **/
    private Date readTime;
    /**
     * description: 信息阅读状态,未读N，已读Y
     **/
    private String readStatus;
    /**
     * description: 消息分类
     **/
    private BigInteger messageMainType;
    /**
     * description: 消息分类名称
     **/
    private String messageMainName;
    /**
     * description: 消息类型
     **/
    private BigInteger messageType;
    /**
     * description: 消息类型名称
     **/
    private String messageTypeName;
    /**
     * description: 删除标识，default N， 删除后保存删的时间。
     **/
    private String deleteFlag;

    public String getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
    }

    private String receiverContact;

    private String receiver;

    private String usingObjectType;

    /**
     * 用户登录ID，平台账号
     */
    private String receiverLoginId;
    private BigInteger messageId;
    private BigInteger commonUserMessageId;
    public String getReceiverLoginId() {
        return receiverLoginId;
    }

    public void setReceiverLoginId(String receiverLoginId) {
        this.receiverLoginId = receiverLoginId;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
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

    public String getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(String updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageTemplateType() {
        return messageTemplateType;
    }

    public void setMessageTemplateType(String messageTemplateType) {
        this.messageTemplateType = messageTemplateType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsTag() {
        return isTag;
    }

    public void setIsTag(String isTag) {
        this.isTag = isTag;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public BigInteger getMessageMainType() {
        return messageMainType;
    }

    public void setMessageMainType(BigInteger messageMainType) {
        this.messageMainType = messageMainType;
    }

    public String getMessageMainName() {
        return messageMainName;
    }

    public void setMessageMainName(String messageMainName) {
        this.messageMainName = messageMainName;
    }

    public BigInteger getMessageType() {
        return messageType;
    }

    public void setMessageType(BigInteger messageType) {
        this.messageType = messageType;
    }

    public String getMessageTypeName() {
        return messageTypeName;
    }

    public void setMessageTypeName(String messageTypeName) {
        this.messageTypeName = messageTypeName;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public BigInteger getMessageId() {
        return messageId;
    }

    public void setMessageId(BigInteger messageId) {
        this.messageId = messageId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getUsingObjectType() {
        return usingObjectType;
    }

    public void setUsingObjectType(String usingObjectType) {
        this.usingObjectType = usingObjectType;
    }
}