package com.dfxh.wang.serialport_test.presenter;

import com.dfxh.wang.serialport_test.biz.ScreenActivityBiz;
import com.dfxh.wang.serialport_test.biz.UpLoadListener;
import com.dfxh.wang.serialport_test.listeners.CloseSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.OpenSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.ReadDataWithSpListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;
import com.dfxh.wang.serialport_test.model.ScreenActivityModel;
import com.dfxh.wang.serialport_test.view.ScreenActivityView;

import android_serialport_api.SerialPort;

/**
 * Created by Axehome_Mr.z
 * 2018/11/28
 * $Describe
 */
public class ScreenActivityPresenter {
    private ScreenActivityBiz mModel;
    private ScreenActivityView mView;

    public ScreenActivityPresenter(ScreenActivityView mView) {
        this.mView = mView;
        mModel = new ScreenActivityModel();
    }

    public void openSerialPort() {

        // TODO: 2018/11/28 暂时注释
        mModel.openSerialPort(mView.senSPUtils(), new OpenSerialPortListener() {
            @Override
            public void openSerialPortSuccess(SerialPort serialPort) {
                mView.openSerialPort(serialPort);
            }

            @Override
            public void openSerialPortError(String errorMsg) {
                mView.errorListener("打开串口失败");
            }
        });
//        mModel.testMethod(onoff, new SendListener() {
//            @Override
//            public void sendSuccess(String msg) {
//
//            }
//
//            @Override
//            public void sendError(String errorMsg) {
//
//            }
//        });
    }

    public void closeSerialPort() {
        mModel.closeSp(mView.senSPUtils(), mView.vertifySerialPort(), new CloseSerialPortListener() {
            @Override
            public void closeSuccess(String msg) {
                mView.closeSerialPort("串口已关闭");
            }

            @Override
            public void closeError(String msg) {
                mView.errorListener("串口关闭失败");
            }
        });
    }

    public void sendDataWithSP(String code) {
        mModel.sendDataWithSP(mView.senSPUtils(), mView.vertifySerialPort(), code, new SendListener() {
            @Override
            public void sendSuccess(String msg) {
                mView.sendDataSuccess(msg);
            }

            @Override
            public void sendError(String errorMsg) {
                mView.sendDataError(errorMsg);
            }
        });
    }

    public void readData() {

        mModel.readSerialData(mView.senSPUtils(), mView.vertifySerialPort(), new ReadDataWithSpListener() {
            @Override
            public void readDataSuccess(String msg) {
                mView.readDataSuccess(msg);
            }

            @Override
            public void readDataError(String errorMsg) {
                mView.readDataError();
            }
        });
    }

    public void finishflow() {
        mModel.upLoadWithoutData(mView.getDeviceId(), new UpLoadListener() {
            @Override
            public void upLoadSuccess(String successStr) {
                mView.errorListener("状态异常");
            }

            @Override
            public void upLoadError(String ErrorStr) {
                mView.errorListener("aaaa");
            }
        });
    }

}
