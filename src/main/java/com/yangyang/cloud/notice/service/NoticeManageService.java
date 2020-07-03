package com.yangyang.cloud.notice.service;

import com.inspur.bss.commonsdk.utils.IdWorker;
import com.yangyang.cloud.common.CommonUtils;
import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.ResponseCode;
import com.yangyang.cloud.common.bean.User;
import com.yangyang.cloud.common.paginate.Page;
import com.yangyang.cloud.keycloak.SecurityContextUtil;
import com.yangyang.cloud.notice.bean.NoticeBean;
import com.yangyang.cloud.notice.bean.enums.NoticeImportLevelEnum;
import com.yangyang.cloud.notice.mapper.NoticeManageMapper;
import com.yangyang.cloud.notice.vo.AnnounceNoticeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuanye
 * @date 2018/10/22
 * Desc:notice manage service
 */
@Service
@Slf4j
public class NoticeManageService {
    @Autowired
    private NoticeManageMapper manageMapper;

    /**
     * @param noticeBean
     * @return
     */
    public ResponseBean addNotice(AnnounceNoticeVo noticeBean) {
        User user = SecurityContextUtil.getLoginUser();
        log.info("user {} add notice {}", user.getId(), noticeBean.getNoticeTitle());
        ResponseBean responseBean = new ResponseBean();
        // 设置主键
        noticeBean.setId(BigInteger.valueOf(IdWorker.getNextId()));
        noticeBean.setCreatedTime(new Date());
        noticeBean.setCreatedUserId(user.getId());
        noticeBean.setCreatedUserName(user.getName());
        noticeBean.setImportLevel(NoticeImportLevelEnum.LEVEL_NOMAL.getLevel());
        manageMapper.save(noticeBean);
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
        return responseBean;
    }

    /**
     *
     * @param page
     * @param startTime
     * @param endTime
     * @param noticeTypeId
     * @param title
     * @param status
     * @return
     */
    public List<NoticeBean> queryNoticeBeansForPage(Page page, String startTime, String endTime,String noticeTypeId,
                                                    String title, String status) {
        log.info("mesage start {},mesage length {},begin time {} and end time {},String noticeTypeId,search title {} status {}", page.getStart(),page.getLength(), startTime, endTime, noticeTypeId,title,status);
        Map queryMap = new HashMap();
        queryMap.put("page",page);
        queryMap.put("startTime",startTime);
        queryMap.put("endTime",endTime);
        queryMap.put("noticeTypeId",noticeTypeId);
        queryMap.put("title",title);
        queryMap.put("status",status);
        return manageMapper.getNoticeBeansForPage(queryMap);
    }

    /**
     * @param noticeBean
     * @return
     */
    public ResponseBean<Object> updateNotice(NoticeBean noticeBean) {
        User user = SecurityContextUtil.getLoginUser();
        ResponseBean<Object> responseBean = new ResponseBean<Object>();
        noticeBean.setUpdatedUserId(user.getId());
        noticeBean.setUpdatedUserName(user.getName());
        manageMapper.updateNotice(noticeBean);
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
        return responseBean;
    }
}
