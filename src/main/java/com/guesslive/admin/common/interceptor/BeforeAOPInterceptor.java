package com.guesslive.admin.common.interceptor;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

public class BeforeAOPInterceptor implements MethodBeforeAdvice{
  public void before(Method method, Object[] args, Object instance)
    throws Throwable {
   System.out.println("---------AOP LOG-----------before()"+method.getName());
  }
 }