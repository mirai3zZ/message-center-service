package com.yangyang.cloud.common.util.html;

import java.util.ArrayList;
import java.util.List;

/**
 * HTMl 表格内容和属性的对象
 *
 * @author daiyan
 */
public class HtmlTable extends HtmlTag {
    private List<HtmlTr> trList;

    public List<HtmlTr> getTrList() {
        return trList;
    }

    public void setTrList(List<HtmlTr> trList) {
        this.trList = trList;
    }

    public void addTr(HtmlTr tr) {
        if (null == trList) {
            trList = new ArrayList<>();
        }
        trList.add(tr);
    }

    @Override
    public String getTagName() {
        return "table";
    }
}
