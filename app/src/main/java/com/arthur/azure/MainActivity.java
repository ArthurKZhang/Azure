package com.arthur.azure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

//import azure_core.annotations.Logging;

import com.arthur.azure_core.annotations.Logging;

@Logging(tag = "CLASS")
public class MainActivity extends AppCompatActivity {

    @Logging
    private String abc;
    @Logging
    private int[] ints;

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
    }

    //    @Logging
    public void a() {
        b(abc);
    }

    @Logging(tag = "BBB")
    private int b(String s) {
        return 0;
    }
}
