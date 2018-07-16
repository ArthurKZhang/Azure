package com.arthur.azure_core;

/**
 * Created by zhangyu on 13/07/2018.
 */

import com.arthur.azure_core.aspects.LogAspect;

/**
 * Azure configurations Interfaces
 */
public class AzureShip {

    public static void setEnableLogging(Boolean isEnable){
        LogAspect.setEnabled(isEnable);
    }

}
