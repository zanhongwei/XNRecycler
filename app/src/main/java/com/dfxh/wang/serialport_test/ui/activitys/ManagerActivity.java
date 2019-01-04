package com.dfxh.wang.serialport_test.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dfxh.wang.serialport_test.BaseActivity;
import com.dfxh.wang.serialport_test.CalfApplication;
import com.dfxh.wang.serialport_test.R;
import com.dfxh.wang.serialport_test.presenter.ManagerPresenter;
import com.dfxh.wang.serialport_test.view.ManagerActivityView;

import java.text.DecimalFormat;

import android_serialport_api.SerialPort;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.SerialPortUtils;

public class ManagerActivity extends BaseActivity implements ManagerActivityView {

    @BindView(R.id.tv_tb_phone)
    TextView tvTbPhone;
    @BindView(R.id.tv_company_num)
    TextView tvCompanyNum;
    @BindView(R.id.btn_open_metal)
    Button btnOpenMetal;
    @BindView(R.id.btn_open_plastics)
    Button btnOpenPlastics;
    @BindView(R.id.btn_open_textile)
    Button btnOpenTextile;
    @BindView(R.id.btn_open_bottle)
    Button btnOpenBottle;
    @BindView(R.id.btn_open_paper)
    Button btnOpenPaper;
    @BindView(R.id.btn_open_waste)
    Button btnOpenWaste;
    @BindView(R.id.btn_open_glass)
    Button btnOpenGlass;
    @BindView(R.id.tv_text)
    TextView tvText;
    private ManagerPresenter mPresenter;
    private SerialPort serialPort;
    private SerialPortUtils serialPortUtils;
    private String flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        ButterKnife.bind(this);

        serialPortUtils = new SerialPortUtils();
        mPresenter = new ManagerPresenter(this);
    }

    private void sendCode() {
        switch (flag) {
            case "metal":
                mPresenter.senData(getResources().getString(R.string.re_metal_open));
                break;
            case "plastic":
                mPresenter.senData(getResources().getString(R.string.re_plastics_open));
                break;
            case "textile":
                mPresenter.senData(getResources().getString(R.string.re_textile_open));
                break;
            case "bottle":
                mPresenter.senData(getResources().getString(R.string.re_bottle_open));
                break;
            case "paper":
                mPresenter.senData(getResources().getString(R.string.re_paper_open));
                break;
            case "waste":
                mPresenter.senData(getResources().getString(R.string.re_waste_open));
                break;
            case "glass":
                mPresenter.senData(getResources().getString(R.string.re_glass_open));
                break;
            default:
                Toast.makeText(this, "命令错误", Toast.LENGTH_SHORT).show();
                break;
        }

        mPresenter.readData();
    }

    @OnClick({R.id.iv_right, R.id.btn_open_metal, R.id.btn_open_plastics, R.id.btn_open_textile, R.id.btn_open_bottle,
            R.id.btn_open_paper, R.id.btn_open_waste, R.id.btn_open_glass, R.id.btn_log, R.id.btn_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                startActivity(new Intent(this, BoxManagerActivity.class));
                finish();
                break;
            case R.id.btn_open_metal:
                flag = "metal";
                mPresenter.openSerialPort();
                break;
            case R.id.btn_open_plastics:
                flag = "plastic";
                mPresenter.openSerialPort();
                break;
            case R.id.btn_open_textile:
                flag = "textile";
                mPresenter.openSerialPort();
                break;
            case R.id.btn_open_bottle:
                flag = "bottle";
                mPresenter.openSerialPort();
                break;
            case R.id.btn_open_paper:
                flag = "paper";
                mPresenter.openSerialPort();
                break;
            case R.id.btn_open_waste:
                flag = "waste";
                mPresenter.openSerialPort();
                break;
            case R.id.btn_open_glass:
                flag = "glass";
                mPresenter.openSerialPort();
                break;
            case R.id.btn_log:
                startActivity(new Intent(this, LogRecordActivity.class));
                finish();
                break;
            case R.id.btn_out:
                CalfApplication.getInstance().exit();
//                calculate();
                break;
        }
    }

    private void calculate() {
        String weight = "3FA07728";
        float metalF1 = Float.intBitsToFloat(Integer.valueOf(weight, 16));
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String metalWeight1 = decimalFormat.format(metalF1);
        tvText.setText(metalWeight1);
    }

    @Override
    public void sendSpDataSuccess(String msg) {
        Log.e("aaa",
                "(ManagerActivity.java:90)<--发送数据成功返回-->" + msg);
    }

    @Override
    public void sendSpDataError(String msg) {
        Log.e("aaa",
                "(ManagerActivity.java:97)<--发送数据失败的返回-->" + msg);
    }

    @Override
    public SerialPort vertifySerialPort() {
        return serialPort;
    }

    @Override
    public SerialPortUtils senSPUtils() {
        return this.serialPortUtils;
    }

    @Override
    public void openSerialPortsError(String msgError) {
        Toast.makeText(this, "串口打开失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void readDataSuccess(String sData) {
        if (TextUtils.isEmpty(sData)) {
            Toast.makeText(this, "数据返回异常，请检查设备", Toast.LENGTH_SHORT).show();
        } else {
            String newStr = sData.trim();
            String stateStr = newStr.substring(0, 6);
            if (stateStr.contains("01050C")) {

            } else if (sData.contains("01050c")) {

            } else {
                Toast.makeText(this, "数据返回异常，请检查设备", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void readDataError() {
        Log.e("aaa",
                "(ManagerActivity.java:126)<--读取下位机返回失败-->");
    }

    @Override
    public void openSerialPort(SerialPort sp) {
        this.serialPort = sp;

        sendCode();
    }

    @Override
    public void closeSerialPort(String msg) {
        Log.e("aaa",
                "(ManagerActivity.java:137)<---->" + "关闭串口成功");
    }

    @Override
    public void closeSeialError(String msgErro) {
        Log.e("aaa",
                "(ManagerActivity.java:143)<---->" + "关闭串口失败");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.closeSerialPorts();
    }
}
