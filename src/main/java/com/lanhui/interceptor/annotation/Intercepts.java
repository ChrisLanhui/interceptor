package com.lanhui.interceptor.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Intercepts {
    Signature[] value();
}
