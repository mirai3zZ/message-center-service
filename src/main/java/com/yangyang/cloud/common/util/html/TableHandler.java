package com.yangyang.cloud.common.util.html;

import com.yangyang.cloud.common.bean.TableContainerBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanye
 * @date 2018/12/22
 * Desc:html 表格处理
 */
@Component
@Slf4j
public class TableHandler {

    /**
     * 参数解析
     *
     * @param tableContainerBeans
     */
    public String tableParse(List<TableContainerBean> tableContainerBeans, String html) {
        log.info("start to parse tables");
        if (tableContainerBeans != null && !tableContainerBeans.isEmpty()) {
            List<HtmlTable> tables = new ArrayList<>();
            for (TableContainerBean tableContainerBean : tableContainerBeans) {
                HtmlTable htmlTable = new HtmlTable();
                String tableContent = tableContainerBean.getContent();
                String tableSplitChar = tableContainerBean.getSplitChar();
                String[] tds = tableContent.split(tableSplitChar);
                int row = tableContainerBean.getRow();
                int column = tableContainerBean.getColumn();
                log.info("table rows {} columns {}  split char is {} and content length is {}", row, column, tableSplitChar, tds.length);
                List<HtmlTr> htmlTrs = new ArrayList<>();
                for (int j = 0; j < row; j++) {
                    HtmlTr tr = new HtmlTr();
                    for (int i = j * column; i < column * (j + 1); i++) {
                        HtmlTd htmlTd = new HtmlTd();
                        htmlTd.setText(tds[i]);
                        tr.addTd(htmlTd);
                    }
                    htmlTrs.add(tr);
                    htmlTable.setTrList(htmlTrs);
                }
                tables.add(htmlTable);
            }
            return HtmlUtil.toHTML(tables, html);
        }
        return html;
    }
}
