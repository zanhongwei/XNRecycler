package com.dfxh.wang.serialport_test.ui.activitys;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dfxh.wang.serialport_test.BaseActivity;
import com.dfxh.wang.serialport_test.R;
import com.dfxh.wang.serialport_test.presenter.ScreenActivityPresenter;
import com.dfxh.wang.serialport_test.view.ScreenActivityView;

import android_serialport_api.SerialPort;
import utils.SerialPortUtils;

public class ScreenActivity extends BaseActivity implements View.OnClickListener, ScreenActivityView {

    private boolean isContinue = true;
    private SerialPort serialPort = null;
    private SerialPortUtils spUtils = null;

    private String sendCode = "";
    private String metalCode = "01050CCAFF00AF54";
    private String plasticsCode = "01050CCCFF004F55";
    private String textileCode = "01050CCEFF00EE95";
    private String paperCode = "01050CD0FF008E93";
    private String glassCode = "01050CD2FF002F53";
    private String wasteCode = "01050CD4FF00CF52";
    private String bottleCode = "01050CD6FF006E92";
    private String sort = "metal";
    private ScreenActivityPresenter mPresenter;

    private final String METAL_CLOSE_SORT = "metal";
    private final String PLASTICS_CLOSE_SORT = "plastics";
    private final String TEXTILE_CLOSE_SORT = "textile";
    private final String PAPER_CLOSE_SORT = "paper";
    private final String GLASS_CLOSE_SORT = "glass";
    private final String WASTE_CLOSE_SORT = "waste";
    private final String BOTTLE_CLOSE_SORT = "bottle";
    private final String FINISH = "finish";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        ImageView ivScreen = findViewById(R.id.iv_screen);
        ivScreen.setOnClickListener(this);
        Log.e("aaa", "(ScreenActivity.java:26)<---->" + " run OnCreate() function");
        isContinue = true;
//        closeBoxDoor();
    }



    private void closeBoxDoor() {
        if (spUtils == null) spUtils = new SerialPortUtils();
        mPresenter = new ScreenActivityPresenter(this);
        mPresenter.openSerialPort();
    }

    @Override
    public void onClick(View view) {
        if (isContinue)
            startActivity(new Intent(ScreenActivity.this, BoxManagerActivity.class));
        else
            Toast.makeText(this, "设备异常", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        soundPool.release();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("aaa", "(ScreenActivity.java:46)<---->" + "run onNewIntent() function");
//        closeBoxDoor();
    }

    @Override
    public void errorListener(String msgError) {
        Toast.makeText(this, "设备异常", Toast.LENGTH_SHORT).show();
        isContinue = false;
    }

    @Override
    public void openSerialPort(SerialPort sp) {
        this.serialPort = sp;
        sort = METAL_CLOSE_SORT;
        sendCode = metalCode;
        mPresenter.sendDataWithSP(sendCode);
        mPresenter.readData();
    }

    @Override
    public void closeSerialPort(String msg) {
        //关闭串口成功
    }

    @Override
    public void readDataSuccess(String sData) {
        // TODO: 2018/11/28 读取数据成功
        if (sort.equals(FINISH)) {
            mPresenter.closeSerialPort();
        } else {
            mPresenter.sendDataWithSP(sendCode);
            mPresenter.readData();
        }
    }

    @Override
    public void readDataError() {
        isContinue = false;
    }

    @Override
    public SerialPort vertifySerialPort() {
        return serialPort;
    }

    @Override
    public SerialPortUtils senSPUtils() {
        return spUtils;
    }

    @Override
    public void sendDataSuccess(String msgSuccess) {
        switch (sort) {
            case METAL_CLOSE_SORT:
                sort = PLASTICS_CLOSE_SORT;
                sendCode = plasticsCode;
                break;
            case PLASTICS_CLOSE_SORT:
                sort = TEXTILE_CLOSE_SORT;
                sendCode = textileCode;
                break;
            case TEXTILE_CLOSE_SORT:
                sort = PAPER_CLOSE_SORT;
                sendCode = paperCode;
                break;
            case PAPER_CLOSE_SORT:
                sort = GLASS_CLOSE_SORT;
                sendCode = glassCode;
                break;
            case GLASS_CLOSE_SORT:
                sort = WASTE_CLOSE_SORT;
                sendCode = wasteCode;
                break;
            case WASTE_CLOSE_SORT:
                sort = BOTTLE_CLOSE_SORT;
                sendCode = bottleCode;
                break;
            case BOTTLE_CLOSE_SORT:
                sort = FINISH;
                mPresenter.closeSerialPort();
                break;
        }
//        mPresenter.readData();
    }

    @Override
    public void sendDataError(String msgError) {
        Toast.makeText(this, "设备异常", Toast.LENGTH_SHORT).show();
        isContinue = false;
    }

    @Override
    public String getDeviceId() {
        return "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
