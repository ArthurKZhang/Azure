/*
 * CheckPermissionItem      2016-05-16
 * Copyright (c) 2016 hujiang Co.Ltd. All right reserved(http://www.hujiang.com).
 * 
 */
package com.arthur.azure_core.permission;

import java.io.Serializable;

public class PermissionItem implements Serializable {

    public String[] permissions;
    public String rationalMessage;
    public String rationalButton;
    public String deniedMessage;
    public String deniedButton;
    public String settingText;
    public boolean needGotoSetting;
    public boolean runIgnorePermission;

    public PermissionItem(String...permissions) {
        if (permissions == null || permissions.length <= 0) {
            throw new IllegalArgumentException("permissions must have one content at least");
        }

        this.permissions = permissions;
    }

    public PermissionItem rationalMessage(String rationalMessage) {
        this.rationalMessage = rationalMessage;

        return this;
    }

    public PermissionItem rationalButton(String rationalButton) {
        this.rationalButton = rationalButton;

        return this;
    }

    public PermissionItem deniedMessage(String deniedMessage) {
        this.deniedMessage = deniedMessage;

        return this;
    }

    public PermissionItem deniedButton(String deniedButton) {
        this.deniedButton = deniedButton;

        return this;
    }

    public PermissionItem needGotoSetting(boolean needGotoSetting) {
        this.needGotoSetting = needGotoSetting;

        return this;
    }

    public PermissionItem runIgnorePermission(boolean ignorePermission) {
        this.runIgnorePermission = ignorePermission;

        return this;
    }

    public PermissionItem settingText(String settingText) {
        this.settingText = settingText;
        return this;
    }
}