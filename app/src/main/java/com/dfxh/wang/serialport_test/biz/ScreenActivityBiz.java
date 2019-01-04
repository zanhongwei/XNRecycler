package com.dfxh.wang.serialport_test.biz;

import com.dfxh.wang.serialport_test.listeners.CloseSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.OpenSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.ReadDataWithSpListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;
import com.dfxh.wang.serialport_test.listeners.VerifyScanCodeListener;

import android_serialport_api.SerialPort;
import utils.SerialPortUtils;

public interface ScreenActivityBiz {

    /**
     * 打开串口
     *
     * @param serialPortUtils
     * @param listener
     */
    void openSerialPort(SerialPortUtils serialPortUtils, OpenSerialPortListener listener);


    /**
     * 关闭串口
     *
     * @param serialPortUtils
     * @param serialPort
     * @param listener
     */
    void closeSp(SerialPortUtils serialPortUtils, SerialPort serialPort, CloseSerialPortListener listener);


    /**
     * 验证串口状态
     *
     * @param deviceId
     * @param listener
     */
    void verifySP(String deviceId, VerifyScanCodeListener listener);


    /**
     * 向下位机发送数据
     *
     * @param serialPortUtils
     * @param serialPort
     * @param data
     * @param listener
     */
    void sendDataWithSP(SerialPortUtils serialPortUtils, SerialPort serialPort, String data, SendListener listener);


    /**
     * 读取下位机返回数据
     *
     * @param serialPortUtils
     * @param serialPort
     * @param listener
     */
    void readSerialData(SerialPortUtils serialPortUtils, SerialPort serialPort, ReadDataWithSpListener listener);


    void testMethod(boolean onoff, SendListener listener);

    void upLoadWithoutData(String device_id, UpLoadListener loadListener);

}
