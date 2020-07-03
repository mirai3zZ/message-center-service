package com.yangyang.cloud.notice.controller;

import com.yangyang.cloud.aop.adminControl;
import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.paginate.Page;
import com.yangyang.cloud.notice.bean.NoticeBean;
import com.yangyang.cloud.notice.service.NoticeManageService;
import com.yangyang.cloud.notice.vo.AnnounceNoticeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;

import static com.yangyang.cloud.common.ResponseCode.RESPONSE_CODE_SUCCESS;
import static com.yangyang.cloud.common.ResponseCode.RESPONSE_SUCCESS_MESSAGE;

/**
 * @author yuanye
 * @date 2018/10/22
 * Desc:notice manage
 */
@RestController
@Validated
@RequestMapping("/operation/manage/notices")
public class NoticeManageController {
    @Autowired
    private NoticeManageService noticeManageService;

    @PostMapping
    @adminControl
    public ResponseBean release(@RequestBody @Valid AnnounceNoticeVo announceNoticeVo) {
        return noticeManageService.addNotice(announceNoticeVo);
    }

    /**
     * for font using
     *
     * @param page
     * @return Page
     */
    @GetMapping
    public ResponseBean list(@ModelAttribute Page page,
                             @RequestParam(value = "startTime") String startTime,
                             @RequestParam(value = "endTime") String endTime,
                             @RequestParam(value = "noticeTypeId") String noticeTypeId,
                             @RequestParam(value = "title", required = false) String title,
                             @RequestParam(value = "status") String status
    ) {
        ResponseBean responseBean = new ResponseBean();
        page.setData(noticeManageService.queryNoticeBeansForPage(page, startTime, endTime, noticeTypeId, title, status));
        responseBean.setCode(RESPONSE_CODE_SUCCESS);
        responseBean.setResult(page);
        responseBean.setMessage(RESPONSE_SUCCESS_MESSAGE);
        return responseBean;
    }

    /**
     * notice update
     *
     * @param noticeBean
     * @return
     */
    @PutMapping("/{id}")
    public ResponseBean<Object> updateNotice(@RequestBody @Validated NoticeBean noticeBean,
                                             @PathVariable(value = "id", required = true) BigInteger id) {
        noticeBean.setId(id);
        return noticeManageService.updateNotice(noticeBean);
    }
}
