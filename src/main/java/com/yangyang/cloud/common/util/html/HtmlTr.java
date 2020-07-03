package com.yangyang.cloud.common.util.html;

import java.util.ArrayList;
import java.util.List;

/**
 * HTML tr 标签的内容和属性
 *
 * @author daiyan
 */
public class HtmlTr extends HtmlTag {
    private List<HtmlTd> tdList;

    public List<HtmlTd> getTdList() {
        return tdList;
    }

    public void setTdList(List<HtmlTd> tdList) {
        this.tdList = tdList;
    }

    public void addTd(HtmlTd td) {
        if (null == tdList) {
            tdList = new ArrayList<>();
        }
        tdList.add(td);
    }

    public void addTd(String text) {
        if (null == tdList) {
            tdList = new ArrayList<>();
        }
        tdList.add(new HtmlTd(text));
    }

    public void addTh(String text) {
        if (null == tdList) {
            tdList = new ArrayList<>();
        }
        tdList.add(new HtmlTh(text));
    }

    public void addTh(HtmlTh th) {
        if (null == tdList) {
            tdList = new ArrayList<>();
        }
        tdList.add(th);
    }

    @Override
    public String getTagName() {
        return "tr";
    }
}
