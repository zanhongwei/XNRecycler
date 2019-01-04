package com.dfxh.wang.serialport_test.model;

import android.text.TextUtils;
import android.util.Log;

import com.dfxh.wang.serialport_test.biz.ManagerBiz;
import com.dfxh.wang.serialport_test.listeners.CloseSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.OpenSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;

import android_serialport_api.SerialPort;
import utils.SerialPortUtils;

/**
 * Describe 回收员开箱操作类
 * Create by axehome_Mr.Z
 * Date on 2018/11/22 14:32
 */
public class ManagerModel implements ManagerBiz {

    private SerialPort serialPort;

    @Override
    public void senSpData(SerialPort serialPort, SerialPortUtils serialPortUtils, String spData, SendListener listener) {
        if (serialPort != null) {
            serialPortUtils.sendSerialPort(spData);
            listener.sendSuccess("发送成功");
        } else {
            listener.sendError("打开串口失败");
        }
    }

    @Override
    public void openSerialPort(SerialPortUtils serialPortUtils, OpenSerialPortListener listener) {
        serialPort = serialPortUtils.openSerialPort();
        if (serialPort == null) {
            listener.openSerialPortError("串口打开失败");
        } else {
            listener.openSerialPortSuccess(serialPort);
        }
    }

    @Override
    public void closeSerialPort(SerialPortUtils serialPortUtils, SerialPort serialPort, CloseSerialPortListener listener) {
        if (serialPort != null)
            serialPortUtils.closeSerialPort();
        listener.closeSuccess("关闭串口成功");
    }

    @Override
    public void readSerialProt(SerialPort serialPort, SerialPortUtils serialPortUtils, SendListener listener) {
        String sData = serialPortUtils.readSerialPort();
        Log.e("aaa",
                "(ManagerModel.java:54)<---->" + sData);
        if (TextUtils.isEmpty(sData)) {
            listener.sendError("");
        } else {
            listener.sendSuccess(sData);
        }
    }
}
