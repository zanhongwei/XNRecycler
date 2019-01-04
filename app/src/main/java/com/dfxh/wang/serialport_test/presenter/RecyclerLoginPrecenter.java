package com.dfxh.wang.serialport_test.presenter;

import android.graphics.Bitmap;

import com.dfxh.wang.serialport_test.beans.ApkVersion;
import com.dfxh.wang.serialport_test.beans.RecyclerBean;
import com.dfxh.wang.serialport_test.listeners.RecyclerLoginListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;
import com.dfxh.wang.serialport_test.model.RecyclerLoginModel;
import com.dfxh.wang.serialport_test.view.RecyclerLoginView;

public class RecyclerLoginPrecenter {


    private RecyclerLoginView mLoginView;
    private RecyclerLoginModel mLoginModel;


    public RecyclerLoginPrecenter(RecyclerLoginView mLoginView) {
        this.mLoginView = mLoginView;
        mLoginModel = new RecyclerLoginModel();
    }

    public void login() {

//        mLoginView.loginSuccess("aaa");

//        mLoginView.loading();
        mLoginModel.login(mLoginView.getAccount(), mLoginView.getPassWord(), new RecyclerLoginListener() {
            @Override
            public void LoginSuccess(String user) {
//                mLoginView.hideLoading();
                mLoginView.loginSuccess(user);
            }

            @Override
            public void loginError(String msg) {
//                mLoginView.hideLoading();
                mLoginView.loginFail(msg);
            }
        });
    }

    public Bitmap createLoginImgCode() {
        //暂定二维码字段为“{"{"adminLoginCode":"1"}"}”
        String text = "{\"adminLoginCode\":\"1\"}";
        Bitmap code = mLoginModel.createCode(text, mLoginView.getContext());
        return code;
    }

    public void updateApk() {

        mLoginModel.updataApk(mLoginView.versionCode(), new SendListener() {
            @Override
            public void sendSuccess(String msg) {
                mLoginView.needUpdate(msg);
            }

            @Override
            public void sendError(String errorMsg) {
                mLoginView.donotUpdata(errorMsg);
            }
        });
    }

    public void unLogin(){
        mLoginModel.unLlogin();
    }

}
