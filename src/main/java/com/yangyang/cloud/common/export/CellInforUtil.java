package com.yangyang.cloud.common.export;

import org.apache.poi.ss.util.CellRangeAddress;

import java.util.LinkedList;
import java.util.List;

public class CellInforUtil {
    public static List<CellRangeAddress> getCellRangeAddressArray(List<? extends CellInfor> cellInfors) {

        List<CellRangeAddress> cellRangeAddressList = new LinkedList<>();
        for (CellInfor cellInfor : cellInfors) {
            if (cellInfor.getWidth() > 1 || cellInfor.getHeight() > 1) {
                cellRangeAddressList.add(new CellRangeAddress(cellInfor.getRowNum(), cellInfor.getRowNum() + cellInfor.getHeight() - 1, cellInfor.getColNum(), cellInfor.getColNum() + cellInfor.getWidth() - 1));
            }
        }
        return cellRangeAddressList;
    }
}
