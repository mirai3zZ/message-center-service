package com.yangyang.cloud.common;


import com.alibaba.fastjson.JSON;
import com.yangyang.cloud.common.exception.ApiException;
import com.yangyang.cloud.message.service.MessageService;
import com.yangyang.cloud.message.vo.MessageReleaseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * desc:
 *
 * @author chenxinyi
 * @date 2018/11/12 17:04
 */
@Component
@Slf4j
public class ApiManageUtil {
    @Autowired
    @Qualifier("defaultRestTemplate")
    DefaultRestTemplate defaultRestTemplate;
    @Autowired
    private MessageService messageService;
    /**
     * 发送短信或邮件
     *
     * @param templateId     用户id
     * @param mobile 验证码
     * @return ResponseBean
     */
    public ResponseBean sendMessage(String templateId, String mobile, String[] smsParams, String email,String[] emailParams) {
        ResponseBean responseBean = new ResponseBean();
        try {
            log.info("调用消息发送接口……");
            MessageReleaseVo messageReleaseVo = new MessageReleaseVo();
            messageReleaseVo.setTemplateId( templateId);
            if (!StringUtils.isEmpty(mobile)) {
                if(CommonUtil.checkMobile(mobile)){
                    messageReleaseVo.setMobile(mobile);
                    messageReleaseVo.setSmsParams( smsParams);
                }else{
                    responseBean.setCode("000.000000400");
                    responseBean.setMessage("请输入正确的手机号");
                    return responseBean;
                }
            }
            if (!StringUtils.isEmpty(email)) {
                if(CommonUtil.checkEmail(email)){
                    messageReleaseVo.setEmail(email);
                    messageReleaseVo.setEmailParams(emailParams);
                }else{
                    responseBean.setCode("000.000000400");
                    responseBean.setMessage("请输入正确的邮箱");
                    return responseBean;
                }
            }
            ResponseBean result = messageService.messageRelease(messageReleaseVo);
            if (!"0".equals(result.getCode())) {
                log.error("消息发送失败,mobile:{},email:{},time：{}", mobile, email,new Date());
                throw new ApiException("004", "消息发送失败");
            }
        } catch (ApiException e) {
            log.error("消息接口调用失败，mobile:{},email:{},time：{}", mobile, email,new Date());
            throw new ApiException("005", "消息接口调用失败");
        }
        log.info("send message [{}] to [{}] success",
                ((!StringUtils.isEmpty(mobile)) && (!StringUtils.isEmpty(email)) ? Arrays.toString(smsParams) + "and" + Arrays.toString(emailParams) : (!StringUtils.isEmpty(mobile) ? Arrays.toString(smsParams) : Arrays.toString(emailParams))),
                (!StringUtils.isEmpty(mobile)) && (!StringUtils.isEmpty(email)) ? mobile + "and" + email : (!StringUtils.isEmpty(mobile) ? mobile : email));
        responseBean.setCode("0");
        responseBean.setMessage("消息发送成功");
        return responseBean;
    }

}
