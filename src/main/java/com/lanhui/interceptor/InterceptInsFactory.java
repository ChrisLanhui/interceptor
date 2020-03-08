package com.lanhui.interceptor;

import com.lanhui.interceptor.annotation.Intercepts;
import com.lanhui.interceptor.annotation.Signature;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InterceptInsFactory implements InvocationHandler {

    private Object target = null;

    private Interceptor interceptor = null;

    private Map<Class<?>, Set<Method>> signatureMap = null;

    private InterceptInsFactory(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
        this.target = target;
        this.interceptor = interceptor;
        this.signatureMap = signatureMap;
    }

    public static Object getProxyInstance(Object target, Interceptor interceptor) {
        Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
        Class<?> type = target.getClass();
        Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
        return interfaces.length > 0 ? Proxy.newProxyInstance(type.getClassLoader(),
                interfaces, new InterceptInsFactory(target, interceptor, signatureMap)) : target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Set<Method> methods = this.signatureMap.get(method.getDeclaringClass());
        return methods != null && methods.contains(method) ?
                this.interceptor.intercept(new Invocation(this.target, method, args))
                : method.invoke(this.target, args);
    }

    private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor Interceptor) {
        Intercepts annotation = Interceptor.getClass().getAnnotation(Intercepts.class);
        if (annotation == null) {
            throw new NullPointerException("the annotation of interceptor is null");
        }
        Map<Class<?>, Set<Method>> map = new HashMap<>();
        Signature[] sigs = annotation.value();
        for (Signature sig : sigs) {
            Set<Method> set = map.computeIfAbsent(sig.type(), k -> new HashSet<>());
            try {
                Method method = sig.type().getMethod(sig.method(), sig.args());
                set.add(method);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("can not find method " + sig.method() + " in Class "
                        + sig.type().getCanonicalName());
            }
        }
        return map;
    }

    private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
        Set<Class<?>> interfaces = new HashSet<>();
        for(; type != null; type = type.getSuperclass()) {
            Class<?>[] tmp = type.getInterfaces();
            for (Class<?> c : tmp) {
                if (signatureMap.containsKey(c)) {
                    interfaces.add(c);
                }
            }
        }
        return interfaces.toArray(new Class<?>[0]);
    }
}
