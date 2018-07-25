package com.arthur.azure_core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <code>int requestCode, String[] permissions</code>
 * <p>
 * if you pin this annotation on your 'business function', be aware that this function will not be called if
 * your required permissions are needed to be requested. That means only if you already have those permissions, your
 * annotated 'business function' will be executed.
 * For callbacks methods, see @PermissionSuccess and @PermissionFail.
 * I recommend you to use these annotations as the given example below:
 * <p>
 * <code>
 *
 * @PermissionRequest(bla.bla) function_business()
 * @PermissionSuccess function_onSuccess(){
 * //something you want to do;
 * function_business()
 * //something you want to do;
 * }
 * @PermissionFail function_onFail(){
 * //something you want to do;
 * }
 * <p>
 * </code>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface PermissionRequest {

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
