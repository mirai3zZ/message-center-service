package com.yangyang.cloud.common.export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * @author hexinyu
 */
public class CellInfor<T> {
    /**
     * 单元格值
     */
    T value;
    /**
     *  单元格起始行号
     */
    int rowNum;
    /**
     * 单元格起始列号
     */
    int colNum;
    /**
     * 单元格跨列数
     */
    int width = 1;
    /*
     * 单元格跨行数
     */
    int height = 1;
    /*
     * 单元格样式
     */
    CellStyle cellStyle;

    public CellInfor(T value, int rowNum, int colNum, int width, int height) {
        this.value = value;
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.width = width;
        this.height = height;
    }
    public CellInfor(T value, int rowNum, int colNum) {
        this.value = value;
        this.rowNum = rowNum;
        this.colNum = colNum;
    }
}
