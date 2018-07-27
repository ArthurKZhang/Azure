/*
 * CheckPermissionItem      2016-05-16
 * Copyright (c) 2016 hujiang Co.Ltd. All right reserved(http://www.hujiang.com).
 * 
 */
package com.arthur.azure_core.permission;

import android.text.TextUtils;
import java.io.Serializable;

/**
 * class description here
 */
public class CheckPermissionItem implements Serializable {

    public String classPath;
    public PermissionItem permissionItem;

    public CheckPermissionItem(String classPath, String...permissions) {
        if (TextUtils.isEmpty(classPath)) {
            throw new IllegalArgumentException("classPath must not be null or empty");
        }

        if (permissions == null || permissions.length <= 0) {
            throw new IllegalArgumentException("permissions must have one content at least");
        }
        permissionItem = new PermissionItem(permissions);
        this.classPath = classPath;
    }

    public CheckPermissionItem rationalMessage(String rationalMessage) {
        permissionItem.rationalMessage(rationalMessage);

        return this;
    }

    public CheckPermissionItem rationalButton(String rationalButton) {
        permissionItem.rationalButton(rationalButton);

        return this;
    }

    public CheckPermissionItem deniedMessage(String deniedMessage) {
        permissionItem.deniedMessage(deniedMessage);

        return this;
    }

    public CheckPermissionItem deniedButton(String deniedButton) {
        permissionItem.deniedButton(deniedButton);

        return this;
    }

    public CheckPermissionItem needGotoSetting(boolean needGotoSetting) {
        permissionItem.needGotoSetting(needGotoSetting);

        return this;
    }

    public CheckPermissionItem runIgnorePermission(boolean ignorePermission) {
        permissionItem.runIgnorePermission(ignorePermission);

        return this;
    }

    public CheckPermissionItem settingText(String settingText) {
        permissionItem.settingText(settingText);

        return this;
    }
}