package com.yangyang.cloud.notice.bean;

import com.yangyang.cloud.notice.bean.enums.NoticeImportLevelEnum;
import com.yangyang.cloud.notice.bean.enums.NoticeStatusEnum;

import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.NotBlank;

/**
 * Author: mengfanlong
 * E-mail: meng.fanlong@inspur.com
 * Date:   2018/9/30 15:20
 * -------------------------------
 * Desc:   公告实体
 */
public class NoticeBean {
    /**
     * 主键
     */
    private BigInteger id;
    /**
     * 发送时间
     */
    private Date createdTime;
    /**
     * 更新时间 默认值 Null
     */
    private Date updatedTime;
    
    /**
     * 公告标题
     */
    @NotBlank
    private String noticeTitle;
    
    /**
     * 公告内容
     */
    @NotBlank
    private String noticeContent;
    
    /**
     * 创建人Id
     */
    private String createdUserId;
    /**
     * 创建名字
     */
    private String createdUserName;
    /**
     * 修改人
     */
    private String updatedUserId;
    /**
     * 修改人名字
     */
    private String updatedUserName;
    /**
     * 重要等级（1,2，3） 默认值1 {@link NoticeImportLevelEnum}
     */
    private Integer importLevel = NoticeImportLevelEnum.LEVEL_NOMAL.getLevel();
    /**
     * 状态 released-已发布；withdrawn-已撤回;草稿-draft {@link NoticeStatusEnum}
     */
    
    @NotBlank
    private String status;
    
    @NotBlank
    private String noticeTypeName;
    
    @NotBlank
    private String noticeTypeId;

    private String sendTime;

    public void setNoticeTypeId(String noticeTypeId) {
        this.noticeTypeId = noticeTypeId;
    }

    public void setNoticeTypeName(String noticeTypeName) {
        this.noticeTypeName = noticeTypeName;
    }

    public String getNoticeTypeId() {
        return noticeTypeId;
    }

    public String getNoticeTypeName() {
        return noticeTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NoticeBean that = (NoticeBean) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedTime, that.updatedTime) &&
                Objects.equals(noticeTitle, that.noticeTitle) &&
                Objects.equals(noticeContent, that.noticeContent) &&
                Objects.equals(createdUserId, that.createdUserId) &&
                Objects.equals(createdUserName, that.createdUserName) &&
                Objects.equals(updatedUserId, that.updatedUserId) &&
                Objects.equals(updatedUserName, that.updatedUserName) &&
                Objects.equals(importLevel, that.importLevel) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, createdTime, updatedTime, noticeTitle, noticeContent, createdUserId, createdUserName, updatedUserId, updatedUserName, importLevel, status);
    }

    @Override
    public String toString() {
        return "NoticeBean{" +
                "id=" + id +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", noticeContent='" + noticeContent + '\'' +
                ", createdUserId='" + createdUserId + '\'' +
                ", createdUserName='" + createdUserName + '\'' +
                ", updatedUserId='" + updatedUserId + '\'' +
                ", updatedUserName='" + updatedUserName + '\'' +
                ", importLevel=" + importLevel +
                ", status='" + status + '\'' +
                '}';
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

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
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

    public String getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(String updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public String getUpdatedUserName() {
        return updatedUserName;
    }

    public void setUpdatedUserName(String updatedUserName) {
        this.updatedUserName = updatedUserName;
    }

    public Integer getImportLevel() {
        return importLevel;
    }

    public void setImportLevel(Integer importLevel) {
        this.importLevel = importLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
