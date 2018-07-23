package com.arthur.azure_core.aspects;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.arthur.azure_core.annotations.PermissionAcquire;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class PermissionAspect {

    private static final String PermiAnnoRef = "com.arthur.azure_core.annotations.PermissionAcquire";

    @Around("execution(@" + PermiAnnoRef + " * *(..)) && @annotation(permission)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, final PermissionAcquire permission) throws Throwable {

        Object target = joinPoint.getTarget();
        Activity activity = null;
        if (target instanceof Activity) {
            activity = (Activity) target;
        }
        if (target instanceof Fragment) {
            activity = ((Fragment) target).getActivity();
        }
        if (target instanceof android.support.v4.app.Fragment) {
            activity = ((android.support.v4.app.Fragment) target).getActivity();
        }

        if (activity == null) return; //ERROR sth wrong happened

        String[] perms = permission.permissions();
        int reqCode = permission.requestCode();
        String[] permsNeeded = new String[]{};
//        String[] permsGrated

        //进行正常的动态权限检查和申请逻辑. Context.checkSelfPermission()进行check.
        if (ToolBox.buildSDKVersion >= 23 && ToolBox.targetSDKVersion >= 23) {
            //1. check permission holding
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int i = 0;
                for (String perm : perms) {
                    if (activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED)
                        permsNeeded[i] = perm;
                    i++;
                }
            }
            if (permsNeeded.length == 0) {
                joinPoint.proceed();
                return;
            }

            //require permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(permsNeeded, reqCode);
            }

        }

        // 版本高的手机上装的是一个老版的应用，
        //动态权限检查-API方法失效: Contex.checkSelfPermission(this,Manifest.permission.CAMERA),
        //使用PermissionChecker.checkSelfPermission(context, permission) API. 在进行动态权限申请.
        if (ToolBox.buildSDKVersion >= 23 && ToolBox.targetSDKVersion < 23) {

        }

//         权限申请在Manifest文件中，安装成功默认声明的权限都开启。
//         但是考虑到不同ROM厂家，照样需要检测是否有权限，若没有仅给出提示。
        if (ToolBox.buildSDKVersion < 23 && ToolBox.targetSDKVersion < 23) {

        }


        final AppCompatActivity ac = null;//(AppCompatActivity) Application.getAppContext().getCurActivity();
        new AlertDialog.Builder(ac)
                .setTitle("提示")
                .setMessage("为了应用可以正常使用，请您点击确认申请权限。")
                .setNegativeButton("取消", null)
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MPermissionUtils.requestPermissionsResult(ac, 1, permission.permissions()
                                , new MPermissionUtils.OnPermissionListener() {
                                    @Override
                                    public void onPermissionGranted() {
                                        try {
                                            joinPoint.proceed();//获得权限，执行原方法
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onPermissionDenied() {
                                        MPermissionUtils.showTipsDialog(ac);
                                    }
                                });
                    }
                })
                .create()
                .show();
    }
}
