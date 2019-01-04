package com.dfxh.wang.serialport_test.biz;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.Time;

import com.dfxh.wang.serialport_test.listeners.CloseSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.OpenSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.QRCodeGenderListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;
import com.dfxh.wang.serialport_test.listeners.StartActivityListener;
import com.dfxh.wang.serialport_test.listeners.TimeStempListener;
import com.dfxh.wang.serialport_test.listeners.VerifyScanCodeListener;

import android_serialport_api.SerialPort;
import utils.SerialPortUtils;

public interface ScanCodeBiz {

    void sendSerialPort(SerialPortUtils serialPortUtils, SerialPort serialPort, String data, SendListener listener);

    void openSerialPort(SerialPortUtils serialPortUtils, OpenSerialPortListener listener);

    void closeSerialPort(SerialPortUtils serialPortUtils, SerialPort serialPort, CloseSerialPortListener listener);

    Bitmap setQRCodeImage(String text, Context context);

    void getTimeStemp(TimeStempListener listener);

    void verifyScanCodeState(String deviceId, VerifyScanCodeListener listener);

    void upLoadData(String userId, String goods_type, String goods_price, String goods_weight, String device_id, String goods_money, UpLoadListener loadListener);

    void upLoadWithoutData(String device_id, UpLoadListener loadListener);

    void readSerialData(SerialPortUtils serialPortUtils, SerialPort serialPort, SendListener listener);

    void startActivity(StartActivityListener listener);


}
