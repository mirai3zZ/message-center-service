package com.yangyang.cloud.notice.mapper;

import com.yangyang.cloud.notice.bean.NoticeBean;
import com.yangyang.cloud.notice.vo.AnnounceNoticeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author yuanye
 * @date 2018/10/22
 * Desc:notice data access
 */
@Mapper
public interface NoticeManageMapper {
    /**
     * int save(NoticeBean noticeBean);
     */
    int save(AnnounceNoticeVo noticeBean);

    List<NoticeBean> getNoticeBeansForPage(Map queryMap);

    int updateNotice(NoticeBean noticeBean);
}
