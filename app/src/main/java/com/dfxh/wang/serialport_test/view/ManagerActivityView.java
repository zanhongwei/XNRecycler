package com.dfxh.wang.serialport_test.view;

import android_serialport_api.SerialPort;
import utils.SerialPortUtils;

/**
 * Describe $
 * Create by axehome_Mr.Z
 * Date on 2018/11/22 14:56
 */
public interface ManagerActivityView {


    void sendSpDataSuccess(String msg);

    void sendSpDataError(String msg);

    SerialPort vertifySerialPort();

    SerialPortUtils senSPUtils();

    void openSerialPortsError(String msgError);

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

    void closeSeialError(String msgErro);

}
