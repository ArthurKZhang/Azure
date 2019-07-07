package com.arthur.azure_core.aspects;

import android.util.Log;

import com.arthur.azure_core.annotations.PbClick;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class PbClickAspect {
    public final String pbClickRef = "com.arthur.azure_core.annotations.PbClick";

    @Pointcut("execution(@" + pbClickRef + " * *(..))")
    public void onClickMethod() {
    }

    @After("onClickMethod()")
    public void addPbAfterClick(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        PbClick pbClick = methodSignature.getMethod().getAnnotation(PbClick.class);
        String action = pbClick.action();
        String block = pbClick.block();
        String rseat = pbClick.rseat();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("action: ").append(action).append("; ")
                .append("block: ").append(block).append("; ")
                .append("rseat: ").append(rseat).append("; ");

        Log.i("Aspect*PbClick",stringBuilder.toString());
    }

}
