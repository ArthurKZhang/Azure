package com.arthur.azure_core.aspects;

import android.app.Application;
import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;

import com.arthur.azure_core.annotations.Logging;
import com.arthur.azure_core.utils.Strings;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.FieldSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by zhangyu on 13/07/2018.
 */
public class ToolBox {
    /**
     * print Log using Log API
     *
     * @param type VERBOSE = 2; DEBUG = 3; INFO = 4; WARN = 5; ERROR = 6
     * @param tag
     * @param msg
     */
    protected static void print(int type, String tag, String msg) {
        switch (type) {
            case 2:
                Log.v(tag, msg);
                break;
            case 3:
                Log.d(tag, msg);
                break;
            case 4:
                Log.i(tag, msg);
                break;
            case 5:
                Log.w(tag, msg);
                break;
            case 6:
                Log.e(tag, msg);
                break;
        }
    }

    public static int targetSDKVersion;
    public static int buildSDKVersion;

    public static Application application;

    /**
     * writing each step in a line, makes it easier to debug
     *
     * @param joinPoint
     * @param cls
     * @return
     */
    protected static Annotation getFieldAnno(JoinPoint joinPoint, Class cls) {
        FieldSignature fieldSignature = (FieldSignature) joinPoint.getSignature();
        Field field = fieldSignature.getField();
        Annotation annotation = field.getAnnotation(cls);
        return annotation;
    }

    protected static String asTag(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            return asTag(cls.getEnclosingClass());
        }
        return cls.getSimpleName();
    }

    protected static void enterMethod2(JoinPoint joinPoint) {

        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        Class<?> cls = signature.getDeclaringType();
        Logging annotation;
        if (signature instanceof MethodSignature) {
            annotation = ((MethodSignature) signature).getMethod().getAnnotation(Logging.class);
        } else if (signature instanceof ConstructorSignature) {
            annotation = (Logging) ((ConstructorSignature) signature).getConstructor().getAnnotation(Logging.class);
        } else {
            Log.e("azure", "cannot get annotation");
            return;
        }

//        Logging annotation = method.getAnnotation(Logging.class);


        String tag = annotation.tag();
        int type = annotation.type();

        String methodName = signature.getName();
        String[] parameterNames = signature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        StringBuilder builder = new StringBuilder("\u21E2 ");
        builder.append(methodName).append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parameterNames[i]).append('=');
            builder.append(Strings.toString(parameterValues[i]));
        }
        builder.append(')');

        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }

        if (tag.equals("azure-Logging")) tag = "[azure-Logging]" + ToolBox.asTag(cls);

        ToolBox.print(type, tag, builder.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final String section = builder.toString().substring(2);
            Trace.beginSection(section);
        }
    }

    protected static void exitMethod2(JoinPoint joinPoint, Object result, long lengthMillis) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection();
        }

        Signature signature = joinPoint.getSignature();

        Class<?> cls = signature.getDeclaringType();
        Logging annotation;

        if (signature instanceof MethodSignature) {
            annotation = ((MethodSignature) signature).getMethod().getAnnotation(Logging.class);
        } else if (signature instanceof ConstructorSignature) {
            annotation = (Logging) ((ConstructorSignature) signature).getConstructor().getAnnotation(Logging.class);
        } else {
            Log.e("azure", "cannot get annotation");
            return;
        }

        String tag = annotation.tag();
        int type = annotation.type();


        String methodName = signature.getName();
        boolean hasReturnType = signature instanceof MethodSignature
                && ((MethodSignature) signature).getReturnType() != void.class;

        StringBuilder builder = new StringBuilder("\u21E0 ")
                .append(methodName)
                .append(" [")
                .append(lengthMillis)
                .append("ms]");

        if (hasReturnType) {
            builder.append(" = ");
            builder.append(Strings.toString(result));
        }

        if (tag.equals("azure-Logging")) tag = "[azure-Logging]" + ToolBox.asTag(cls);

        ToolBox.print(type, tag, builder.toString());

    }

    protected static void enterMethod(JoinPoint joinPoint) {

        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        Class<?> cls = codeSignature.getDeclaringType();

        Logging annotation = cls.getAnnotation(Logging.class);

        if (annotation == null) {
            if (codeSignature instanceof MethodSignature) {
                annotation = ((MethodSignature) codeSignature).getMethod().getAnnotation(Logging.class);
            } else if (codeSignature instanceof ConstructorSignature) {
                annotation = (Logging) ((ConstructorSignature) codeSignature).getConstructor().getAnnotation(Logging.class);
            } else {
                Log.e("azure", "cannot get annotation");
                return;
            }
        }

        String tag = annotation.tag();
        int type = annotation.type();

        String methodName = codeSignature.getName();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        StringBuilder builder = new StringBuilder("\u21E2 ");
        builder.append(methodName).append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parameterNames[i]).append('=');
            builder.append(Strings.toString(parameterValues[i]));
        }
        builder.append(')');

        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }

        if (tag.equals("azure-Logging")) tag = "[azure-Logging]" + ToolBox.asTag(cls);

        ToolBox.print(type, tag, builder.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final String section = builder.toString().substring(2);
            Trace.beginSection(section);
        }
    }

    protected static void exitMethod(JoinPoint joinPoint, Object result, long lengthMillis) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection();
        }

        Signature signature = joinPoint.getSignature();

        Class<?> cls = signature.getDeclaringType();
        Logging annotation = cls.getAnnotation(Logging.class);

        if (annotation == null) {
            if (signature instanceof MethodSignature) {
                annotation = ((MethodSignature) signature).getMethod().getAnnotation(Logging.class);
            } else if (signature instanceof ConstructorSignature) {
                annotation = (Logging) ((ConstructorSignature) signature).getConstructor().getAnnotation(Logging.class);
            } else {
                Log.e("azure", "cannot get annotation");
                return;
            }
        }

        String tag = annotation.tag();
        int type = annotation.type();


        String methodName = signature.getName();
        boolean hasReturnType = signature instanceof MethodSignature
                && ((MethodSignature) signature).getReturnType() != void.class;

        StringBuilder builder = new StringBuilder("\u21E0 ")
                .append(methodName)
                .append(" [")
                .append(lengthMillis)
                .append("ms]");

        if (hasReturnType) {
            builder.append(" = ");
            builder.append(Strings.toString(result));
        }

        if (tag.equals("azure-Logging")) tag = "[azure-Logging]" + ToolBox.asTag(cls);

        ToolBox.print(type, tag, builder.toString());

    }

    public static boolean pingBack(Map<String, String> map) {
        Log.i("PingBackTest", "MSG contains: " + map.toString());

        return true;
    }
}
