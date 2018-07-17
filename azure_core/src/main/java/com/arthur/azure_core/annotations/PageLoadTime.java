package com.arthur.azure_core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhangyu on 17/07/2018.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PageLoadTime {
    String tag() default "PageLoadTime";
    int type() default 2;
    String info() default "TIME CONSUMING";

}
