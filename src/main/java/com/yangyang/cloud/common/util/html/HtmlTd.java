package com.yangyang.cloud.common.util.html;

/**
 * HTML td 标签的内容和属性
 *
 * @author daiyan
 */
public class HtmlTd extends HtmlTag {
    /**
     * td 标签的 colspan 属性
     */
    private int colspan;

    /**
     * td 标签的 rowspan 属性
     */
    private int rowspan;

    /**
     * 文本内容
     */
    private String text;

    public HtmlTd() {
    }

    public HtmlTd(String text) {
        this.text = text;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getTagName() {
        return "td";
    }
}
