package com.arthur.azure;

import android.app.Application;
import android.os.Build;

import com.arthur.azure_core.aspects.ToolBox;

public class APP extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        ToolBox.buildSDKVersion = Build.VERSION.SDK_INT;
        ToolBox.targetSDKVersion = this.getApplicationInfo().targetSdkVersion;

    }
}
