package com.arthur.azure_core.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * an array of @PbElement. It has a PbElement array: "PbElement[] pairs() default {}"
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PingBack {
    PbElement[] pairs() default {};
}
