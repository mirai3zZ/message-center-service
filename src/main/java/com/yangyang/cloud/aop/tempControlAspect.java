package com.yangyang.cloud.aop;


import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.keycloak.SecurityContextUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Set;

@Aspect
@Component
public class tempControlAspect {
    @Pointcut("@annotation(com.yangyang.cloud.aop.tempControl)" )
    public void addAdvice(){}  
    
    @Around("addAdvice()")
    public Object Interceptor(ProceedingJoinPoint pjp){
    	Set<String> roles = SecurityContextUtil.getLoginUser().getRoles();
    	if(!roles.isEmpty() && roles.contains(ControlConstants.tempControl)) {
    		try {
				return pjp.proceed();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return new ResponseBean<>();
    }

}
