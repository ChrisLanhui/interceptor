package com.lanhui.simple;

import com.lanhui.interceptor.InterceptInsFactory;

public class SampleClient {
    public static void main(String[] args) {
        SampleImpl sample = new SampleImpl();
        sample.doBusiness("doSomething");
        System.out.println("===================");
        SampleInteceptor inteceptor = new SampleInteceptor();
        ISample proxy = (ISample) inteceptor.getProxyInstance(new SampleImpl());
        proxy.doBusiness("doSomething");
    }
}
