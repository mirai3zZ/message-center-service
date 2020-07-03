package com.yangyang.cloud.common.export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 导出excel工具类
 */
@Component
public class ExportExcel {


    /**
     * 导出excel法<br>
     *
     * @param response Response对象
     * @param request  Request对象
     * @param fileName Excel文件名
     */
    public void doExport(Workbook wb, HttpServletResponse response, HttpServletRequest request, String fileName) {
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            response.reset();
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent
                    && -1 != agent.indexOf("Trident")) {// ie trident是一种内核 IE内核
                String name = URLEncoder.encode(fileName, "UTF8");
                fileName = name;
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// firefox,chrome
                fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            }
            response.setHeader("Content-disposition", "attachment; filename="
                    + fileName + ".xlsx");
            response.setContentType("application/msexcel");
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("all")
    public static List<Object[]> ListOthersToListObjectArray(List<? extends Object> lists, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        String[] getMethodNames = new String[fields.length];
        List<Object[]> objsList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            String filedName = fields[i].getName();
            getMethodNames[i] = "get" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
        }
        for (Object obj : lists) {
            Object[] objs = new Object[fields.length];
            for (int j = 0; j < getMethodNames.length; j++) {
                Method method = null;
                try {
                    method = clazz.getMethod(getMethodNames[j], new Class[]{});
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                try {
                    objs[j] = method.invoke(obj, new Object[]{});
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            objsList.add(objs);
        }
        return objsList;
    }

    /**
     * 导出excel方法<br>
     *
     * @param response
     * @param dataMap  键值有：<br>
     *                 fileName 导出的文件名称，必填，不用带后缀.xls<br>
     *                 nameArr 导出的列英文名称数组，必填，示例：String[] nameArr = new String[]{"name","sex","age"};<br>
     *                 titleArr 列中文名称数组，必填，示例：String[] titleArr = new String[]{"姓名","性别","年龄"};<br>
     *                 alignArr 列对齐方式数组，非必填，默认居中对齐，示例：String[] alignArr = new String[]{"center","left","right"}<br>
     *                 widthArr 列宽度数组 ，非必填，默认10个汉字宽度，示例：int[] widthArr = new int[]{5,20,10},数字为汉字的个数<br>
     *                 dataList 要导出的数据，List中为javabean或map对象<br>
     */
    public void doExport(HttpServletResponse response, HttpServletRequest request, Map<String, Object> dataMap) {
        String fileName = (String) dataMap.get("fileName");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            response.reset();
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent
                    && -1 != agent.indexOf("Trident")) {// ie trident是一种内核 IE内核
                String name = URLEncoder.encode(fileName, "UTF8");
                fileName = name;
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// firefox,chrome
                fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            }
            response.setHeader("Content-disposition", "attachment; filename="
                    + fileName + ".xls");
            response.setContentType("application/msexcel");
            doExport(os, dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出excel方法<br>
     *
     * @param response
     * @param request
     * @param fileName 导出的文件名称，必填，不用带后缀.xls
     * @param nameArr  导出的列英文名称数组，必填，示例：String[] nameArr = new String[]{"name","sex","age"};
     * @param titleArr 列中文名称数组，必填，示例：String[] titleArr = new String[]{"姓名","性别","年龄"};
     * @param alignArr 列对齐方式数组，非必填，默认居中对齐，示例：String[] alignArr = new String[]{"center","left","right"}
     * @param widthArr 列宽度数组 ，非必填，默认10个汉字宽度，示例：int[] widthArr = new int[]{5,20,10},数字为汉字的个数
     * @param dataList 要导出的数据，List中为javabean或map对象
     */
    public void doExport(HttpServletResponse response, HttpServletRequest request, String fileName, String[] nameArr,
                         String[] titleArr, String[] alignArr, int[] widthArr, List dataList) {
        try {
//			String userAgent = request.getHeader("USER-AGENT");
            OutputStream os = response.getOutputStream();
            response.reset();
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent
                    && -1 != agent.indexOf("Trident")) {// ie trident是一种内核 IE内核
                String name = URLEncoder.encode(fileName, "UTF8");
                fileName = name;
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// firefox,chrome
                fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            }
            response.setHeader("Content-disposition", "attachment; filename="
                    + fileName + ".xlsx");
            response.setContentType("application/msexcel");
            doExport(os, nameArr, titleArr, alignArr, widthArr, dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 导出excel方法<br>
     *
     * @param os      输出流
     * @param dataMap 键值有：<br>
     *                nameArr 导出的列英文名称数组，必填，示例：String[] nameArr = new String[]{"name","sex","age"};<br>
     *                titleArr 列中文名称数组，必填，示例：String[] titleArr = new String[]{"姓名","性别","年龄"};<br>
     *                alignArr 列对齐方式数组，非必填，默认居中对齐，示例：String[] alignArr = new String[]{"center","left","right"}<br>
     *                widthArr 列宽度数组 ，非必填，默认10个汉字宽度，示例：int[] widthArr = new int[]{5,20,10},数字为汉字的个数<br>
     *                dataList 要导出的数据，List中为javabean或map对象<br>
     */
    @SuppressWarnings({"unchecked"})
    public void doExport(OutputStream os, Map<String, Object> dataMap) {
        String[] nameArr = (String[]) dataMap.get("nameArr");
        String[] titleArr = (String[]) dataMap.get("titleArr");
        String[] alignArr = (String[]) dataMap.get("alignArr");
        int[] widthArr = (int[]) dataMap.get("widthArr");
        List<Object> dataList = (List<Object>) dataMap.get("dataList");
        try {
            doExport(os, nameArr, titleArr, alignArr, widthArr, dataList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }


    /**
     * 导出excel方法<br>
     *
     * @param os       流对象
     * @param nameArr  列名(object的字段名或map的key)
     * @param titleArr 标题
     * @param alignArr 对其方式
     * @param widthArr 列宽
     * @param dataList 数据(list object或list map)
     */
    @SuppressWarnings({"unchecked"})
    public void doExport(OutputStream os, String[] nameArr, String[] titleArr,
                         String[] alignArr, int[] widthArr, List dataList) {
        List<CellInfor<String>> titles = new ArrayList<>();
        for (int i = 0; i < titleArr.length; i++) {
            titles.add(new CellInfor<>(titleArr[i], 0, i, 1, 1));
        }
        doExport(os, nameArr, titles, alignArr, widthArr, dataList);
    }

    /**
     * 导出excel方法（可添加合并的表头）<br>
     *
     * @param os       流对象
     * @param nameArr  列名(object的字段名或map的key)
     * @param titles   标题List
     * @param alignArr 对其方式
     * @param widthArr 列宽
     * @param dataList 数据(list object或list map)
     */
    public void doExport(OutputStream os, String[] nameArr, List<CellInfor<String>> titles,
                         String[] alignArr, int[] widthArr, List dataList) {
        Position p = new Position(-1, -1);
        LinkedHashMap<Position, List> dataMap = new LinkedHashMap<>();
        dataMap.put(p, dataList);
        doExport(os, nameArr, titles, alignArr, widthArr, dataMap);
    }

    /**
     * 导出excel方法（可添加合并的表头）<br>
     *
     * @param os       流对象
     * @param nameArr  列名(object的字段名或map的key)
     * @param titles   标题List
     * @param alignArr 对其方式
     * @param widthArr 列宽
     * @param dataMap  Map<Position,List> 数据(position数据开始位置,List数据)
     */
    @SuppressWarnings("resource")
    public void doExport(OutputStream os, String[] nameArr, List<CellInfor<String>> titles,
                         String[] alignArr, int[] widthArr, LinkedHashMap<Position, List> dataMap) {
        String title = "Sheet1";
        try {
            // 声明一个工作薄
            XSSFWorkbook workbook = new XSSFWorkbook();
            // 生成一个表格
            XSSFSheet sheet = workbook.createSheet(title);
            // 设置表格默认列宽度为10个中文字符宽度
            sheet.setDefaultColumnWidth(10 * 2);

            XSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            XSSFFont titleFontStyle = workbook.createFont();
            //设置标题字体样式
            titleFontStyle.setFontName("宋体");
            //设置字体高度
            titleFontStyle.setFontHeightInPoints((short) 10);
            //设置粗体
            titleFontStyle.setBold(true);
            titleStyle.setFont(titleFontStyle);

            //添加标题
            int k = 0;
            for (CellInfor<String> cellInfo : titles) {
                XSSFRow row = sheet.getRow(cellInfo.getRowNum());
                if (row == null) {
                    row = sheet.createRow(cellInfo.getRowNum());
                }
                XSSFCell cell = row.createCell(cellInfo.getColNum());
                cell.setCellValue(cellInfo.getValue());
                cell.setCellStyle(titleStyle);
                //设置列宽
                if (widthArr != null && widthArr.length > 0) {
                    sheet.setColumnWidth(k, widthArr[k] * 2 * 256);
                }
                k++;
            }
            //合并单元格
            List<CellRangeAddress> cellRangeAddressList = CellInforUtil.getCellRangeAddressArray(titles);
            for (CellRangeAddress cra : cellRangeAddressList) {
                sheet.addMergedRegion(cra);
            }

            //设置每行的样式
            XSSFCellStyle contentStyle = workbook.createCellStyle();
            XSSFFont contentFontStyle = workbook.createFont();
            //设置字体样式
            contentFontStyle.setFontName("宋体");
            //设置字体高度
            contentFontStyle.setFontHeightInPoints((short) 10);
            contentStyle.setWrapText(true);
            contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentStyle.setFont(contentFontStyle);
            //判断是否为Map
            Boolean isMap = false;
            //添加数据
            int currentRowN = sheet.getLastRowNum();
            int lastRowN = sheet.getLastRowNum() + 1;
            for (Map.Entry<Position, List> entry : dataMap.entrySet()) {
                List dataList = entry.getValue();
                Position startPosition = entry.getKey();
                for (int i = 0; i < dataList.size(); i++) {
                    Map<String, Object> data = null;
                    Object obj = dataList.get(i);
                    if (obj instanceof Map) {
                        isMap = true;
                        data = (Map<String, Object>) obj;
                    }
                    int startRow = startPosition.getRowNum() == -1
                            ? lastRowN : startPosition.getRowNum();
                    XSSFRow row = sheet.getRow(i + startRow);
                    if (row == null) {
                        row = sheet.createRow(i + startRow);
                    }
                    //设置对齐方式，默认居中对齐
                    for (int j = 0; j < nameArr.length; j++) {
                        if (alignArr == null || alignArr.length == 0) {
                            contentStyle.setAlignment(HorizontalAlignment.CENTER);
                        } else {
                            contentStyle.setAlignment(changeAlign(alignArr[j]));
                        }
                        int startCol = startPosition.getColNum() == -1
                                ? 0 : startPosition.getColNum();
                        XSSFCell cell = row.createCell(j + startCol);
                        Object value = null;
                        if (isMap) {
                            value = data.get(nameArr[j]);
                        } else {
                            value = methodInvoke(obj, nameArr[j]);
                        }
                        String textValue = "";
                        if (value instanceof Date) {
                            Date date = (Date) value;
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            textValue = format.format(date);
                            cell.setCellValue(textValue);
                        } else if (value instanceof Integer || value instanceof Float
                                || value instanceof Double || value instanceof Long
                                || value instanceof BigDecimal) {
                            textValue = changeToStr(value);
                            if (textValue.length() <= 11) {
                                // 是数字当作double处理
                                cell.setCellValue(Double.parseDouble(textValue));
                            } else {
                                cell.setCellValue(new BigDecimal(textValue).toString());
                            }
                        } else {
                            textValue = changeToStr(value);
                            cell.setCellValue(textValue);
                        }
                        cell.setCellStyle(contentStyle);
                    }
                }
            }

            workbook.write(os);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doExport(Workbook workbook, HttpServletResponse response, String fileName) {
        response.setContentType("APPLICATION/DOWNLOAD");
        try {
            response.setHeader("Content-Disposition", String.format("attachment;fileName= %s", URLEncoder.encode(fileName + ".xlsx", "UTF-8")));
            ServletOutputStream out = null;
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转换对齐方式
     */
    public HorizontalAlignment changeAlign(String align) {
        HorizontalAlignment alignment;
        if ("left".equals(align)) {
            alignment = HorizontalAlignment.LEFT;
        } else if ("center".equals(align)) {
            alignment = HorizontalAlignment.CENTER;
        } else {
            alignment = HorizontalAlignment.RIGHT;
        }
        return alignment;
    }

    /*
     *原类中包含其它类引用
     */
    private Object methodInvoke(Object obj, String str) throws Exception {
        Object object = obj;
        String[] params = str.split("\\.");
        for (int i = 0; i < params.length; i++) {
            if (null == object) {
                continue;
            }
            object = methodInvokeHander(object, params[i]);
        }
        return object;
    }

    private Object methodInvokeHander(Object obj, String str) throws Exception {
        Class clss = obj.getClass();
        String getMethodName = "get" + str.substring(0, 1).toUpperCase() + str.substring(1);
        Method method = clss.getMethod(getMethodName, new Class[]{});
        return method.invoke(obj, new Object[]{});
    }

    private CellStyle getCommonStyle(Workbook workbook) {
        CellStyle commonStyle = workbook.createCellStyle();
        //边框
        commonStyle.setBorderBottom(BorderStyle.MEDIUM);
        commonStyle.setBorderLeft(BorderStyle.MEDIUM);
        commonStyle.setBorderTop(BorderStyle.MEDIUM);
        commonStyle.setBorderRight(BorderStyle.MEDIUM);
        commonStyle.setWrapText(true);
        //内容居中
        commonStyle.setAlignment(HorizontalAlignment.CENTER);
        commonStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //字体样式
        Font cfont = workbook.createFont();
        cfont.setFontName("宋体");
        //字体大小
        cfont.setFontHeightInPoints((short) 11);
        //加粗
        commonStyle.setFont(cfont);
        return commonStyle;
    }

    /**
     * 把object对象转换为字符串，null转换为空字符串
     */
    public String changeToStr(Object value) {
        if (value == null || "null".equals(value)) {
            return "";
        }
        return String.valueOf(value);
    }
}
