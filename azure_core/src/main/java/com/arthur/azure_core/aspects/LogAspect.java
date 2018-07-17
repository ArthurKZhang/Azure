package com.arthur.azure_core.aspects;


import com.arthur.azure_core.annotations.Logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.FieldSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Array;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyu on 13/07/2018.
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

    @Pointcut("within(@" + annoRef + " *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {
    }

    @Pointcut("execution(@" + annoRef + " * *(..))")
    public void method() {
    }

    @Pointcut("execution(@" + annoRef + " *.new(..))")
    public void constructor() {
    }

    @Pointcut("method() || constructor()")
    public void methodAndConstructor() {
    }

    //Method & Constructor manufacturing

    //Advice for annotated Method or Constructor.
    @Around("method() || constructor()")
    public Object AnnotedMethodAndConstru(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!enabled) return joinPoint.proceed();


        ToolBox.enterMethod2(joinPoint);

        long startNanos = System.nanoTime();
        Object result = joinPoint.proceed();
        long stopNanos = System.nanoTime();
        long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);

        ToolBox.exitMethod2(joinPoint, result, lengthMillis);
        return result;
    }

    //Advice for annotated Class. All methods or constructors will be wrapped. Priority lower than Field and Method or Constructor Advices.
    @Around("(methodInsideAnnotatedType()||constructorInsideAnnotatedType())&&(!methodAndConstructor())")
    public Object InsideAnnoedTypeMethodAndConstru(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!enabled) return joinPoint.proceed();


        ToolBox.enterMethod(joinPoint);

        long startNanos = System.nanoTime();
        Object result = joinPoint.proceed();
        long stopNanos = System.nanoTime();
        long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);

        ToolBox.exitMethod(joinPoint, result, lengthMillis);
        return result;
    }

    //Field manufacturing

    @Pointcut("get(@com.arthur.azure_core.annotations.Logging * *)")
    public void readField() {
    }

    @Pointcut("set(@com.arthur.azure_core.annotations.Logging * *)")
    public void writeField() {
    }

    //获取成员变量值的时候
    @AfterReturning(pointcut = "readField()", returning = "value")
    public void afterFieldAccess(Object value, JoinPoint joinPoint) {
        if (!enabled) return;

        Logging annotation = (Logging) ToolBox.getFieldAnno(joinPoint, Logging.class);
        String tag = annotation.tag();
        int type = annotation.type();

        FieldSignature fs = (FieldSignature) joinPoint.getSignature();
        String fieldType = fs.getField().getType().getName();
        String fieldName = joinPoint.getSignature().getName();
        String msg = joinPoint.toLongString() + "\n\t" + fieldName + "\n\t" + value + fieldType;

        StringBuilder sb = new StringBuilder("\u21E2 ");
        sb.append("Field: ").append(joinPoint.toLongString());
        sb.append("; ").append("Values: ");


        sb.append("[");
//TODO 对数组类型的成员变量进行处理
        sb.append(value);
        sb.append("]");

        ToolBox.print(type, tag, sb.toString());
    }

    //给成员变量赋值的时候
    @Around(value = "writeField()")//, returning = "value")
    public void catchFieldSet(ProceedingJoinPoint joinPoint) throws Throwable {//, Object value) {
        if (!enabled) return;
        FieldSignature fs = (FieldSignature) joinPoint.getSignature();
        String fieldType = fs.getField().getType().getName();

        Logging annotation = (Logging) ToolBox.getFieldAnno(joinPoint, Logging.class);
        String tag = annotation.tag();
        int type = annotation.type();

        Object[] values = joinPoint.getArgs();
        String fieldName = joinPoint.getSignature().getName();

        StringBuilder sb = new StringBuilder("\u21E2 ");
        sb.append("Field: ").append(joinPoint.toLongString());
        sb.append("; ").append("Values: ");
        sb.append("[");
        for (int i = 0; i < values.length; i++) {
            //TODO 对数组类型的成员变量进行处理
            sb.append(values[i].toString());
            if (i != values.length - 1)
                sb.append(",");
        }
        sb.append("]");

        ToolBox.print(type, tag, sb.toString());
        joinPoint.proceed(joinPoint.getArgs());
    }
}
