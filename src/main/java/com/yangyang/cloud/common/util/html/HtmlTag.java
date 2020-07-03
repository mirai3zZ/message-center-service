package com.yangyang.cloud.common.util.html;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * HTMl 标签
 *
 * @author daiyan
 */
public abstract class HtmlTag {
    /**
     * 标签的 id 属性
     */
    private String id;

    /**
     * 标签的 class 属性
     */
    private Set<String> classNames;

    /**
     * 一些自定义的标签属性
     */
    private Map<String, String> attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getClassNames() {
        return classNames;
    }

    public void setClassNames(Set<String> classNames) {
        this.classNames = classNames;
    }

    public void addClassName(String className) {
        if (null == classNames) {
            classNames = new HashSet<>();
        }
        classNames.add(className);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(String key, String value) {
        if (null == attributes) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }

    /**
     * 根据不同标签返回各自的 tagName
     *
     * @return String
     */
    public abstract String getTagName();
}
