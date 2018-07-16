package com.arthur.azure_core.aspects;

import android.util.Log;

import com.arthur.azure_core.annotations.Logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyu on 13/07/2018.
 */

/**
 *
 */
@Aspect
public class LogAspect {
    /*
    @Logging
    String tag() default "azure-Logging";

    int type() default 2;
     */
    private static volatile boolean enabled = true;

    public final String annoRef = "com.arthur.azure_core.annotations.Logging";

    public static void setEnabled(boolean enabled) {
        LogAspect.enabled = enabled;
    }
//
//    @Pointcut("within(@" + annoRef + " *)")
//    public void withinAnnotatedClass() {
//    }
//
//    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
//    public void methodInsideAnnotatedType() {
//    }
//
//    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
//    public void constructorInsideAnnotatedType() {
//    }
//
//    @Pointcut("execution(@" + annoRef + " * *(..)) || methodInsideAnnotatedType()")
//    public void method() {
//    }
//
//    @Pointcut("execution(@" + annoRef + " *.new(..)) || constructorInsideAnnotatedType()")
//    public void constructor() {
//    }
//
//    @Pointcut("method() || constructor()")
//    public void methodAndConstructor() {
//    }
//
//    @Pointcut("get(@Logging * *)")
//    public void readField() {
//    }
//
//    @Pointcut("set(@Logging * *)")
//    public void writeField() {
//    }
//
//    @AfterReturning(pointcut = "readField()", returning = "value")
//    public void afterFieldAccess(Object value, JoinPoint joinPoint) {
//        if (!enabled) return;
//
//        Logging annotation = (Logging) ToolBox.getFieldAnno(joinPoint, Logging.class);
//        String tag = annotation.tag();
//        int type = annotation.type();
//
//        String fieldName = joinPoint.getSignature().getName();
//        String msg = joinPoint.toLongString() + "\n\t" + fieldName + "\n\t" + value;
//
//        ToolBox.print(type, tag, msg);
//    }
//
//    @AfterReturning(value = "writeField()", returning = "value")
//    public void catchFieldSet(JoinPoint joinPoint, Object value) {
//        if (!enabled) return;
//        Logging annotation = (Logging) ToolBox.getFieldAnno(joinPoint, Logging.class);
//        String tag = annotation.tag();
//        int type = annotation.type();
//
//        String fieldName = joinPoint.getSignature().getName();
//        String msg = joinPoint.toLongString() + "\n\t" + fieldName + "\n\t" + value;
//
//        ToolBox.print(type, tag, msg);
//    }
//

//    @Around("methodAndConstructor() && annotation(logging)")
    //@Around("execution(* com.arthur.azure.MainActivity.*(..) && annotation(logging)")
    @Around("execution(@com.arthur.azure_core.annotations.Logging * *(..))")
    public Object logAndExecute(ProceedingJoinPoint joinPoint)throws Throwable{//, final Logging logging) throws Throwable {
        Log.v("@@@@@@@@","@@@@@@@@@@@@@@@@@@@@@@@@@@");
        if (!enabled) return joinPoint.proceed();

//        String tag = logging.tag();
//        int type = logging.type();
//
//        ToolBox.enterMethod(joinPoint, tag, type);
//
//        long startNanos = System.nanoTime();
        Object result = joinPoint.proceed();
//        long stopNanos = System.nanoTime();
//        long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
//
//        ToolBox.exitMethod(joinPoint, result, lengthMillis, tag, type);
        Log.v("1111111","@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return result;
    }


}
