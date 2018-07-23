package com.arthur.azure_core.aspects;

import android.util.Log;

import com.arthur.azure_core.annotations.PbElement;
import com.arthur.azure_core.annotations.PingBack;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.HashMap;
import java.util.Map;

@Aspect
public class PingBackAspect {
    private static final String PingbackAnnoRef = "com.arthur.azure_core.annotations.PingBack";

    @Pointcut("execution(@" + PingbackAnnoRef + " * *(..))")
    public void annotatedMethod() {
    }

    @After("annotatedMethod()")
    public void AfterMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        PingBack pbClick = methodSignature.getMethod().getAnnotation(PingBack.class);
        PbElement[] elements = pbClick.pairs();

        if (elements.length < 1) return;

        Map<String, String> map = new HashMap<String, String>();
        for (PbElement e : elements) {
            map.put(e.keyName(), e.contentValue());
        }

        boolean result = ToolBox.pingBack(map);
        if (!result)
            Log.e("PingBack Issue", "PingBack failed after Method: {" + methodName + "()}");
    }
}
