package com.dfxh.wang.serialport_test.view;

import android.content.Context;

import android_serialport_api.SerialPort;
import utils.SerialPortUtils;

public interface DeliverView {

    void upLoadSuccess(String msg);

    void upLoadError(String msg);

    void showLoading();

    void hideLoading();


    String getDeviceId();


    void successListener(String msg);

    void errorListener(String msg);

    void readDataSuccess(String sData);

    void readDataError();

    /**
     * 打开串口
     */
    void openSerialPort(SerialPort sp);

    /**
     * 关闭串口
     */
    void closeSerialPort(String msg);

    SerialPort vertifySerialPort();

    SerialPortUtils senSPUtils();

    Context getContext();

    String getUserId();
}
