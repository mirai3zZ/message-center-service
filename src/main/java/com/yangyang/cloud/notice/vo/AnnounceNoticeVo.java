package com.yangyang.cloud.notice.vo;

import com.yangyang.cloud.notice.bean.enums.NoticeImportLevelEnum;
import com.yangyang.cloud.notice.bean.enums.NoticeStatusEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigInteger;
import java.util.Date;

@Data
public class AnnounceNoticeVo {
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
    private String noticeTypeId;
    @NotBlank
    private String noticeTypeName;
}
