package com.guesslive.admin.common.interceptor;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;

public class AfterAOPInterceptor implements AfterReturningAdvice {
	public void afterReturning(Object value, Method method, Object[] args, Object instance) throws Throwable {
		System.out.println("after()" + method.getName());
	}
}