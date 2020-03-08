package com.lanhui.simple;

import com.lanhui.interceptor.InterceptInsFactory;
import com.lanhui.interceptor.Interceptor;
import com.lanhui.interceptor.Invocation;
import com.lanhui.interceptor.annotation.Intercepts;
import com.lanhui.interceptor.annotation.Signature;

@Intercepts({@Signature(type = ISample.class, method = "doBusiness", args = {String.class})})
public class SampleInterceptor implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("intercept the method of ISample");
        return invocation.proceed();
    }

    public Object getProxyInstance(Object target) {
        return InterceptInsFactory.getProxyInstance(target, this);
    }
}
