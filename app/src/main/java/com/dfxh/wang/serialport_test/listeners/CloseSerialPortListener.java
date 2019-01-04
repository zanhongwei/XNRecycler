package com.dfxh.wang.serialport_test.listeners;

public interface CloseSerialPortListener {

    void closeSuccess(String msg);

    void closeError(String msg);
}
