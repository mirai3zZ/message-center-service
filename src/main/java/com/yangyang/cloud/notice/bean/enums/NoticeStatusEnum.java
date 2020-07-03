package com.yangyang.cloud.notice.bean.enums;

import org.springframework.util.StringUtils;

/**
 * @author : mengfanlong
 * E-mail: meng.fanlong@inspur.com
 * Date:   2018/9/30 15:32
 * -------------------------------
 * Desc:   公告状态枚举类
 */
public enum NoticeStatusEnum {
    RELEASED("released", "已发布"),
    WITHDRAWN("withdrawn", "已撤回"),
    DRAFT("draft", "草稿");
    private String status;
    private String statusDesc;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    NoticeStatusEnum(String status, String statusDesc) {

        this.status = status;
        this.statusDesc = statusDesc;
    }

    /**
     * @param status
     * @return java.lang.String
     * @desc 根据状态获取描述
     * @author meng.fanlong
     * @date 2018/9/30 16:14
     */
    public static String getDescByStatus(String status) {
        if (StringUtils.isEmpty(status)) {
            return "";
        }
        for (NoticeStatusEnum noticeStatusEnum : NoticeStatusEnum.values()) {
            if (noticeStatusEnum.getStatus().equals(status)) {
                return noticeStatusEnum.getStatusDesc();
            }
        }
        return "";
    }
}
