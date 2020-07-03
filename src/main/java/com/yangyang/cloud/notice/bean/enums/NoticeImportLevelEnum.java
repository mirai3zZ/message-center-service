package com.yangyang.cloud.notice.bean.enums;

/**
 * @author : mengfanlong
 * E-mail: meng.fanlong@inspur.com
 * Date:   2018/9/30 15:32
 * -------------------------------
 * Desc:   公告重要性枚举类
 */
public enum NoticeImportLevelEnum {
    LEVEL_NOMAL(1, "一般"),
    LEVEL_IMPORTANT(2, "重要"),
    LEVEL_VERY_IMPORTANT(3, "非常重要");
    private Integer level;
    private String levelDesc;


    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }

    NoticeImportLevelEnum(Integer level, String levelDesc) {
        this.level = level;
        this.levelDesc = levelDesc;
    }
}
