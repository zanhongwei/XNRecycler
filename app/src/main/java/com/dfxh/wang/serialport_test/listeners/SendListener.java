package com.dfxh.wang.serialport_test.listeners;

public interface SendListener {

    void sendSuccess(String msg);

    void sendError(String errorMsg);

}
