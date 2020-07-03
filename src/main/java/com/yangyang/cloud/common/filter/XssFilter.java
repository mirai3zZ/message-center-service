package com.yangyang.cloud.common.filter;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 功能描述: 跨域：由于浏览器的安全性限制，不允许AJAX访问 协议不同、域名不同、端口号不同的 数据接口;
 * 前后端都需要设置允许跨域
 *
 * @param:
 * @return:
 * @author: zhanghuazong
 * @date: 2019/1/29 17:31
 */
@WebFilter(urlPatterns = "/*")
public class XssFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(XssFilter.class);
    // /operation/manage/notices/* 修改公告 /operation/manage/notices/153070831402811392 修改
    // /operation/manage/notices 创建公告 /operation/manage/notices/153070831402811392
    // /operation/message 发送消息
    @Value("${whiteListURLs:/operation/message,/operation/manage/notices/**,/operation/manage/notices,/operation/manage/message,/operation/manage/message/types/*/templates,/operation/workorders/send/message}")
    private String whiteListURLstr;
    private final String[] NULL_STRING_ARRAY = new String[0];
    //逗号  空格 分号  换行
    private final String URL_SPLIT_PATTERN = "[, ;\r\n]";
    /**
     * 白名单
     */
    private String[] whiteListURLs = null;
    private final PathMatcher pathMatcher = new AntPathMatcher(System.getProperty("file.separator"));
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.initConfig();
    }


    /**
     * 将corsFilter类转移 CrosXssFilter,统一处理跨域和xss漏洞
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
    	request.setCharacterEncoding("utf-8");
    	response.setContentType("text/html;charset=utf-8");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String currentURL = httpRequest.getServletPath();
        logger.info("XssFilter.......get session ID {}",((HttpServletRequest) request).getSession().getId());
        if(isWhiteURL(currentURL)){
            HttpServletRequest httpServletRequest=(HttpServletRequest)request;
            logger.info("XssFilter.......orignal url:{},匹配白名单成功,ParameterMap:{}",httpServletRequest.getRequestURI(), JSONObject.fromObject(httpServletRequest.getParameterMap()));
            chain.doFilter(httpRequest, response);
            return;
        }else{
            //sql,xss过滤
            HttpServletRequest httpServletRequest=(HttpServletRequest)request;
            logger.info("XssFilter.......orignal url:{},ParameterMap:{}",httpServletRequest.getRequestURI(), JSONObject.fromObject(httpServletRequest.getParameterMap()));
            XssHttpServletRequestWrapper xssHttpServletRequestWrapper=new XssHttpServletRequestWrapper(
                    httpServletRequest);
            chain.doFilter(xssHttpServletRequestWrapper, response);
            logger.info("XssFilter..........doFilter url:{},ParameterMap:{}",xssHttpServletRequestWrapper.getRequestURI(), JSONObject.fromObject(xssHttpServletRequestWrapper.getParameterMap()));
        }
    }
    @Override
    public void destroy() {

    }
    private boolean isWhiteURL(String currentURL) {
        for (String whiteURL : whiteListURLs) {
            if (pathMatcher.match(whiteURL, currentURL)) {
                logger.debug("url filter : white url list matches : [{}] match [{}] continue", whiteURL, currentURL);
                return true;
            }
            logger.debug("url filter : white url list not matches : [{}] match [{}]", whiteURL, currentURL);
        }
        return false;
    }

    private void initConfig() {
        whiteListURLs = strToArray(whiteListURLstr);
    }
    private String[] strToArray(String urlStr) {
        if (urlStr == null) {
            return NULL_STRING_ARRAY;
        }
        String[] urlArray = urlStr.split(URL_SPLIT_PATTERN);

        List<String> urlList = new ArrayList<String>();

        for (String url : urlArray) {
            url = url.trim();
            if (url.length() == 0) {
                continue;
            }

            urlList.add(url);
        }

        return urlList.toArray(NULL_STRING_ARRAY);
    }




}
