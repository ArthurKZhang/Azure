package com.arthur.azure_core.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.arthur.azure_core.annotations.PermissionRequest;
import com.arthur.azure_core.aspects.ToolBox;

import junit.framework.Assert;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * two Advices: 'adviceOnNeedPermissionMethod()' and 'adviceOnActivityCreate()'
 */
@Aspect
public class PermissionAspect {
    private static final String Tag = "**firefly";

    private static Map<String, CheckPermissionItem> checkPermissionItems = new HashMap<String, CheckPermissionItem>();
    private static Map<String, Boolean> sActivitySessions = new HashMap<String, Boolean>();
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static final long DELAY_PERMISSION_DIALOG = 100;

    private static final String annoRef = "com.arthur.azure_core.permission.NeedPermission";


    @Pointcut("execution(@" + annoRef + " * *(..)) && @annotation(needPermission)")
    public void pointcutOnNeedPermissionMethod(NeedPermission needPermission) {

    }

    private static final String shaowActivityRef = "com.arthur.azure_core.permission.ShadowPermissionActivity";

    @Pointcut("execution(* android.app.Activity.onCreate(..)) && !within(android.support.v7.app.AppCompatActivity)" +
            " && !within(android.support.v4.app.FragmentActivity)" +
            " && !within(android.support.v4.app.BaseFragmentActivityDonut)" +
            " && !within(" + shaowActivityRef + ")")
    public void pointcutOnActivityCreate() {

    }

    //用在返回为void的方法上，包括private, public , static等修饰的方法
    @Around("pointcutOnNeedPermissionMethod(needPermission)")
    public void adviceOnNeedPermissionMethod(final ProceedingJoinPoint joinPoint, final NeedPermission needPermission) throws Throwable {
        Log.v(Tag, "catched, insode Advice");
        if (needPermission == null) {
            joinPoint.proceed();
            return;
        }

        if (needPermission.runIgnorePermission()) {
            joinPoint.proceed();
        }

        String[] permissions = needPermission.permissions();
        if (permissions != null && permissions.length > 0) {
            Context context = PermissionCheckSDK.application;
            Log.v(Tag, context.getApplicationInfo().className);
            PermissionItem permissionItem = new PermissionItem(permissions);

            String rationalMsg = getInfoContent(context, needPermission.rationalMessage(), needPermission.rationalMsgResId());
            String rationalBtn = getInfoContent(context, needPermission.rationalButton(), needPermission.rationalBtnResId());
            String deniedMsg = getInfoContent(context, needPermission.deniedMessage(), needPermission.deniedMsgResId());
            String deniedBtn = getInfoContent(context, needPermission.deniedButton(), needPermission.deniedBtnResId());
            String settingBtn = getInfoContent(context, needPermission.settingText(), needPermission.settingResId());

            if (!TextUtils.isEmpty(rationalMsg) && !TextUtils.isEmpty(rationalBtn)) {
                permissionItem.rationalMessage(rationalMsg).rationalButton(rationalBtn);
            }

            if (!TextUtils.isEmpty(deniedMsg) && !TextUtils.isEmpty(deniedBtn)) {
                permissionItem.deniedMessage(deniedMsg).deniedButton(deniedBtn);
            }

            if (!TextUtils.isEmpty(settingBtn)) {
                permissionItem.settingText(settingBtn);
            }

            permissionItem.needGotoSetting(needPermission.needGotoSetting());

            CheckPermission.instance(context).check(permissionItem, new PermissionListener() {
                @Override
                public void permissionGranted() {
                    if (!needPermission.runIgnorePermission()) {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }

                @Override
                public void permissionDenied() {
                }
            });
        }
    }


    @Around("pointcutOnActivityCreate()")
    public void adviceOnActivityCreate(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Activity target = (Activity) joinPoint.getTarget();
        String targetName = target.getClass().getName();
        if (!sActivitySessions.containsKey(targetName)) {
            sHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    NeedPermission np = target.getClass().getAnnotation(NeedPermission.class);
                    if (np != null) {
                        String[] permissions = np.permissions();
                        if (permissions != null && permissions.length > 0) {
                            processCheckPermissionOnActivity(target
                                    , permissions
                                    , getInfoContent(target, np.rationalMessage(), np.rationalMsgResId())
                                    , getInfoContent(target, np.rationalButton(), np.rationalBtnResId())
                                    , getInfoContent(target, np.deniedMessage(), np.deniedMsgResId())
                                    , getInfoContent(target, np.deniedButton(), np.deniedBtnResId())
                                    , getInfoContent(target, np.settingText(), np.settingResId())
                                    , np.needGotoSetting()
                                    , np.runIgnorePermission());
                        }
                    } else {
                        String classPath = joinPoint.getSourceLocation().getWithinType().getName();

                        if (checkPermissionItems.containsKey(classPath)) {
                            CheckPermissionItem checkPermissionItem = checkPermissionItems.get(classPath);
                            processCheckPermissionOnActivity(target
                                    , checkPermissionItem.permissionItem.permissions
                                    , checkPermissionItem.permissionItem.rationalMessage
                                    , checkPermissionItem.permissionItem.rationalButton
                                    , checkPermissionItem.permissionItem.deniedMessage
                                    , checkPermissionItem.permissionItem.deniedButton
                                    , checkPermissionItem.permissionItem.settingText
                                    , checkPermissionItem.permissionItem.needGotoSetting
                                    , checkPermissionItem.permissionItem.runIgnorePermission);
                        }
                    }
                }
            }, DELAY_PERMISSION_DIALOG);

            sActivitySessions.put(target.getClass().getName(), true);
        }

        joinPoint.proceed();
    }

    private static String getInfoContent(Context context, String strContent, int resId) {
        if (context == null) {
            return null;
        }

        if (TextUtils.isEmpty(strContent)) {
            if (resId <= 0) {
                return strContent;
            }

            try {
                return context.getString(resId);
            } catch (Resources.NotFoundException e) {
                return strContent;
            }
        }

        return strContent;
    }

    private static void processCheckPermissionOnActivity(final Activity target, String[] permissions, String rationalMessage, String rationalButton
            , String deniedMessage, String deniedButton, String settingText, boolean needGotoSetting, final boolean runIgnorePermission) {

        Assert.assertTrue(permissions != null && permissions.length > 0);

        PermissionItem permissionItem = new PermissionItem(permissions);

        if (!TextUtils.isEmpty(rationalMessage)
                && !TextUtils.isEmpty(rationalButton)) {
            permissionItem.rationalMessage(rationalMessage).rationalButton(rationalButton);
        }

        if (!TextUtils.isEmpty(deniedMessage)
                && !TextUtils.isEmpty(deniedButton)) {
            permissionItem.deniedMessage(deniedMessage).deniedButton(deniedButton);
        }

        if (!TextUtils.isEmpty(settingText)) {
            permissionItem.settingText(settingText);
        }

        permissionItem.needGotoSetting(needGotoSetting);

        CheckPermission.instance(target).check(permissionItem, new PermissionListener() {
            @Override
            public void permissionGranted() {
                sActivitySessions.remove(target.getClass().getName());
            }

            @Override
            public void permissionDenied() {
                sActivitySessions.remove(target.getClass().getName());
                if (!runIgnorePermission) {
                    target.finish();
                }
            }
        });
    }

    public static void addCheckPermissionItem(CheckPermissionItem item) {
        if (item != null && item.classPath != null) {
            checkPermissionItems.put(item.classPath, item);
        }
    }

}