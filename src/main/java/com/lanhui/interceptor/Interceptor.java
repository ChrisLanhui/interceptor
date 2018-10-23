package com.lanhui.interceptor;


public interface Interceptor {
    Object intercept(Invocation invocation) throws Throwable;

    Object getProxyInstance(Object target);
}
