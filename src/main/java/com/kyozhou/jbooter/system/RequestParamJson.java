package com.kyozhou.jbooter.system;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface RequestParamJson {
    String value();
}
