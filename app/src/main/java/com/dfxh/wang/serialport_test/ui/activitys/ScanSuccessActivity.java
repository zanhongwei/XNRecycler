package com.dfxh.wang.serialport_test.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.dfxh.wang.serialport_test.BaseActivity;
import com.dfxh.wang.serialport_test.CalfApplication;
import com.dfxh.wang.serialport_test.R;
import com.dfxh.wang.serialport_test.beans.LogBean;
import com.dfxh.wang.serialport_test.beans.ScanCodeStateBean;
import com.dfxh.wang.serialport_test.presenter.ScanCodeActivityPresenter;
import com.dfxh.wang.serialport_test.presenter.ScanCodeSuccessActivityPresenter;
import com.dfxh.wang.serialport_test.utils.AnalyseDataUtil;
import com.dfxh.wang.serialport_test.utils.BytesHexStrTranslate;
import com.dfxh.wang.serialport_test.utils.CountDownTimerUtils;
import com.dfxh.wang.serialport_test.utils.DeviceUuidFactory;
import com.dfxh.wang.serialport_test.utils.SpUtil;
import com.dfxh.wang.serialport_test.view.DeliverView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import android_serialport_api.SerialPort;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import utils.SerialPortUtils;

public class ScanSuccessActivity extends BaseActivity implements DeliverView {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_flow)
    ImageView ivFlow;
    @BindView(R.id.tv_tb_phone)
    TextView tvTbPhone;
    @BindView(R.id.tv_company_num)
    TextView tvCompanyNum;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_success)
    ImageView ivSuccess;
    @BindView(R.id.btn_go_on)
    Button btnGoOn;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    @BindView(R.id.btn_complete)
    Button btnComplete;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_content_s)
    TextView tvContentS;
    @BindView(R.id.tv_content_s_money)
    TextView tvContentSMoney;
    @BindView(R.id.ll_content_s)
    LinearLayout llContentS;
    private Unbinder bind;
    private String state;
    private Thread getData;
    private ScanCodeStateBean bean;
    private String sort;

    private SerialPortUtils serialPortUtils;
    private SerialPort serialPort;
    //    private Handler handler2;
    private byte[] mBuffer;
    private ScanCodeSuccessActivityPresenter mPresenter;
    private LoadingDailog dialog;
    private String deviceUuid;
    private double value;//重量
    private String money;
    private double unitPrice;
    private List<LogBean> logs;
    private boolean needUpLoad = false;
    private String code2 = "010305F2000D2530";//称重
    private String code1;
    private long timeOutCount;
    private SoundPool soundPool;
    private int load;
    private String flag = "";
    private CountDownTimerUtils countDownTimerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("aaa",
                "(ScanSuccessActivity.java:97)<--进入OnCreate()方法-->");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_success);
        bind = ButterKnife.bind(this);
        sort = getIntent().getStringExtra("sort");
        bean = (ScanCodeStateBean) getIntent().getSerializableExtra("bean");
        logs = SpUtil.getList(this, "logs");
        state = "0";

        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                .setMessage("加载中...")
                .setCancelable(true)
                .setCancelOutside(false);
        dialog = loadBuilder.create();

        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(this);
        deviceUuid = deviceUuidFactory.getDeviceUuid().toString();
        serialPortUtils = new SerialPortUtils("success");
        mPresenter = new ScanCodeSuccessActivityPresenter(this);
        if (serialPort == null)
            mPresenter.openSerialPort();

        soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 5);
        //加载资源
        load = soundPool.load(this, R.raw.put_complete, 1);

        countDownTimerUtils = new CountDownTimerUtils(tvTime, 119000, 1000, new CountDownTimerUtils.MyListener() {

            @Override
            public void refresh() {

                finishSend("1");
                if (sort.equals("glass")||sort.equals("waste")){
                    mPresenter.uploadData(sort, String.valueOf("0"), "0", "0");

                }else {
                    mPresenter.uploadData(sort, String.valueOf(unitPrice), String.valueOf(value - CalfApplication.value), money);

                }
//                startActivity(new Intent(ScanSuccessActivity.this, ScreenActivity.class));
//                finish();
            }
        });
        countDownTimerUtils.start();
    }

    private void changeView() {

        if (state.equals("0")) {
            tvTitle.setVisibility(View.GONE);
            ivFlow.setVisibility(View.GONE);

            ivSuccess.setVisibility(View.VISIBLE);
            llContent.setVisibility(View.VISIBLE);

        }

    }

    @OnClick({R.id.btn_go_on, R.id.btn_finish, R.id.iv_right, R.id.btn_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                break;
            case R.id.btn_complete:
                finishSend("0");
//                finishSendTest();

                break;
            case R.id.btn_go_on:
                flag = "0";
                if (countDownTimerUtils != null) countDownTimerUtils.cancel();
                if (sort.equals("glass") || sort.equals("waste")) {
                    mPresenter.uploadData(sort, "0", "0", "0");
                } else
                    mPresenter.uploadData(sort, String.valueOf(unitPrice), String.valueOf(value - CalfApplication.value), money);
                break;
            case R.id.btn_finish:
                if (countDownTimerUtils != null) countDownTimerUtils.cancel();
                flag = "1";
                if (sort.equals("glass") || sort.equals("waste")) {
                    mPresenter.uploadData(sort, "0", "0", "0");
//                    goonTest();
                } else
                    mPresenter.uploadData(sort, String.valueOf(unitPrice), String.valueOf(value - CalfApplication.value), money);
//                    goonTest();
                break;
        }
    }

    private void goonTest() {
        mPresenter.upLoadWithoutData();
    }

    private void finishSend(String flogan) {

        if (flogan.equals("0")){
            soundPool.play(load, 1, 1, 1, 0, 1);
//            state = "1";
            llContent.setVisibility(View.GONE);
            llContentS.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.GONE);
            ivFlow.setVisibility(View.GONE);

            if (sort.equals("glass") || sort.equals("waste")) {
                llContentS.setVisibility(View.GONE);
            }

            btnComplete.setVisibility(View.GONE);
            btnGoOn.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.VISIBLE);
            ivSuccess.setVisibility(View.VISIBLE);
        }else {

        }

//        if (countDownTimerUtils != null) countDownTimerUtils.cancel();
//        soundPool.play(load, 1, 1, 1, 0, 1);
        state = "1";
//        llContent.setVisibility(View.GONE);
//        llContentS.setVisibility(View.VISIBLE);
//        tvTitle.setVisibility(View.GONE);
//        ivFlow.setVisibility(View.GONE);

//        if (sort.equals("glass") || sort.equals("waste")) {
//            llContentS.setVisibility(View.GONE);
//        }

//        btnComplete.setVisibility(View.GONE);
//        btnGoOn.setVisibility(View.VISIBLE);
//        btnFinish.setVisibility(View.VISIBLE);
//        ivSuccess.setVisibility(View.VISIBLE);

        code1 = "";
        switch (sort) {
            case "metal":
                unitPrice = 0.60;
                timeOutCount = 0;
                code1 = getResources().getString(R.string.metal_close);
                mPresenter.sendSerialPort(code1);
                mPresenter.readData();
                logs.add(new LogBean(System.currentTimeMillis(), "发送读取金属重量和关箱命令"));
                break;
            case "plastics":
                unitPrice = 0.50;
                timeOutCount = 0;
                code1 = getResources().getString(R.string.plastics_close);
                mPresenter.sendSerialPort(code1);
                mPresenter.readData();
                logs.add(new LogBean(System.currentTimeMillis(), "发送读取塑料重量和关箱命令"));
                break;
            case "textile":
                unitPrice = 0.60;
                timeOutCount = 0;
                code1 = getResources().getString(R.string.textile_close);
                mPresenter.sendSerialPort(code1);
                mPresenter.readData();
                logs.add(new LogBean(System.currentTimeMillis(), "发送读取纺织物重量和关箱命令"));
                break;
            case "paper":
                unitPrice = 0.60;
                timeOutCount = 0;
                code1 = getResources().getString(R.string.paper_close);
                mPresenter.sendSerialPort(code1);
                mPresenter.readData();
                logs.add(new LogBean(System.currentTimeMillis(), "发送读取纸张重量和关箱命令"));
                break;
            case "glass":
                timeOutCount = 0;
                code1 = getResources().getString(R.string.glass_close);
                mPresenter.sendSerialPort(code1);
                mPresenter.readData();
                logs.add(new LogBean(System.currentTimeMillis(), "发送玻璃关箱命令"));
                break;
            case "waste":
                timeOutCount = 0;
                code1 = getResources().getString(R.string.waste_close);
                mPresenter.sendSerialPort(code1);//
                mPresenter.readData();
                logs.add(new LogBean(System.currentTimeMillis(), "发送垃圾关箱命令"));
                break;
            case "bottle":
                timeOutCount = 0;
                code1 = getResources().getString(R.string.bottle_close);
                mPresenter.sendSerialPort(code1);//
                mPresenter.readData();
                tvUnit.setText("个");
                logs.add(new LogBean(System.currentTimeMillis(), "发送读取瓶子重量和关箱命令"));
                break;
        }

        SpUtil.putList(this, "logs", logs);

    }

    private void finishSendTest() {

        soundPool.play(load, 1, 1, 1, 0, 1);
        state = "1";
        llContent.setVisibility(View.GONE);
        llContentS.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
        ivFlow.setVisibility(View.GONE);

        if (sort.equals("glass") || sort.equals("waste")) {
            llContentS.setVisibility(View.GONE);
        }

        btnComplete.setVisibility(View.GONE);
        btnGoOn.setVisibility(View.VISIBLE);
        btnFinish.setVisibility(View.VISIBLE);
        ivSuccess.setVisibility(View.VISIBLE);

        code1 = "";
        switch (sort) {
            case "metal":
                unitPrice = 0.60;
                timeOutCount = 0;
                code1 = getResources().getString(R.string.metal_close);
//                mPresenter.sendSerialPort(code1);
//                mPresenter.readData();
                logs.add(new LogBean(System.currentTimeMillis(), "发送读取金属重量和关箱命令"));
                break;
            case "plastics":
                unitPrice = 0.50;
                timeOutCount = 0;
                code1 = getResources().getString(R.string.plastics_close);
//                mPresenter.sendSerialPort(code1);
//                mPresenter.readData();
                logs.add(new LogBean(System.currentTimeMillis(), "发送读取塑料重量和关箱命令"));
                break;
            case "textile":
                unitPrice = 0.60;
                timeOutCount = 0;
                code1 = getResources().getString(R.string.textile_close);
//                mPresenter.sendSerialPort(code1);
//                mPresenter.readData();
                logs.add(new LogBean(System.currentTimeMillis(), "发送读取纺织物重量和关箱命令"));
                break;
            case "paper":
                unitPrice = 0.60;
                timeOutCount = 0;
                code1 = getResources().getString(R.string.paper_close);
//                mPresenter.sendSerialPort(code1);
//                mPresenter.readData();
                logs.add(new LogBean(System.currentTimeMillis(), "发送读取纸张重量和关箱命令"));
                break;
            case "glass":
                timeOutCount = 0;
                code1 = getResources().getString(R.string.glass_close);
//                mPresenter.sendSerialPort(code1);
//                mPresenter.readData();
                logs.add(new LogBean(System.currentTimeMillis(), "发送玻璃关箱命令"));
                break;
            case "waste":
                timeOutCount = 0;
                code1 = getResources().getString(R.string.waste_close);
//                mPresenter.sendSerialPort(code1);//
//                mPresenter.readData();
                logs.add(new LogBean(System.currentTimeMillis(), "发送垃圾关箱命令"));
                break;
            case "bottle":
                timeOutCount = 0;
                code1 = getResources().getString(R.string.bottle_close);
//                mPresenter.sendSerialPort(code1);//
//                mPresenter.readData();
                tvUnit.setText("个");
                logs.add(new LogBean(System.currentTimeMillis(), "发送读取瓶子重量和关箱命令"));
                break;
        }

        SpUtil.putList(this, "logs", logs);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("scanSuccess", "run onStop() function");
        mPresenter.closeSerialPort();
        serialPortUtils = null;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("scanSuccess", "run onDestroy() function");
        bind.unbind();
        soundPool.release();
        soundPool = null;

        if (countDownTimerUtils != null) countDownTimerUtils.cancel();
        countDownTimerUtils = null;

        Log.e("scanSuccess", "run onStop() function over");
    }


    @Override
    public void upLoadSuccess(String msg) {
        Log.e("aaa",
                "(ScanSuccessActivity.java:284)<---->" + msg);

        if (countDownTimerUtils != null) {
            countDownTimerUtils.cancel();
            countDownTimerUtils = null;
        }
        if (flag.equals("0")) {
            startActivity(new Intent(this, BoxManagerActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, ScreenActivity.class));
            finish();
        }


    }


    @Override
    public void upLoadError(String msg) {
        Log.e("aaa",
                "(ScanSuccessActivity.java:284)<---->" + msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {

        try {

            if (dialog.isShowing()) {

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
            if (dialog.isShowing()) dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    @Override
    public String getDeviceId() {
        return deviceUuid;
    }

    @Override
    public void successListener(String msg) {

    }

    @Override
    public void errorListener(String msg) {

    }

    @Override
    public void readDataSuccess(String sData) {
//        String str = BytesHexStrTranslate.bytesToHexFun1(mBuffer);
        Log.e("aaa",
                "(ScanSuccessActivity.java:287)<---->" + sData);
        logs.add(new LogBean(System.currentTimeMillis(), "获取下位机返回的字符串 == " + sData));
        SpUtil.putList(ScanSuccessActivity.this, "logs", logs);
        String newStr = sData.trim();
        switch (sort) {
            case "metal":
                if (newStr.substring(0, 6).equals("01050C")) {
                    mPresenter.sendSerialPort(code2);
                    mPresenter.readData();
                } else if (newStr.substring(0, 6).equals("01050c")) {
                    mPresenter.sendSerialPort(code2);
                    mPresenter.readData();
                } else {
                    value = AnalyseDataUtil.analyseData(newStr, sort);
                    money = AnalyseDataUtil.calculateData(0.60, newStr, sort);
                    Log.e("aaa", "476readDataSuccess: "+value );
                    Log.e("aaa", "476readDataSuccess: "+CalfApplication.value);
                    Log.e("aaa","476"+String.valueOf(value - CalfApplication.value));

                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    String metalWeight1 = decimalFormat.format(value - CalfApplication.value);
                    tvContentS.setText(metalWeight1);
                    tvContentSMoney.setText(String.valueOf(money));
                }

                break;
            case "plastics":
                if (newStr.substring(0, 6).equals("01050C")) {
                    mPresenter.sendSerialPort(code2);
                    mPresenter.readData();
                } else if (newStr.substring(0, 6).equals("01050c")) {
                    mPresenter.sendSerialPort(code2);
                    mPresenter.readData();
                } else {
                    value = AnalyseDataUtil.analyseData(newStr, sort);
                    money = AnalyseDataUtil.calculateData(0.50, newStr, sort);

                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    String metalWeight1 = decimalFormat.format(value - CalfApplication.value);
                    tvContentS.setText(metalWeight1);

//                    tvContentS.setText(String.valueOf(value - CalfApplication.value));
                    tvContentSMoney.setText(String.valueOf(money));
                }

                break;
            case "textile":
                if (newStr.substring(0, 6).equals("01050C")) {
                    mPresenter.sendSerialPort(code2);
                    mPresenter.readData();
                } else if (newStr.substring(0, 6).equals("01050c")) {
                    mPresenter.sendSerialPort(code2);
                    mPresenter.readData();
                } else {

                    value = AnalyseDataUtil.analyseData(newStr, sort);
                    money = AnalyseDataUtil.calculateData(0.60, newStr, sort);
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    String metalWeight1 = decimalFormat.format(value - CalfApplication.value);
                    tvContentS.setText(metalWeight1);
//                    tvContentS.setText(String.valueOf(value - CalfApplication.value));
                    tvContentSMoney.setText(String.valueOf(money));

                    Log.e("aaa", "readDataSuccess: "+CalfApplication.value);
                    Log.e("aaa",String.valueOf(value - CalfApplication.value));
                }
                break;
            case "paper":
                if (newStr.substring(0, 6).equals("01050C")) {
                    mPresenter.sendSerialPort(code2);
                    mPresenter.readData();
                } else if (newStr.substring(0, 6).equals("01050c")) {
                    mPresenter.sendSerialPort(code2);
                    mPresenter.readData();
                } else {
                    value = AnalyseDataUtil.analyseData(newStr, sort);
                    money = AnalyseDataUtil.calculateData(0.90, newStr, sort);
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    String metalWeight1 = decimalFormat.format(value - CalfApplication.value);
                    tvContentS.setText(metalWeight1);
//                    tvContentS.setText(String.valueOf(value - CalfApplication.value));
                    tvContentSMoney.setText(String.valueOf(money));
                }
                break;
            case "bottle":
                if (newStr.substring(0, 6).equals("01050C")) {
                    mPresenter.sendSerialPort(code2);
                    mPresenter.readData();
                } else if (newStr.substring(0, 6).equals("01050c")) {
                    mPresenter.sendSerialPort(code2);
                    mPresenter.readData();
                    return;
                } else {
                    value = AnalyseDataUtil.analyseData(newStr, sort);
                    money = AnalyseDataUtil.calculateData(0.1, newStr, sort);
                    tvContentS.setText(String.valueOf(value));
                    tvContentSMoney.setText(String.valueOf(money));
                }
                break;
            case "glass":

                break;
            case "waste":

                break;
        }


        Log.e("aaa",
                "(ScanSuccessActivity.java:284)<---->" + money);
        Log.e("aaa",
                "(ScanSuccessActivity.java:286)<---->" + value);

    }

    @Override
    public void readDataError() {

        Log.e("aaa",
                "(ScanCodeActivity.java:281)<--读取数据失败-->");
        timeOutCount++;
        if (timeOutCount < 10000) {
            mPresenter.readData();
        } else {
//            handler2.sendEmptyMessage(2);
            Toast.makeText(this, "读取下位机数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openSerialPort(SerialPort sp) {
        this.serialPort = sp;
    }

    @Override
    public void closeSerialPort(String msg) {

    }

    @Override
    public SerialPort vertifySerialPort() {
        return this.serialPort;
    }

    @Override
    public SerialPortUtils senSPUtils() {
        return this.serialPortUtils;
    }

    @Override
    public Context getContext() {
        return ScanSuccessActivity.this;
    }

    @Override
    public String getUserId() {
        int user_id = bean.getData().getUser_id();
        Log.e("aaa",
                "(ScanSuccessActivity.java:428)<--userId-->" + user_id);
        return String.valueOf(user_id);
    }


}
