package com.lanhui.simple;

public class SampleClient {
    public static void main(String[] args) {
        SampleImpl sample = new SampleImpl();
        sample.doBusiness("doSomething");
        System.out.println("===================");
        SampleInterceptor inteceptor = new SampleInterceptor();
        ISample proxy = (ISample) inteceptor.getProxyInstance(new SampleImpl());
        proxy.doBusiness("doSomething");
    }
}
