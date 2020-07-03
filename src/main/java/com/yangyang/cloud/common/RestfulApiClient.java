package com.yangyang.cloud.common;

import com.yangyang.cloud.common.bean.User;
import com.yangyang.cloud.keycloak.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;

/**
 * @author yuanye
 * @date 2018/10/12
 * Desc:access api
 */
@Component
@Slf4j
@Deprecated
public class RestfulApiClient {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HttpServletRequest request;

    public String accessApiJson(String url, HttpMethod method, Object object, Object[] uriVariables) {
        HttpHeaders headers = getHeader();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return accessApi(url, method, object, uriVariables, headers);
    }

    public String accessApiForm(String url, HttpMethod method, Object object, Object[] uriVariables) {
        HttpHeaders headers = getHeader();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return accessApi(url, method, object, uriVariables, headers);
    }

    private String accessApi(String url, HttpMethod method, Object object, Object[] uriVariables, HttpHeaders httpHeaders) {

        HttpEntity entity = new HttpEntity(object, httpHeaders);
        if (uriVariables == null) {
            uriVariables = new Object[]{};
        }
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, entity, String.class, uriVariables);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("response entity is {}", responseEntity.getBody());
                return responseEntity.getBody();
            }
        } catch (Exception e) {
            log.error("fail to access api {}", url, e);
            throw e;
        }
        return null;
    }

    private HttpHeaders getHeader() {
        HttpHeaders headers = SecurityContextUtil.getHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        headers.add(RequestIdUtil.REQUEST_ID_KEY, RequestIdUtil.requestIdThreadLocal.get());
        return headers;
    }
}
