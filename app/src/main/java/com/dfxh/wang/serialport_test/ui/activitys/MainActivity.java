package com.dfxh.wang.serialport_test.ui.activitys;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dfxh.wang.serialport_test.R;

import java.io.FileDescriptor;

import android_serialport_api.SerialPort;
import utils.SerialPortUtils;

public class MainActivity extends AppCompatActivity implements SerialPortUtils.OnDataReceiveListener {
    private final String TAG = "MainActivity";

    private Button button_open;
    private Button button_close;
    private EditText editText_send;
    private Button button_send;
    private TextView textView_status, tvText;
    private Button button_status;
    private Spinner spinner_one;

    private SerialPortUtils serialPortUtils = new SerialPortUtils("flag");
    private SerialPort serialPort;

    private Handler handler;
    private byte[] mBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String hex = "66664176";

        Float value = Float.intBitsToFloat(Integer.valueOf(hex, 16));


        Log.e("aaa",
                "(MainActivity.java:47)<---->" + value);

        handler = new Handler(); //创建主线程的handler  用于更新UI

        button_open = (Button) findViewById(R.id.button_open);
        button_close = (Button) findViewById(R.id.button_close);
        button_send = (Button) findViewById(R.id.button_send);
        editText_send = (EditText) findViewById(R.id.editText_send);
        textView_status = (TextView) findViewById(R.id.textView_status);
        tvText = (TextView) findViewById(R.id.tv_text);
        button_status = (Button) findViewById(R.id.button_status);
        spinner_one = (Spinner) findViewById(R.id.spinner_one);

        editText_send.setText("S1");

        button_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //serialPortUtils = new SerialPortUtils();
                serialPort = serialPortUtils.openSerialPort();
                if (serialPort == null) {
                    Log.e(TAG, "串口打开失败");
                    Toast.makeText(MainActivity.this, "串口打开失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                textView_status.setText("串口已打开");
                Toast.makeText(MainActivity.this, "串口已打开", Toast.LENGTH_SHORT).show();

            }
        });
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serialPortUtils.closeSerialPort();
                textView_status.setText("串口已关闭");
                Toast.makeText(MainActivity.this, "串口关闭成功", Toast.LENGTH_SHORT).show();
            }
        });
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String aaa = "01033044030102030405060708090A0B0C0D0E0F102030405060708090A0B0C0D0E0F0112233445566778899AABBCCDDEEFF00DFA6";
                serialPortUtils.sendSerialPort(aaa);
//               textView_status.setText("串口发送指令：" + serialPortUtils.data_);
                Toast.makeText(MainActivity.this, "发送指令： " + aaa, Toast.LENGTH_SHORT).show();
            }
        });
        button_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //boolean status = serialPortUtils.serialPortStatus;
                //textView_status.setText(String.valueOf(status));
                FileDescriptor fileDescriptor = serialPort.mFd;
                String result = fileDescriptor.toString();
                textView_status.setText(result);
            }
        });
        //串口数据监听事件
        serialPortUtils.setOnDataReceiveListener(new SerialPortUtils.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                Log.d(TAG, "进入数据监听事件中。。。" + new String(buffer));
                //
                //在线程中直接操作UI会报异常：ViewRootImpl$CalledFromWrongThreadException
                //解决方法：handler
                //
                mBuffer = buffer;
                handler.post(runnable);
            }

            //开线程更新UI
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    textView_status.setText("size：" + String.valueOf(mBuffer.length) + "数据监听：" + new String(mBuffer));
                }
            };
        });


//        //定义一个下拉列表适配器
//        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.data,R.layout.support_simple_spinner_dropdown_item);
//        spinner_one.setAdapter(arrayAdapter); //将适配器传入spinner
//        //设置选中事件
//        spinner_one.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                //获取选中数据
//                String a = spinner_one.getSelectedItem().toString();
//                Toast.makeText(MainActivity.this,"选中了"+a,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

    }

    @Override
    public void onDataReceive(final byte[] buffer, int size) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvText.setText(buffer.toString());
            }
        });
    }
}
