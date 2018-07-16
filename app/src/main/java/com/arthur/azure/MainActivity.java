package com.arthur.azure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

//import azure_core.annotations.Logging;

import com.arthur.azure_core.annotations.Logging;

//@Logging(tag = "asdfg", type = 3)
public class MainActivity extends AppCompatActivity {

    //    @Logging
    private String abc;

    @Logging
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        abc = "asdfg";
//        Log.v("Log","asdfg");
        a();
    }

    @Logging
    public void a() {
    }

}
