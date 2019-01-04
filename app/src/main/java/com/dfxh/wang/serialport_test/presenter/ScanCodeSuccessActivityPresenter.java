package com.dfxh.wang.serialport_test.presenter;

import android.graphics.Bitmap;
import android.util.Log;

import com.dfxh.wang.serialport_test.beans.ScanCodeStateBean;
import com.dfxh.wang.serialport_test.biz.ScanCodeBiz;
import com.dfxh.wang.serialport_test.biz.UpLoadListener;
import com.dfxh.wang.serialport_test.listeners.CloseSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.OpenSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;
import com.dfxh.wang.serialport_test.listeners.TimeStempListener;
import com.dfxh.wang.serialport_test.listeners.VerifyScanCodeListener;
import com.dfxh.wang.serialport_test.model.ScanCodeAcModel;
import com.dfxh.wang.serialport_test.view.DeliverView;
import com.dfxh.wang.serialport_test.view.ScanCodeView;

import android_serialport_api.SerialPort;

public class ScanCodeSuccessActivityPresenter {


    private ScanCodeBiz mMode;
    private DeliverView mDeliverView;

    public ScanCodeSuccessActivityPresenter(DeliverView mDeliverView) {
        this.mDeliverView = mDeliverView;
        this.mMode = new ScanCodeAcModel();

    }

    public void sendSerialPort(String data) {

        mMode.sendSerialPort(mDeliverView.senSPUtils(), mDeliverView.vertifySerialPort(), data, new SendListener() {
            @Override
            public void sendSuccess(String msg) {
                mDeliverView.successListener(msg);
            }

            @Override
            public void sendError(String errorMsg) {
                mDeliverView.errorListener(errorMsg);
            }
        });
    }

    public void openSerialPort() {
        mMode.openSerialPort(mDeliverView.senSPUtils(), new OpenSerialPortListener() {
            @Override
            public void openSerialPortSuccess(SerialPort serialPort) {
                mDeliverView.openSerialPort(serialPort);
            }

            @Override
            public void openSerialPortError(String errorMsg) {
                mDeliverView.errorListener("打开串口失败");
            }
        });
    }

    public void closeSerialPort() {
        mMode.closeSerialPort(mDeliverView.senSPUtils(), mDeliverView.vertifySerialPort(), new CloseSerialPortListener() {
            @Override
            public void closeSuccess(String msg) {
                mDeliverView.successListener(msg);
            }

            @Override
            public void closeError(String msg) {
                mDeliverView.errorListener("关闭串口失败");
            }
        });
    }

    public Bitmap setCodeImage(String text) {
        Bitmap bitmap = mMode.setQRCodeImage(text, mDeliverView.getContext());
        return bitmap;
    }

//    public void getTimeStemp() {
//        mDeliverView.showLoading();
//        mMode.getTimeStemp(new TimeStempListener() {
//            @Override
//            public void getSuccess(String string) {
//                mDeliverView.hideLoading();
//                mDeliverView.getTimeSuccess(string);
//            }
//
//            @Override
//            public void getError(String string) {
//                mDeliverView.hideLoading();
//                mDeliverView.getTimeError(string);
//            }
//        });
//    }

//    public void scanCodeState() {
////        mDeliverView.showLoading();
//        mMode.verifyScanCodeState(mDeliverView.getDeviceId(), new VerifyScanCodeListener() {
//            @Override
//            public void scanSuccess(ScanCodeStateBean bean) {
////                mDeliverView.hideLoading();
//                mDeliverView.ScanCodeSuccess(bean);
//            }
//
//            @Override
//            public void scanError(String msg) {
////                mDeliverView.hideLoading();
//                mDeliverView.ScanCodeError(msg);
//            }
//        });
//    }

    public void uploadData(String goods_type, String goods_price, String goods_weight, String goods_money) {

        Log.e("aaa", "(ScanCodeSuccessActivityPresenter.java:116)<---->" + goods_price);
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

    public void upLoadWithoutData() {
        mDeliverView.showLoading();
        mMode.upLoadWithoutData(mDeliverView.getDeviceId(), new UpLoadListener() {
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


    public void readData() {
        Log.e("aaa",
                "(ScanCodeSuccessActivityPresenter.java:133)<--读取第三个数据-->");
        mMode.readSerialData(mDeliverView.senSPUtils(), mDeliverView.vertifySerialPort(), new SendListener() {
            @Override
            public void sendSuccess(String msg) {
                mDeliverView.readDataSuccess(msg);
            }

            @Override
            public void sendError(String errorMsg) {
                mDeliverView.readDataError();
            }
        });
    }


}
