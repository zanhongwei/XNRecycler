package com.dfxh.wang.serialport_test.presenter;

import android.graphics.Bitmap;
import android.util.Log;

import com.dfxh.wang.serialport_test.beans.ScanCodeStateBean;
import com.dfxh.wang.serialport_test.biz.ScanCodeBiz;
import com.dfxh.wang.serialport_test.biz.UpLoadListener;
import com.dfxh.wang.serialport_test.listeners.CloseSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.OpenSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;
import com.dfxh.wang.serialport_test.listeners.StartActivityListener;
import com.dfxh.wang.serialport_test.listeners.TimeStempListener;
import com.dfxh.wang.serialport_test.listeners.VerifyScanCodeListener;
import com.dfxh.wang.serialport_test.model.ScanCodeAcModel;
import com.dfxh.wang.serialport_test.view.DeliverView;
import com.dfxh.wang.serialport_test.view.ScanCodeView;

import android_serialport_api.SerialPort;

public class ScanCodeActivityPresenter {


    private ScanCodeView mView;
    private ScanCodeBiz mMode;
    private DeliverView mDeliverView;

    public ScanCodeActivityPresenter(ScanCodeView mView) {
        this.mView = mView;
        this.mMode = new ScanCodeAcModel();
    }

    public ScanCodeActivityPresenter(DeliverView mDeliverView) {
        this.mDeliverView = mDeliverView;
        this.mMode = new ScanCodeAcModel();
    }

    public void sendSerialPort(String data) {

        mMode.sendSerialPort(mView.senSPUtils(), mView.vertifySerialPort(), data, new SendListener() {
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
        mMode.openSerialPort(mView.senSPUtils(), new OpenSerialPortListener() {
            @Override
            public void openSerialPortSuccess(SerialPort serialPort) {
                mView.openSerialPort(serialPort);
            }

            @Override
            public void openSerialPortError(String errorMsg) {
                mView.errorListener("打开串口失败");
            }
        });
    }

    public void closeSerialPort() {
        mMode.closeSerialPort(mView.senSPUtils(), mView.vertifySerialPort(), new CloseSerialPortListener() {
            @Override
            public void closeSuccess(String msg) {
                mView.successListener(msg);
            }

            @Override
            public void closeError(String msg) {
                mView.ScanCodeError("");
            }
        });
    }

    public Bitmap setCodeImage(String text) {
        Bitmap bitmap = mMode.setQRCodeImage(text, mView.getContext());
        return bitmap;
    }

    public void getTimeStemp() {
        mView.showLoading();
        mMode.getTimeStemp(new TimeStempListener() {
            @Override
            public void getSuccess(String string) {
                mView.hideLoading();
                mView.getTimeSuccess(string);
            }

            @Override
            public void getError(String string) {
                mView.hideLoading();
                mView.getTimeError(string);
            }
        });
    }

    public void scanCodeState() {
        Log.e("aaa",
                "(ScanCodeActivityPresenter.java:103)<--执行查询是否扫码成功方法 scanCodeState()-->");
//        mView.showLoading();
        mMode.verifyScanCodeState(mView.getDeviceId(), new VerifyScanCodeListener() {
            @Override
            public void scanSuccess(ScanCodeStateBean bean) {
//                mView.hideLoading();
                mView.ScanCodeSuccess(bean);
            }

            @Override
            public void scanError(String msg) {
//                mView.hideLoading();
                mView.ScanCodeError(msg);
            }
        });
    }

    public void uploadData(String goods_type, String goods_price, String goods_weight, String goods_money) {

        mDeliverView.showLoading();
        mMode.upLoadData(mDeliverView.getUserId(), goods_type, goods_price, goods_weight, mDeliverView.getDeviceId(), goods_money, new UpLoadListener() {
            @Override
            public void upLoadSuccess(String successStr) {
                mDeliverView.hideLoading();
                mDeliverView.upLoadSuccess(successStr);
            }

            @Override
            public void upLoadError(String ErrorStr) {
                mDeliverView.hideLoading();
                mDeliverView.upLoadError(ErrorStr);
            }
        });
    }

    public void startActivity() {

        mView.showLoading();
        mMode.startActivity(new StartActivityListener() {
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

    public void readData() {

        mMode.readSerialData(mView.senSPUtils(), mView.vertifySerialPort(), new SendListener() {
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
