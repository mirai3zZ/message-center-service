package com.yangyang.cloud.common.export;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * excel位置坐标 row与column
 *
 * @author hexinyu
 * @create 2018/11/15 10:29
 */
@Data
@AllArgsConstructor
public class Position {
    /**
     *  单元格起始行号
     */
    int rowNum = -1;
    /**
     * 单元格起始列号
     */
    int colNum = -1;
}
