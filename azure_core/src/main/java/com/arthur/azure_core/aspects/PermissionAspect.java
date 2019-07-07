package com.arthur.azure_core.aspects;

import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.arthur.azure_core.annotations.PermissionRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Aspect
public class PermissionAspect {

    //TODO If annotaion annotated on a Class(Type), when should we check required permissions??

    private static final String PermiAnnoRef = "com.arthur.azure_core.annotations.PermissionRequest";

    @Pointcut("execution(@" + PermiAnnoRef + " * *(..))")
    public void annotatedMethod() {
    }

    public static final String TAG = "permissionTest-Aspect";

    @Around("annotatedMethod() && @annotation(permAnnot)")
    public void aroundMethod(final ProceedingJoinPoint joinPoint, final PermissionRequest permAnnot) throws Throwable {

        Object target = joinPoint.getTarget();

        Field field = target.getClass().getDeclaredField("this$0");
        field.setAccessible(true);
        Object o = field.get(target);

        Log.v(TAG, "joinPoint target is a " + target.getClass().getName());
        Activity activity = null;
        if (target instanceof Activity) {
            activity = (Activity) target;
        }

        if (activity == null) {
            Log.e(TAG, "Activity is null, aspect aroundMethod() return;");
            return; //ERROR sth wrong happened
        }

        String[] perms = permAnnot.permissions();
        int reqCode = permAnnot.requestCode();
        List<String> permsNeeded = new ArrayList<String>();

        //TODO if activity is an object of Fragment, do something else

        //进行正常的动态权限检查和申请逻辑. Context.checkSelfPermission()进行check.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ToolBox.targetSDKVersion >= 23) {
            //1. check permission holding
            for (String perm : perms) {
                if (activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED)
                    permsNeeded.add(perm);
            }
            Log.v(TAG, "need permission: " + permsNeeded);
            if (permsNeeded.size() == 0) {
                Log.v(TAG, "permissions are already holden, do the Job.");
                joinPoint.proceed();
                Log.v(TAG, "after Joinpoint.proceed(),next statement will be return;");
                return;
            } else {
                //request permission
                //TODO 可以用 shouldShowRequestPermissionRationale() 方法来判断是否需要弹出 解释框
                Log.v(TAG, "asynchronous request permission");
                //ATTENTION Asynchronous execution
                activity.requestPermissions(fuckedupCastException(permsNeeded), reqCode);
                Log.v(TAG, "aspect aroundMethod() return;");
                return;
            }
        }
        // 以上的运行流程是：如果权限已经持有，执行注解方法内的逻辑；如果注解没有持有，去申请权限，会弹出系统弹窗
        // 系统弹窗点击'同意授权'，执行授权成功的回调方法，点击'取消授权'，执行授权失败的回调方法。

        Log.e("permissionTest", "should not access this statement!!");


//
        // 版本高的手机上装的是一个老版的应用，
        //动态权限检查-API方法失效: Contex.checkSelfPermission(this,Manifest.permission.CAMERA),
        //使用PermissionChecker.checkSelfPermission(context, permission) API. 在进行动态权限申请.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ToolBox.targetSDKVersion < 23) {
            //1. check permission holding
            for (String perm : perms) {
                if (activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED)
                    permsNeeded.add(perm);
            }
            if (permsNeeded.size() == 0) {
                Log.v(TAG, "permissions are already holden, do the Job.");
                joinPoint.proceed();
                Log.v(TAG, "after Joinpoint.proceed(),next statement will be return;");
                return;
            } else {
                activity.requestPermissions(fuckedupCastException(permsNeeded), reqCode);
                //这里是没有持有授权，然后只会执行授权失败的回调方法，不会有系统弹窗，直接走的是用户拒却的流程。
                //这里可以进行弹窗提示用户去设置
//                final Activity ac = activity;
//                new AlertDialog.Builder(ac)
//                        .setTitle("提示")
//                        .setMessage("为了应用可以正常使用，请您去设置权限。")
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("允许", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // 跳转到设置页面，不同ROM不同
//                            }
//                        })
//                        .create()
//                        .show();

                return;
            }
        }
//
        //权限申请在Manifest文件中，安装成功默认声明的权限都开启。
        //但是考虑到不同ROM厂家，照样需要检测是否有权限，若没有仅给出提示。
        if (ToolBox.buildSDKVersion < 23 && ToolBox.targetSDKVersion < 23) {
            // 不处理
        }
    }

    private String[] fuckedupCastException(List<String> list) {
        String[] result = new String[list.size()];
        int i = 0;
        for (String s : list) {
            result[i] = s;
            i++;
        }
        return result;
    }

}
