package com.dfxh.wang.serialport_test.ui.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dfxh.wang.serialport_test.BaseActivity;
import com.dfxh.wang.serialport_test.R;
import com.dfxh.wang.serialport_test.utils.BinStr;

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String string = "01031444030000000000000000000000000000000000001868";
                String s1 = Integer.toBinaryString(0x4403);

                byte[] bytes = BinStr.parseHexStr2Byte("4403");

                String s = new String(bytes);


                Log.e("aaa", "(TestActivity.java:28)<---->" + s);

                Log.e("aaa", "(TestActivity.java:34)<---->" + s.length());
            }
        });
    }


}
