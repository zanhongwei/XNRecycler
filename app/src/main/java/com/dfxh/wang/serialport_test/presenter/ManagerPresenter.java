package com.dfxh.wang.serialport_test.presenter;

import com.dfxh.wang.serialport_test.biz.ManagerBiz;
import com.dfxh.wang.serialport_test.listeners.CloseSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.OpenSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;
import com.dfxh.wang.serialport_test.model.ManagerModel;
import com.dfxh.wang.serialport_test.view.ManagerActivityView;

import android_serialport_api.SerialPort;

/**
 * Describe $回收员的页面操作类
 * Create by axehome_Mr.Z
 * Date on 2018/11/22 15:23
 */
public class ManagerPresenter {

    private ManagerBiz mModel;
    private ManagerActivityView mView;

    public ManagerPresenter(ManagerActivityView mView) {
        this.mView = mView;
        mModel = new ManagerModel();
    }

    public void openSerialPort() {

        mModel.openSerialPort(mView.senSPUtils(), new OpenSerialPortListener() {
            @Override
            public void openSerialPortSuccess(SerialPort serialPort) {
                mView.openSerialPort(serialPort);
            }

            @Override
            public void openSerialPortError(String errorMsg) {
                mView.openSerialPortsError(errorMsg);
            }

        });

    }

    public void closeSerialPorts() {
        mModel.closeSerialPort(mView.senSPUtils(), mView.vertifySerialPort(), new CloseSerialPortListener() {
            @Override
            public void closeSuccess(String msg) {
                mView.closeSerialPort(msg);
            }

            @Override
            public void closeError(String msg) {
                mView.closeSeialError(msg);
            }
        });
    }

    public void senData(String data) {
        mModel.senSpData(mView.vertifySerialPort(), mView.senSPUtils(), data, new SendListener() {
            @Override
            public void sendSuccess(String msg) {
                mView.sendSpDataSuccess(msg);
            }

            @Override
            public void sendError(String errorMsg) {
                mView.sendSpDataError(errorMsg);
            }
        });

    }

    public void readData() {
        mModel.readSerialProt(mView.vertifySerialPort(), mView.senSPUtils(), new SendListener() {
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
}
