package com.dfxh.wang.serialport_test.listeners;

import android_serialport_api.SerialPort;

public interface OpenSerialPortListener {


    void openSerialPortSuccess(SerialPort serialPort);

    void openSerialPortError(String errorMsg);
}
