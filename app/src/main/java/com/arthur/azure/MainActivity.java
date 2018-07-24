package com.arthur.azure;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import azure_core.annotations.Logging;

import com.arthur.azure_core.AzureShip;
import com.arthur.azure_core.annotations.Logging;
import com.arthur.azure_core.annotations.PbClick;
import com.arthur.azure_core.annotations.PbElement;
import com.arthur.azure_core.annotations.PermissionFail;
import com.arthur.azure_core.annotations.PermissionSuccess;
import com.arthur.azure_core.annotations.PingBack;

//@Logging(tag = "CLASS")
public class MainActivity extends Activity {

    @Logging
    private String abc;
    @Logging
    private int[] ints;

    Button bt_testOnclick;

//ActivityCompat./**//**/
    //    @Logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        abc = "asdfg";

        ints = new int[]{1, 2, 3};

        a();
        A a = new A();
        a.toString();
        System.out.println(ints);

        bt_testOnclick = (Button) this.findViewById(R.id.bt_testOnclick);
        bt_testOnclick.setOnClickListener(new View.OnClickListener() {
            @PbClick(rseat = "BT_TEST_ONCLICK")
            @Override
            public void onClick(View view) {
                Log.i("___****___", "HHHHHHHHHHHHHAAAAAA");
            }
        });
//        Fragment
    }

    //    @Logging
    public void a() {
        b(abc);
    }

    //    @Logging(tag = "BBB")
    @PingBack(pairs = {
            @PbElement(keyName = "Class", contentValue = "MainActivity"),
            @PbElement(keyName = "Method", contentValue = "b()")
    })
    private int b(String s) {
        return 0;
    }


//    @PermissionRequest(targetSDKVersion = targetSDKVersion,)

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        AzureShip.onRequestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public static final int REQUECT_CODE_SDCARD = 1;
    public static final int REQUECT_CODE_CALL_PHONE = 2;


    @PermissionSuccess(requestCode = REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess() {
        Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = REQUECT_CODE_SDCARD)
    public void requestSdcardFailed() {
        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }


    @PermissionSuccess(requestCode = REQUECT_CODE_CALL_PHONE)
    public void requestCallPhoneSuccess() {
        Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = REQUECT_CODE_CALL_PHONE)
    public void requestCallPhoneFailed() {
        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }
}
