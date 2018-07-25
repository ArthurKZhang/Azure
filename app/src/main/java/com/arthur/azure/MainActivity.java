package com.arthur.azure;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import azure_core.annotations.Logging;

import com.arthur.azure_core.AzureShip;
import com.arthur.azure_core.annotations.Logging;
import com.arthur.azure_core.annotations.PbClick;
import com.arthur.azure_core.annotations.PbElement;
import com.arthur.azure_core.annotations.PermissionFail;
import com.arthur.azure_core.annotations.PermissionRequest;
import com.arthur.azure_core.annotations.PermissionSuccess;
import com.arthur.azure_core.annotations.PingBack;

//@Logging(tag = "CLASS")
public class MainActivity extends Activity {


    Button bt_testPermission;
    TextView tv_testPerms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String versions = "build sdk-" + Build.VERSION.SDK_INT + "; target sdk-" + this.getApplicationInfo().targetSdkVersion;
        tv_testPerms = (TextView) findViewById(R.id.tv_testPerms);
        tv_testPerms.setText(versions);


        bt_testPermission = (Button) findViewById(R.id.bt_testPermission);

        bt_testPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "inside onClick(), button PermissionTest clicked");
                Log.v(TAG, "inside onClick(), before bbbbbb execution");
                bbbbbb();
                Log.v(TAG, "inside onClick(), after bbbbbb execution");
            }
        });

    }

    public static final String TAG = "MainActiity";

    @PermissionRequest(requestCode = REQUECT_CODE_SDCARD, permissions = {Manifest.permission.READ_EXTERNAL_STORAGE})
    private void bbbbbb() {
        businessFunction();
    }

    private void businessFunction() {
        Toast.makeText(MainActivity.this, "businessFunction", Toast.LENGTH_SHORT).show();
        Log.v(TAG, "inside businessFunction");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        AzureShip.onRequestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public static final int REQUECT_CODE_SDCARD = 1;
//    public static final int REQUECT_CODE_CALL_PHONE = 2;


    @PermissionSuccess(requestCode = REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess() {
        Log.v(TAG, "inside requestSdcardSuccess() method");
        businessFunction();
        Log.v(TAG, "inside requestSdcardSuccess(), after bus-function execution");
    }

    @PermissionFail(requestCode = REQUECT_CODE_SDCARD)
    public void requestSdcardFailed() {
        Log.v(TAG, "inside requestSdcardFailed() method");
        Toast.makeText(MainActivity.this, "FFF-requestSdcardFailed", Toast.LENGTH_SHORT).show();
    }


//    @PermissionSuccess(requestCode = REQUECT_CODE_CALL_PHONE)
//    public void requestCallPhoneSuccess() {
//        Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
//    }
//
//    @PermissionFail(requestCode = REQUECT_CODE_CALL_PHONE)
//    public void requestCallPhoneFailed() {
//        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
//    }
}
