package com.arthur.azure_core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * an element of @PingBack annotation.
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface PbElement {
    String keyName();

    String contentValue();
}
