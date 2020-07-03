package com.yangyang.cloud.common.util.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Map;

/**
 * 处理 HTML 格式的文档
 *
 * @author daiyan
 */
public class HtmlUtil {
	private static final char TAG_LEFT = '<';
	private static final char TAG_RIGHT = '>';
	
    /**
     * 根据 HTML table 生成 HTML 格式的字符串
     *
     * @return String
     */
    public static String toHTML(List<HtmlTable> tables, String html) {
        if (null == tables || isEmpty(tables)) {
            return html;
        }
        // 初始化 html document
        for (int i = 0; i < tables.size(); i++) {
            Document document = Jsoup.parse(html);
            Element tableElement = document.getElementsByTag("table").get(i);
            // 添加行
            for (HtmlTr tr : tables.get(i).getTrList()) {
                if (null == tr || isEmpty(tr.getTdList())) {
                    continue;
                }
                Element trElement = createElementByTag(tr, tableElement);
                for (HtmlTd td : tr.getTdList()) {
                    if (null == td) {
                        continue;
                    }
                    Element tdElement = createElementByTag(td, trElement);
                    if (null != td.getText()) {
                        tdElement.appendText(td.getText());
                    }
                }
            }
            html = document.toString();
        }
        return html;
    }

    /**
     * 把一个标签和它的属性添加到 Jsoup 的 Element 中
     *
     * @param tag           HtmlTag
     * @param parentElement Element 如果不为空，直接将新元素添加到 parentElement
     * @return Element 新生成的 element
     */
    private static Element createElementByTag(HtmlTag tag, Element parentElement) {
        Element element = new Element(tag.getTagName());
        // 将标签的属性添加到 element
        if (null != tag.getClassNames()) {
            for (String className : tag.getClassNames()) {
                if (null != className) {
                    element.addClass(className);
                }
            }
        }
        if (null != tag.getId()) {
            element.attr("id", tag.getId());
        }
        if (null != tag.getAttributes()) {
            for (Map.Entry<String, String> attribute : tag.getAttributes().entrySet()) {
                if (null != attribute.getKey() && null != attribute.getValue()) {
                    element.attr(attribute.getKey(), attribute.getValue());
                }
            }
        }
        if (tag instanceof HtmlTd) {
            addTdAttributes((HtmlTd) tag, element);
        }

        // 如果参数 parentElement 不为空，自动将新的 element 添加到 parentElement
        if (null != parentElement) {
            parentElement.appendChild(element);
        }
        return element;
    }

    /**
     * 向 targetElement 中添加属于 td 的属性
     *
     * @param td            HtmlTd
     * @param targetElement Element
     */
    private static void addTdAttributes(HtmlTd td, Element targetElement) {
        if (td.getColspan() > 0) {
            targetElement.attr("colspan", String.valueOf(td.getColspan()));
        }
        if (td.getRowspan() > 0) {
            targetElement.attr("rowspan", String.valueOf(td.getRowspan()));
        }
    }

    /**
     * 验证集合是否完全为空
     * @param list List<T>
     * @return boolean
     */
    private static <T> boolean isEmpty(List<T> list) {
        if (null != list) {
            for (T t : list) {
                if (null != t) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * html 压缩
     * @param html String
     * @return String
     */
    public static String htmlCompression(String html) {
    	StringBuffer stringBuffer = new StringBuffer();
    	for (int i = html.indexOf(TAG_LEFT); i <= html.lastIndexOf(TAG_RIGHT); i++) {
    		if (charIsUseful(html, i)) {
    			stringBuffer.append(html.charAt(i));
    		}
    	}
        return stringBuffer.toString();
    }
    
    /**
     * 判断一个字符在 html 中是否需要保留
     * @param html String
     * @param index int
     * @return boolean
     */
    private static boolean charIsUseful(String html, int index) {
    	if (' ' == html.charAt(index) || '\n' == html.charAt(index)) {
    		// 从 index 向左找，如果除了空以外最左侧是标签，不保留
    		for (int i = index - 1; i > 0; i--) {
    			if (isTag(html.charAt(i))) {
    				return false;
    			}
    		}
    		
    		// 向右找
    		for (int i = index + 1; i < html.length(); i++) {
    			if (isTag(html.charAt(i))) {
    				return false;
    			}
    		}
    	}
		return true;
    }
    
    /**
     * 判断字符是否是标签
     * @param c char
     * @return boolean
     */
    private static boolean isTag(char c) {
    	return TAG_LEFT == c || TAG_RIGHT == c;
    }
}
