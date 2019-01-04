package com.dfxh.wang.serialport_test.view;

import android.content.Context;

import com.dfxh.wang.serialport_test.beans.ScanCodeStateBean;

import android_serialport_api.SerialPort;
import utils.SerialPortUtils;

public interface ScanCodeView {

    void ScanCodeSuccess(ScanCodeStateBean bean);

    void ScanCodeError(String errorStr);

    void successListener(String msg);

    void errorListener(String msg);

    /**
     * 打开串口
     */
    void openSerialPort(SerialPort sp);

    /**
     * 关闭串口
     */
    void closeSerialPort(String msg);

    void readDataSuccess(String sData);

    void readDataError();

    SerialPort vertifySerialPort();

    SerialPortUtils senSPUtils();

    /**
     * 获取生成二维码的字段
     *
     * @return
     */
    String getCodeText();

    Context getContext();

    void showLoading();


    void hideLoading();

    void getTimeSuccess(String string);

    void getTimeError(String msg);

    String getDeviceId();

    void startActivitySuccess();

    void startActivityError();

}
