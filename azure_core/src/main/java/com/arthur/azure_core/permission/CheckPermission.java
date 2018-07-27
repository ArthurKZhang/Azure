package com.arthur.azure_core.permission;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.arthur.azure_core.permission.PermissionCheckSDK.PERMISSION_TAG;

/**
 * check permission
 * <br>
 */
public class CheckPermission {
    private static final String Tag = "CheckPermission";

    private static CheckPermission sInstance;
    private final Context mContext; //A Application Context
    private PermissionRequestWrapper mCurPermissionRequestWrapper;
    private Queue<PermissionRequestWrapper> mPermissionRequestWrappers = new ConcurrentLinkedQueue<>();
    // a callback function AFTER requesting permission.
    private ShadowPermissionActivity.OnPermissionRequestFinishedListener mOnPermissionRequestFinishedListener = new ShadowPermissionActivity.OnPermissionRequestFinishedListener() {
        @Override
        public boolean onPermissionRequestFinishedAndCheckNext(String[] permissions) {
            mCurPermissionRequestWrapper = mPermissionRequestWrappers.poll();
            if (mCurPermissionRequestWrapper != null) {
                requestPermissions(mCurPermissionRequestWrapper);
            }

            return mCurPermissionRequestWrapper != null;
        }
    };

    public static CheckPermission getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (CheckPermission.class) {
                if (sInstance == null) {
                    sInstance = new CheckPermission(context);
                }
            }
        }

        return sInstance;
    }

    private CheckPermission(Context context) {
        this.mContext = context.getApplicationContext();

        ShadowPermissionActivity.setOnPermissionRequestFinishedListener(mOnPermissionRequestFinishedListener);
    }

    public void check(PermissionItem permissionItem, PermissionListener permissionListener) {
        if (permissionItem == null || permissionListener == null) {
            return;
        }

        if (!PermissionUtils.isOverMarshmallow()) {
            Log.v(PERMISSION_TAG, "Build.VERSION.SDK_INT < Build.VERSION_CODES.M (android 6.0)");
            //6.0以下manifest声明了 安卓便授予权限
            onPermissionGranted(permissionItem, permissionListener);
        } else {
            mPermissionRequestWrappers.add(new PermissionRequestWrapper(permissionItem, permissionListener));
            if (mCurPermissionRequestWrapper == null) {
                mCurPermissionRequestWrapper = mPermissionRequestWrappers.poll();
                requestPermissions(mCurPermissionRequestWrapper);
            }
        }
    }

//    private Handler mHandler = new Handler(Looper.getMainLooper());

    private void requestPermissions(PermissionRequestWrapper permissionRequestWrapper) {
        final PermissionItem item = permissionRequestWrapper.permissionItem;
        final PermissionListener listener = permissionRequestWrapper.permissionListener;

        if (PermissionUtils.hasSelfPermissions(mContext, item.permissions)) {
            onPermissionGranted(item, listener);
        } else {
            ShadowPermissionActivity.start(mContext
                    , item.permissions
                    , item.rationalMessage
                    , item.rationalButton
                    , item.needGotoSetting
                    , item.settingText
                    , item.deniedMessage
                    , item.deniedButton
                    , listener);
        }
    }

    private void onPermissionGranted(PermissionItem item, PermissionListener listener) {

        if (listener != null) {
            listener.permissionGranted();
        }

        mOnPermissionRequestFinishedListener.onPermissionRequestFinishedAndCheckNext(item.permissions);
    }

    private void onPermissionDenied(PermissionItem item, PermissionListener listener) {

        if (listener != null) {
            listener.permissionDenied();
        }

        mOnPermissionRequestFinishedListener.onPermissionRequestFinishedAndCheckNext(item.permissions);
    }

    class PermissionRequestWrapper {
        PermissionItem permissionItem;
        PermissionListener permissionListener;

        public PermissionRequestWrapper(PermissionItem item, PermissionListener listener) {
            this.permissionItem = item;
            this.permissionListener = listener;
        }
    }

}
