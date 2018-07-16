package com.arthur.azure_core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhangyu on 13/07/2018.
 * String - tag, int - type
 * Log type. Default 2 = VERBOSE
 * VERBOSE = 2; DEBUG = 3; INFO = 4; WARN = 5; ERROR = 6
 *
 * @return
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {
    String tag() default "azure-Logging";


    int type() default 2;
}
