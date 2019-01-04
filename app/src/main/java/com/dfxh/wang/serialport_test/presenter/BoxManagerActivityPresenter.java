package com.dfxh.wang.serialport_test.presenter;

import android.util.Log;

import com.dfxh.wang.serialport_test.listeners.CloseSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.HandleDataListener;
import com.dfxh.wang.serialport_test.listeners.OpenSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;
import com.dfxh.wang.serialport_test.listeners.StartActivityListener;
import com.dfxh.wang.serialport_test.model.BoxManagerModel;
import com.dfxh.wang.serialport_test.view.BoxManagerView;

import android_serialport_api.SerialPort;

public class BoxManagerActivityPresenter {

    private BoxManagerView mView;
    private BoxManagerModel mModel;

    public BoxManagerActivityPresenter(BoxManagerView mView) {
        this.mView = mView;
        mModel = new BoxManagerModel();
    }

    public void startActivity() {
        mView.showLoading();
        mModel.startActivity(mView.getSort(), mView.getCode(), new StartActivityListener() {
            @Override
            public void startSuccess() {
                mView.hideLoading();
                mView.startActivitySuccess();
            }

            @Override
            public void startError() {
                mView.hideLoading();
                mView.startActivityError();
            }
        });
    }

    public void sendSerialPort(String data) {

        mModel.sendSerialPort(mView.senSPUtils(), mView.vertifySerialPort(), data, new SendListener() {
            @Override
            public void sendSuccess(String msg) {
                mView.successListener(msg);
            }

            @Override
            public void sendError(String errorMsg) {
                mView.errorListener(errorMsg);
            }
        });
    }

    public void openSerialPort() {
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


//        boolean[] booleans = mModel.handleData("0103144d34c7d30234007f0000000000000000000000000089");
//        return booleans;

    }

    public void closeSerialPort() {
        mModel.closeSerialPort(mView.senSPUtils(), mView.vertifySerialPort(), new CloseSerialPortListener() {
            @Override
            public void closeSuccess(String msg) {
                mView.closeSerialPort(msg);
            }

            @Override
            public void closeError(String msg) {
                mView.errorListener("");
            }
        });
    }

    public void readData() {

        mModel.readSerialData(mView.senSPUtils(), mView.vertifySerialPort(), new SendListener() {
            @Override
            public void sendSuccess(String msg) {
                mView.readDataSuccess(msg);
            }

            @Override
            public void sendError(String errorMsg) {
                mView.readDataError();
            }
        });

    }

    public boolean[] handleData(String data) {

        boolean[] booleans = mModel.handleData(data);

        return booleans;

    }

    public void sendShortMessage(String num) {

        mModel.sendShortMessage(num, new SendListener() {
            @Override
            public void sendSuccess(String msg) {
//                mView.successListener(msg);
            }

            @Override
            public void sendError(String errorMsg) {
//                mView.errorListener(errorMsg);
            }
        });


    }

}
