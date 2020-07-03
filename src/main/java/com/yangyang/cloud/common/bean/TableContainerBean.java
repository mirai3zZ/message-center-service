package com.yangyang.cloud.common.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yuanye
 * @date 2018-12-22
 * Desc:封装表格bean
 */
@Data
public class TableContainerBean {
    @NotNull
    private int row;
    @NotNull
    private int column;
    @NotNull
    private String splitChar;
    @NotNull
    private String content;
}
