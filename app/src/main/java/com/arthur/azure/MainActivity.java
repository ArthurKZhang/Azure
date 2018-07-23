package com.arthur.azure;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

//import azure_core.annotations.Logging;

import com.arthur.azure_core.AzureShip;
import com.arthur.azure_core.annotations.Logging;
import com.arthur.azure_core.annotations.PbClick;
import com.arthur.azure_core.annotations.PbElement;
import com.arthur.azure_core.annotations.PermissionAcquire;
import com.arthur.azure_core.annotations.PingBack;

import java.security.Permission;

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


//    @PermissionAcquire(targetSDKVersion = targetSDKVersion,)

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        AzureShip.onRequestPermissionsResult(MainActivity.this,requestCode,permissions,grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
