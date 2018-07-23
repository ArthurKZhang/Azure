package com.arthur.azure_core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface PermissionAcquire {

    int requestCode();

    String[] permissions();//  需要的权限数组，默认""空

//
//    String rationalMessage(); //合理性解释，默认""空
//
//    int rationalMsgResId() default 0;
//
//    String deniedMessage() default ""; //权限禁止显示内容
//
//    int deniedMsgResId() default 0;
//
//    String grantBtnMsg() default "同意";
//
//    int grantBtnMsgId() default 0;
//
//    String denyBtnMsg() default "取消";
//
//    int denyBtnMsgId() default 0;
//
//    boolean needGotoSetting() default false;//是否去系统设置
//
//    boolean runIgnorePermission() default false;//是否无视权限继续方法
}
