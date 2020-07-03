package com.yangyang.cloud.notice.service;

import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.ResponseCode;
import com.yangyang.cloud.common.bean.User;
import com.yangyang.cloud.common.paginate.Page;
import com.yangyang.cloud.keycloak.SecurityContextUtil;
import com.yangyang.cloud.notice.bean.NoticeBean;
import com.yangyang.cloud.notice.common.NoticeConstants;
import com.yangyang.cloud.notice.dto.NoticeTypeDTO;
import com.yangyang.cloud.notice.mapper.NoticeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: mengfanlong
 * E-mail:  meng.fanlong@inspur.com
 * Date:    2018/9/30 16:47
 * -------------------------------
 * Desc:    公告业务处理
 */
@Service
@Slf4j
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    public NoticeBean getNotice(String id) {
        User user = SecurityContextUtil.getLoginUser();
        log.info("user id [{}] name [{}] get notice {} detail", user.getId(), user.getName(), id);
        return noticeMapper.getNoticeBeanById(id);
    }

    /**
     * @param page
     * @param type
     * @return
     */
    public ResponseBean<List<NoticeBean>> queryNoticeBeansForPage(Page page, String type,String exclusiveTypes) {
        ResponseBean responseBean = new ResponseBean();
        User user = SecurityContextUtil.getLoginUser();
        log.info("user id [{}] name [{}] get notice list", user.getId(), user.getName());
        Map queryMap = new HashMap();
        queryMap.put("page", page);
        queryMap.put("type", type);
        queryMap.put("exclusiveTypes",StringUtils.isEmpty(exclusiveTypes)?exclusiveTypes:exclusiveTypes.split(","));
        List<NoticeBean> noticeBeans =noticeMapper.getNoticeBeansForPage(queryMap);
        page.setData(noticeBeans);
        responseBean.setResult(page);
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
        return responseBean;
    }


    /**
     *@param noticeTypeId
     *@return
     *
     */
    public ResponseBean getNoticeByTypeIdForPage(Page page,String noticeTypeId){
        ResponseBean responseBean = new ResponseBean();
        User user = SecurityContextUtil.getLoginUser();
        log.info("user id [{}] name [{}] get notices by typeId", user.getId(), user.getName());
        Map queryMap = new HashMap();
        queryMap.put("type",noticeTypeId);
        queryMap.put("page", page);
        List<NoticeBean> noticeBeans = noticeMapper.getNoticeBeansForPage(queryMap);
        if(noticeBeans.size()==0){
            responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
            responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
            responseBean.setResult(new ArrayList<>());
        }else{
            page.setData(noticeBeans);
            NoticeTypeDTO noticeTypeDTO = new NoticeTypeDTO();
            noticeTypeDTO.setNoticeTypeId(noticeTypeId);
            if(("all").equals(noticeTypeId)){
                noticeTypeDTO.setNoticeTypeName("全部");
            }else{
                noticeTypeDTO.setNoticeTypeName(noticeBeans.get(0).getNoticeTypeName());
            }
            noticeTypeDTO.setNotices(page);
            responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
            responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
            responseBean.setResult(noticeTypeDTO);
        }
        return  responseBean;
    }
}
