package com.yangyang.cloud.common.controller;

import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.ResponseCode;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author : zhanghuazong
 * @date : 2019/4/10 18:25
 */
public class TestHealthController {
    /**
     * 测试接口
     *
     * @return
     */
    @GetMapping("/operation/health")
    public ResponseBean testHealth() {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
        responseBean.setResult("SUCCESS");
        return responseBean;
    }
}
