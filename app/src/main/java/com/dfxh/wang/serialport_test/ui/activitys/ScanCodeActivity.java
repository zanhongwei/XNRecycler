package com.dfxh.wang.serialport_test.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.dfxh.wang.serialport_test.BaseActivity;
import com.dfxh.wang.serialport_test.CalfApplication;
import com.dfxh.wang.serialport_test.R;
import com.dfxh.wang.serialport_test.beans.LogBean;
import com.dfxh.wang.serialport_test.beans.ScanCodeStateBean;
import com.dfxh.wang.serialport_test.presenter.ScanCodeActivityPresenter;
import com.dfxh.wang.serialport_test.utils.AnalyseDataUtil;
import com.dfxh.wang.serialport_test.utils.BytesHexStrTranslate;
import com.dfxh.wang.serialport_test.utils.CountDownTimerUtils;
import com.dfxh.wang.serialport_test.utils.DeviceUuidFactory;
import com.dfxh.wang.serialport_test.utils.MyUtils;
import com.dfxh.wang.serialport_test.utils.QRCode;
import com.dfxh.wang.serialport_test.utils.SpUtil;
import com.dfxh.wang.serialport_test.view.ScanCodeView;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android_serialport_api.SerialPort;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.SerialPortUtils;

public class ScanCodeActivity extends BaseActivity implements ScanCodeView {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_tb_phone)
    TextView tvTbPhone;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_company_num)
    TextView tvTbComNum;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    private ScanCodeActivityPresenter mPresenter;
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            // 要做的事情
//            canRead = true;
            mPresenter.scanCodeState();
            super.handleMessage(msg);

        }
    };
    private String code;//串口指令
    private String weightCode = "010305F2000D2530";
    /*下位机发送的数据*/
    private String sendCode = "";
    private double value;
    private int timeOutCount = 0;
    private SerialPort serialPort;
    private SerialPortUtils serialPortUtils;
    private String imgStr;
    private LoadingDailog dialog;
    private String deviceUuid;
    private String sort;
    private List<LogBean> logBeans;
    private ScanCodeStateBean beans;
    private Timer timer;
    private TimerTask task;
    private SoundPool soundPool;
    private int load;
    private CountDownTimerUtils countDownTimerUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("aaa",
                "(ScanCodeActivity.java:83)<--进入OnCreate()方法-->");
        upLoadLog(new Gson().toJson(new LogBean(System.currentTimeMillis(), "进入扫码页面")));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        ButterKnife.bind(this);
        code = getIntent().getStringExtra("code");
        sort = getIntent().getStringExtra("sort");

        //此集合只记录操作事件
        logBeans = new ArrayList<>();
//        code = "01050CC9FF005F54";//屏金属开
        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(this);
        deviceUuid = deviceUuidFactory.getDeviceUuid().toString();
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                .setMessage("加载中...")
                .setCancelable(true)
                .setCancelOutside(false);
        dialog = loadBuilder.create();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                handler2.sendMessage(message);
            }
        };
        timer.schedule(task, 2000, 2000);
        serialPortUtils = new SerialPortUtils("scode");
        mPresenter = new ScanCodeActivityPresenter(this);
        mPresenter.getTimeStemp();

        soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 5);
        //加载资源
        load = soundPool.load(this, R.raw.open_success, 1);

        countDownTimerUtils = new CountDownTimerUtils(tvTime, 119000, 1000, new CountDownTimerUtils.MyListener() {

            @Override
            public void refresh() {
                startActivity(new Intent(ScanCodeActivity.this, ScreenActivity.class));
                finish();
            }
        });
        countDownTimerUtils.start();
    }

    @OnClick({R.id.iv_right, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                timer.cancel();
                countDownTimerUtils.cancel();
                if (countDownTimerUtils != null)
                    countDownTimerUtils = null;
                startActivity(new Intent(this, RecyclerLoginActivity.class));
                finish();
                break;
            case R.id.tv_cancel:
                timer.cancel();
                startActivity(new Intent(this, BoxManagerActivity.class));
                finish();
                countDownTimerUtils.cancel();
                if (countDownTimerUtils != null)
                    countDownTimerUtils = null;
                break;
        }
    }

    @Override
    public void ScanCodeSuccess(ScanCodeStateBean bean) {
//        handler1.removeCallbacks(runnable1);
        beans = bean;
        //第一步  打开串口

        if (countDownTimerUtils != null) countDownTimerUtils.cancel();
        logBeans.add(new LogBean(System.currentTimeMillis(), "扫码成功"));
        logBeans.add(new LogBean(System.currentTimeMillis(), "发送执行开箱命令"));
        logBeans.add(new LogBean(System.currentTimeMillis(), "发送记录箱内重量"));
        SpUtil.putList(this, "logs", logBeans);
        upLoadLog(new Gson().toJson(logBeans));
        if (null != soundPool)
            soundPool.play(load, 1, 1, 1, 0, 1);
        if (TextUtils.isEmpty(sendCode))
            mPresenter.openSerialPort();
        if (null != timer)
            timer.cancel();
    }

    @Override
    public void ScanCodeError(String errorStr) {
//        Toast.makeText(this, errorStr, Toast.LENGTH_SHORT).show();
//        mPresenter.scanCodeState();
//        finish();
    }

    @Override
    public void successListener(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorListener(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openSerialPort(SerialPort serialPort) {
        logBeans.add(new LogBean(System.currentTimeMillis(), "打开串口成功"));
        this.serialPort = serialPort;
        switch (sort) {
            case "metal":
                sendCode = weightCode;
                break;
            case "plastics":
                sendCode = weightCode;
                break;
            case "textile":
                sendCode = weightCode;
                break;
            case "bottle":
                sendCode = weightCode;
                break;
            case "paper":
                sendCode = weightCode;
                break;
            case "glass":
                sendCode = code;
                break;
            case "waste":
                sendCode = code;
                break;
        }
        mPresenter.sendSerialPort(sendCode);
        mPresenter.readData();
        logBeans.add(new LogBean(System.currentTimeMillis(), "发送获取箱内数据的指令为" + weightCode));
        SpUtil.putList(this, "logs", logBeans);
        upLoadLog(new Gson().toJson(new LogBean(System.currentTimeMillis(), "发送获取箱内数据的指令为" + weightCode)));

    }

    @Override
    public void closeSerialPort(String msg) {

    }

    @Override
    public void readDataSuccess(String sData) {

        logBeans.add(new LogBean(System.currentTimeMillis(), "扫码成功下位机返回的字符串 == " + sData));
        String newStr = sData.trim();
        String stateStr = newStr.substring(0, 6);
        Log.e("aaa",
                "(ScanCodeActivity.java:252)<--stateStr-->" + stateStr);
        if (stateStr.equals("01050C")) {
            mPresenter.startActivity();
        } else if (stateStr.equals("01050c")) {
            mPresenter.startActivity();
        } else {
            logBeans.add(new LogBean(System.currentTimeMillis(), "记录当前箱内数据"));
            value = AnalyseDataUtil.analyseData(newStr, sort);
            CalfApplication.value = ScanCodeActivity.this.value;
            timeOutCount = 0;
            sendCode = code;
            mPresenter.sendSerialPort(sendCode);//开箱命令
            mPresenter.readData();

            logBeans.add(new LogBean(System.currentTimeMillis(), "发送开箱命令 ==  " + sendCode));
            SpUtil.putList(this, "logs", logBeans);
            upLoadLog(new Gson().toJson(logBeans));
        }
        Log.e("aaa",
                "(ScanCodeActivity.java:229)<--接收到的字符串为-->" + sData);
    }

    @Override
    public void readDataError() {

        Log.e("aaa",
                "(ScanCodeActivity.java:281)<--读取数据失败-->");

        timeOutCount++;
        if (timeOutCount < 10000) {
            mPresenter.readData();
        } else {
            Toast.makeText(this, "读取下位机数据失败", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public SerialPort vertifySerialPort() {

        return this.serialPort;
    }

    @Override
    public SerialPortUtils senSPUtils() {
        return serialPortUtils;
    }

    @Override
    public String getCodeText() {
        return "{\"time\":\"1539329142\",\"device\":\"1234567890\"}";
    }

    @Override
    public Context getContext() {
        return ScanCodeActivity.this;
    }

    @Override
    public void showLoading() {

        try {
            if (dialog.isShowing()) {
//
            } else {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideLoading() {
        try {
            if (dialog.isShowing())
                dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getTimeSuccess(String string) {
        //{"time":"string","device_id":"deviceId","adminLoginCode":"-1"}
        imgStr = "{\"time\":\"" + string + "\",\"device_id\":\"" + deviceUuid + "\",\"adminLoginCode\":\"-1\"}";
        Log.e("aaa",
                "(ScanCodeActivity.java:197)<---->" + imgStr);
        ivCode.setImageBitmap(mPresenter.setCodeImage(imgStr));
    }

    @Override
    public void getTimeError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getDeviceId() {
        return deviceUuid;
//        return "1d5d5965-3394-35fe-bd30-7c0033a8c6c1";

    }

    @Override
    public void startActivitySuccess() {
        countDownTimerUtils.cancel();
        if (countDownTimerUtils != null)
            countDownTimerUtils = null;
        Intent intent = new Intent(ScanCodeActivity.this, ScanSuccessActivity.class);
        intent.putExtra("bean", beans);
        intent.putExtra("sort", sort);
        SpUtil.putList(ScanCodeActivity.this, "logs", logBeans);
        upLoadLog(new Gson().toJson(logBeans));
        startActivity(intent);
        finish();
    }

    @Override
    public void startActivityError() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("scancode", "run onstop() function");
        mPresenter.closeSerialPort();

        Log.e("scancode", "run onstop() function over");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("scancode", "run ondestroy() function");
        soundPool.release();
        soundPool = null;
        timer.cancel();
        timer = null;
        if (countDownTimerUtils != null)
            countDownTimerUtils.cancel();

        // TODO: 2018/9/27 关闭串口
        mPresenter.closeSerialPort();

        Log.e("scancode", "run ondestroy() function over");

    }

    public void test() {
        Gson gson = new Gson();
        beans = gson.fromJson("{\"result\":1,\"msg\":\"success\",\"data\":\n" +
                "{\"id\":1,\"device_id\":\"1234567890\",\"device_state\":\"1\",\n" +
                "\"device_dress\":\"xxx\",\"user_id\":8}}", ScanCodeStateBean.class);
//        Intent intent = new Intent(this, ScanSuccessActivity.class);
//        intent.putExtra("sort", sort);
//        intent.putExtra("bean", scanCodeStateBean);
//        startActivity(intent);
        mPresenter.startActivity();
    }

}
