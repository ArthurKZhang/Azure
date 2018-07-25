package com.arthur.azure_core;

/**
 * Created by zhangyu on 13/07/2018.
 */

import android.content.pm.PackageManager;
import android.util.Log;

import com.arthur.azure_core.annotations.PermissionFail;
import com.arthur.azure_core.annotations.PermissionSuccess;
import com.arthur.azure_core.aspects.LogAspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Azure configurations Interfaces
 */
public class AzureShip {

    public static final String TAG = "AzureShip";

    public static void setEnableLogging(Boolean isEnable) {
        LogAspect.setEnabled(isEnable);
    }

    /**
     * @param obj          Should be a Context Wrapper, An Activity would be good.
     * @param requestCode  int variable
     * @param permissions  String array
     * @param grantResults int array
     */
    public static void onRequestPermissionsResult(Object obj, int requestCode, String[] permissions, int[] grantResults) {

        //get denied permissions' list
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }
        Log.v(TAG, "Object: " + obj.getClass().getName() + "; DeniedPermissions:" + deniedPermissions.toString());
        if (deniedPermissions.size() > 0) {
            doExecuteFail(obj, requestCode);
        } else {
            doExecuteSuccess(obj, requestCode);
        }
    }

    private static void doExecuteFail(Object activity, int requestCode) {
        Method executeMethod = findMethodWithRequestCode(activity.getClass(), PermissionFail.class, requestCode);
        executeMethod(activity, executeMethod);
    }

    private static void doExecuteSuccess(Object activity, int requestCode) {
        Method executeMethod = findMethodWithRequestCode(activity.getClass(), PermissionSuccess.class, requestCode);
        executeMethod(activity, executeMethod);
    }

    private static void executeMethod(Object activity, Method executeMethod) {
        if (executeMethod != null) {
            try {
                if (!executeMethod.isAccessible()) executeMethod.setAccessible(true);
                executeMethod.invoke(activity, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static Method findMethodWithRequestCode(Class clazz, Class<? extends Annotation> annotation, int requestCode) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                if (isEqualRequestCodeFromAnntation(method, annotation, requestCode)) {
                    Log.v(TAG, "find method: " + method.getName() + " matched");
                    return method;
                }
            }
            Log.v(TAG, "find method name: " + method.getName());
        }
        return null;
    }

    private static boolean isEqualRequestCodeFromAnntation(Method method, Class<? extends Annotation> annotation, int requestCode) {
        if (annotation.equals(PermissionFail.class)) {
            return requestCode == method.getAnnotation(PermissionFail.class).requestCode();
        } else if (annotation.equals(PermissionSuccess.class)) {
            return requestCode == method.getAnnotation(PermissionSuccess.class).requestCode();
        } else {
            return false;
        }
    }


}
