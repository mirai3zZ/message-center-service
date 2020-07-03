package com.yangyang.cloud.common.paginate;

import org.apache.commons.lang.StringUtils;

import java.util.List;


/**
 * 分页实例
 *
 * @版本 V 1.0
 */
public class Page {
    /**
     * 当前记录起始索引
     */
    private int start = 0;

    /**
     * 每页显示记录数
     */
    private int length = 10;
    private Integer draw = 1;
    private int iTotalRecords; //总记录数

    /**
     * 总记录数
     */
    private int iTotalDisplayRecords;
    private boolean auth = true;

    /**
     * 存放查询的结果集
     */
    private List<?> data;
    //2019/04/17 备案主体列表中有使用判断添加主体按钮
    private boolean isOperationManager;


    public Page() {
        super();
    }

    public Page(Integer start, Integer length) {
        super();
        if (null == start) {
            start = 0;
        }
        this.start = start;
        this.length = length;
    }

    public int getStart() {
        return start;
    }

    public void setStart(Object start) {
        if (!StringUtils.isEmpty(String.valueOf(start))) {
            this.start = Integer.valueOf(String.valueOf(start));
        } else {
            this.start = 0;
        }
    }

    public int getLength() {
        return length;
    }

    public void setLength(Object length) {
        if (!StringUtils.isEmpty(String.valueOf(length))) {
            this.length = Integer.valueOf(String.valueOf(length));
        } else {
            this.length = 10;
        }
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public int getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(int iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public int getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean getIsOperationManager() {
        return isOperationManager;
    }

    public void setIsOperationManager(boolean operationManager) {
        isOperationManager = operationManager;
    }
}
