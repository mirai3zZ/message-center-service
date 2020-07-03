package com.yangyang.cloud.notice.mapper;

import com.yangyang.cloud.notice.bean.NoticeBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: mengfanlong
 * E-mail:  meng.fanlong@inspur.com
 * Date:    2018/9/30 16:47
 * -------------------------------
 * Desc:    公告数据库访问
 */
@Mapper
public interface NoticeMapper {
    NoticeBean getNoticeBeanById(@Param(("id")) String id);
    List<NoticeBean> getNoticeBeansForPage(Map queryMap);
}
