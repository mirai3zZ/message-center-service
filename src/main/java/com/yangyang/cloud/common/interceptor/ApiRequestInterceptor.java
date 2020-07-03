package com.yangyang.cloud.common.interceptor;

import com.yangyang.cloud.common.RequestIdUtil;
import com.yangyang.cloud.common.RequestIdUtil;
import com.yangyang.cloud.common.bean.User;
import com.yangyang.cloud.keycloak.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yuanye
 * @date 2018/10/3
 * Desc: before controller check
 */
@Component
@Slf4j
public class ApiRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("check is there a requestId in header");
        String requestId = RequestIdUtil.getRequestId(request);
        if (!StringUtils.isEmpty(requestId)) {
            response.setHeader(RequestIdUtil.REQUEST_ID_KEY, requestId);
            // put request id into thread context ,in order to print in logs
            MDC.put(RequestIdUtil.REQUEST_ID_KEY, requestId);
            return true;
        }
        return false;
    }

}
