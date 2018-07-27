package com.arthur.azure;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.arthur.azure_core.permission.NeedPermission;

@NeedPermission(permissions = {android.Manifest.permission.READ_PHONE_STATE}, runIgnorePermission = true)
public class Main2Activity extends AppCompatActivity {
    private static final String Tag = "@@@@";

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button = (Button) findViewById(R.id.bt111);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(Tag, "----------1----------");
                pppppp();
                Log.v(Tag, "----------2----------");
            }
        });
    }

    @NeedPermission(permissions = {Manifest.permission.READ_EXTERNAL_STORAGE},
            rationalMessage = "合理性解释",
            rationalButton = "合解按钮",
            deniedMessage = "禁止msg",
            deniedButton = "禁止but",
            settingText = "设置App",
            needGotoSetting = true)
    private void pppppp() {
        Log.v(Tag, "inside method pppp()");
    }
}
