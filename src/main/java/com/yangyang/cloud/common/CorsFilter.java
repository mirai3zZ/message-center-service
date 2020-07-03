package com.yangyang.cloud.common;

import lombok.extern.slf4j.Slf4j;

/**
 * desc:
 *
 * @param
 * @author chengym
 * @return
 * @date 2018/12/12 16:23
 */

@Slf4j
public class CorsFilter {
    /**
     * 未起作用,在修改OperationServiceApplication将会造成该过滤器生效,为避免对现有功能影响,先注释掉 20190122
     */
   /* @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        log.info("++++++++++++++Filter+++++++++");
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials","true");
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }
*/
}
