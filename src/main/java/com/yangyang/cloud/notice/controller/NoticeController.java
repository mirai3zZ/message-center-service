package com.yangyang.cloud.notice.controller;

import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.ResponseCode;
import com.yangyang.cloud.common.paginate.Page;
import com.yangyang.cloud.message.common.MessageConstants;
import com.yangyang.cloud.notice.bean.NoticeBean;
import com.yangyang.cloud.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: mengfanlong
 * E-mail:  meng.fanlong@inspur.com
 * Date:    2018/9/30 14:55
 * -------------------------------
 * Desc:    公告 运营和用户通用的控制器
 */
@RestController
@RequestMapping("/operation/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    /**
     * for font using
     *
     * @param page
     * @return Page
     */
    @GetMapping
    public ResponseBean<List<NoticeBean>> listPage(@ModelAttribute Page page, @RequestParam("type") String type,@RequestParam(value = "exclusiveTypes",required = false)String exclusiveTypes) {
        if (page.getLength() > MessageConstants.MESSAGE_MAX_LIST) {
            page.setLength(MessageConstants.MESSAGE_MAX_LIST);
        }
        return noticeService.queryNoticeBeansForPage(page, type,exclusiveTypes);
    }
    /**
     * get detail of notice
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean detail(@PathVariable("id") String id) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setResult(noticeService.getNotice(id));
        responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        return responseBean;
    }

    /**
     *description:分类获取公告接口
     *@author: LiuYang01
     *@date: 2019/9/13 10:36
     *@Param: noticeTypeId
     *@return page
     */
    @GetMapping("/groups")
    public ResponseBean getNoticeByTypeId(@ModelAttribute Page page,@RequestParam("noticeTypeId") String noticeTypeId){
        return noticeService.getNoticeByTypeIdForPage(page,noticeTypeId);
    }
}
