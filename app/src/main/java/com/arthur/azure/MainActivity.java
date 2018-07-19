package com.arthur.azure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

//import azure_core.annotations.Logging;

import com.arthur.azure_core.annotations.Logging;
import com.arthur.azure_core.annotations.PbClick;

//@Logging(tag = "CLASS")
public class MainActivity extends AppCompatActivity {

    @Logging
    private String abc;
    @Logging
    private int[] ints;

    Button bt_testOnclick;


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
                Log.i("___****___","HHHHHHHHHHHHHAAAAAA");
            }
        });
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
