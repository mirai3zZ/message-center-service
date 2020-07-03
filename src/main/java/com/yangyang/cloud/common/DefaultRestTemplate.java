package com.yangyang.cloud.common;

import com.yangyang.cloud.keycloak.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


/**
 * Created by zhangygao on 2018/10/22.
 * wrapper the token of key cloak for spring resttemplate, and wrapper the requestId also.
 */
@Component
@Slf4j
public class DefaultRestTemplate<T> {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HttpServletRequest request;


    public ResponseEntity<T> exchange(String url, HttpMethod method,
                                      Object params, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = generateHeaders();
        return exchange(url, method, httpHeaders, params, responseType, uriVariables);
    }

    public ResponseEntity<T> exchange(String url, HttpMethod method, HttpHeaders httpHeaders,
                                      Object params, Class<T> responseType, Object... uriVariables) {
        HttpEntity entity = new HttpEntity(params, httpHeaders);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, entity, responseType, uriVariables);
        log.info("requestId {} response entity is {}",httpHeaders.get(RequestIdUtil.REQUEST_ID_KEY), responseEntity.getBody());
        return responseEntity;
    }

    public HttpHeaders generateHeaders() {
        HttpHeaders httpHeaders = SecurityContextUtil.getHeaders();
        httpHeaders.add(RequestIdUtil.REQUEST_ID_KEY, RequestIdUtil.requestIdThreadLocal.get());
        return httpHeaders;
    }

}
