package com.yangyang.cloud.aop;


import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.ResponseCode;
import com.yangyang.cloud.keycloak.SecurityContextUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Aspect
@Component
public class adminControlAspect {
    @Pointcut("@annotation(com.yangyang.cloud.aop.adminControl)" )
    public void addAdvice(){}

	@Value("${noPrivilegeMessage:无权限操作}")
	private String noPrivilegeMessage;
    @Around("addAdvice()")
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
    	Set<String> roles = SecurityContextUtil.getLoginUser().getRoles();
    	if(!roles.isEmpty() && (roles.contains(ControlConstants.adminControl) || roles.contains(ControlConstants.tempControl))) {
    		try {
				return pjp.proceed();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
    	}
		return new ResponseBean<>(ResponseCode.RESPONSE_CODE_SUCCESS,noPrivilegeMessage, null);
    }

}
