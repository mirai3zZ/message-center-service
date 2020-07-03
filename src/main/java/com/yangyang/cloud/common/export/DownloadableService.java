package com.yangyang.cloud.common.export;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/28.
 */
@Component
abstract public class DownloadableService {
    @Autowired
    HttpServletRequest request;
    @Autowired
    ExportExcel exportExcel;
    public void downloadExcel(List<?> list) {
        OutputStream os = null;
        try {
            os = getResponse().getOutputStream();
            getResponse().reset();
//        	HttpServletRequest request = getRequest();
            setHeader();
            if (getExcelTitle() == null) {
                exportExcel.doExport(os, getExcelColumn(), getExcelTitleList(), new String[]{},
                        new int[]{}, list);
            } else {
                exportExcel.doExport(os, getExcelColumn(), getExcelTitle(), new String[]{},
                        new int[]{}, list);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    public void downloadExcel(LinkedHashMap<Position, List> dataMap) {
        OutputStream os = null;
        try {
            os = getResponse().getOutputStream();
            getResponse().reset();
            setHeader();
            exportExcel.doExport(os, getExcelColumn(), getExcelTitleList(), new String[]{},
                    new int[]{}, dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }



    private void setHeader() throws UnsupportedEncodingException {
        String fileName = getFileName();
        String agent = request.getHeader("USER-AGENT");
        if (null != agent && -1 != agent.indexOf("MSIE") || null != agent
                && -1 != agent.indexOf("Trident")) {// ie trident是一种内核 IE内核
            String name = java.net.URLEncoder.encode(fileName, "UTF8");
            fileName = name;
        } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// firefox,chrome
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        }
        getResponse().setHeader("Content-disposition", "attachment; filename="
                + fileName + ".xlsx");
        getResponse().setContentType("application/msexcel");
    }

    public String[] getExcelTitle() {
        return null;
    }

    public List<CellInfor<String>> getExcelTitleList() {
        return null;
    }

    public abstract String[] getExcelColumn();

    public abstract HttpServletResponse getResponse();

    public abstract String getFileName();
    public String getSheetName(){return null;}
    public List<String> getSheetNameList(){return null;}
}
