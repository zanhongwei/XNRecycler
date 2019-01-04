package com.dfxh.wang.serialport_test.biz;

import com.dfxh.wang.serialport_test.listeners.CloseSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.OpenSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;

import android_serialport_api.SerialPort;
import utils.SerialPortUtils;

/**
 * Describe 管理员回收控制操作类
 * Create by axehome_Mr.Z
 * Date on 2018/11/22 14:21
 */
public interface ManagerBiz {

    /**
     * 向下位机发送数据
     *
     * @param serialPort
     * @param serialPortUtils
     * @param spData
     * @param listener
     */
    void senSpData(SerialPort serialPort, SerialPortUtils serialPortUtils, String spData, SendListener listener);

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
    void closeSerialPort(SerialPortUtils serialPortUtils, SerialPort serialPort, CloseSerialPortListener listener);

    /**
     * 读取下位机返回的数据
     *
     * @param serialPort
     * @param serialPortUtils
     * @param listener
     */
    void readSerialProt(SerialPort serialPort, SerialPortUtils serialPortUtils, SendListener listener);

}
