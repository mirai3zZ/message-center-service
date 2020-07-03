package com.yangyang.cloud.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author yuanye
 */
@Slf4j
public class RequestIdUtil {

    public static final String REQUEST_ID_KEY = "requestId";
    public static ThreadLocal<String> requestIdThreadLocal = new ThreadLocal<String>();

    /**
     * get request id
     *
     * @param request
     * @return
     */
    public static String getRequestId(HttpServletRequest request) {
        String requestId = null;
        String headerRequestId = request.getHeader(REQUEST_ID_KEY);
        if (StringUtils.isEmpty(headerRequestId)) {
            requestId = UUID.randomUUID().toString();
            log.info("request header has no requestId,we generate a new request id:{}", requestId);
        } else {
            requestId = headerRequestId;
            log.info("request header has requestId:{}", requestId);
        }
        requestIdThreadLocal.set(requestId);
        log.info("requestId:{}", requestId);
        return requestId;
    }

}
