package com.yangyang.cloud.common.util.html;

/**
 * HTML th 标签的内容和属性
 *
 * @author daiyan
 */
public class HtmlTh extends HtmlTd {
    public HtmlTh(String text) {
        super(text);
    }

    @Override
    public String getTagName() {
        return "th";
    }
}
