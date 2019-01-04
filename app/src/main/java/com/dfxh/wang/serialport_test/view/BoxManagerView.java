package com.dfxh.wang.serialport_test.view;

import android_serialport_api.SerialPort;
import utils.SerialPortUtils;

public interface BoxManagerView {


    String getCode();

    String getSort();

    void startActivitySuccess();

    void startActivityError();

    void showLoading();

    void hideLoading();

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

    void successListener(String msg);

    void errorListener(String msg);
}
