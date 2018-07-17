package com.arthur.azure_core.aspects;

import android.support.v4.app.Fragment;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by zhangyu on 17/07/2018.
 */
@Aspect
public class PageLoadTimeAspect {
    public final String annoRef = "com.arthur.azure_core.annotations.PageLoadTime";

    private long actiStart, actiEnd, fragStart, fragEnd;

    @Pointcut("within(@" + annoRef + " *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(* android.app.Activity+.onCreate(..))")
    public void onCreateInSubClass() {
    }

    @Pointcut("execution(* (@" + annoRef + " *).onWindowFocusChanged(..))")
    public void onWindowFocusChangedInSubClass() {
    }

    @Pointcut("withinAnnotatedClass() && onCreateInSubClass()")
    public void activityOnCreate() {
    }

    @Pointcut("withinAnnotatedClass() && onWindowFocusChangedInSubClass()")
    public void activityOnWindFocusChanged() {
    }

    //Activity的所有的子类的所有方法 execution(* android.app.Activity+.onCreate(..))
    //"execution(* android.app.Activity+.onWindowFocusChanged(..))"
    @Before("activityOnCreate()")
    public void startActi(ProceedingJoinPoint joinPoint) {
        actiStart = System.nanoTime();
    }

    @After("activityOnWindFocusChanged()")
    public void endActi(ProceedingJoinPoint joinPoint) {
        actiEnd = System.nanoTime();
        Log.v("launch Activity Cost", actiEnd - actiStart + "");
    }


    @Pointcut("execution(* android.support.v4.app.Fragment+.onCreate(..))")
    public void onCreateInFragSub() {
    }

    @Pointcut("execution(* android.support.v4.app.Fragment+.onResume(..))")
    public void onResumeInFragSub() {
    }

    @Pointcut("withinAnnotatedClass() && onCreateInFragSub()")
    public void fragmentOnCreate() {
    }

    @Pointcut("withinAnnotatedClass() && onResumeInFragSub()")
    public void fragmentOnWindFocusChanged() {
    }

    @Before("fragmentOnCreate()")
    public void startFrag(ProceedingJoinPoint joinPoint) {
        actiStart = System.nanoTime();
    }

    @After("fragmentOnWindFocusChanged()")
    public void endFrag(ProceedingJoinPoint joinPoint) {
        actiEnd = System.nanoTime();
        Log.v("launch Fragment Cost", actiEnd - actiStart + "");
    }

}
