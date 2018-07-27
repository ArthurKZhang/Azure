/*
 * ModulesAOPHub      2016-05-16
 * Copyright (c) 2016 hujiang Co.Ltd. All right reserved(http://www.hujiang.com).
 * 
 */
package com.arthur.azure_core.permission;

import android.app.Application;


public class PermissionCheckSDK {

    public static Application application;

    public static void addCheckPermissionItem(CheckPermissionItem item) {
        PermissionAspect.addCheckPermissionItem(item);
    }

    public static void init(Application app) {
        if (app == null) {
            throw new IllegalArgumentException("application must not be null");
        }
        application = app;
    }
}