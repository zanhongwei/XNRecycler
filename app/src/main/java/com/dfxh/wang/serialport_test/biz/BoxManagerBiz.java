package com.dfxh.wang.serialport_test.biz;

import com.dfxh.wang.serialport_test.listeners.CloseSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.OpenSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;

import android_serialport_api.SerialPort;
import utils.SerialPortUtils;

/**
 * Created by Axehome_Mr.z on 2018/12/4 9:31
 * $Describe
 */
public interface BoxManagerBiz {

    void sendSerialPort(SerialPortUtils serialPortUtils, SerialPort serialPort, String data, SendListener listener);

    void openSerialPort(SerialPortUtils serialPortUtils, OpenSerialPortListener listener);

    void closeSerialPort(SerialPortUtils serialPortUtils, SerialPort serialPort, CloseSerialPortListener listener);

    void readSerialData(SerialPortUtils serialPortUtils, SerialPort serialPort, SendListener listener);

    void sendShortMessage(String num, SendListener listener);


}
