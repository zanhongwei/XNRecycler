package com.dfxh.wang.serialport_test.view;

import android.content.Context;

import com.dfxh.wang.serialport_test.beans.ApkVersion;
import com.dfxh.wang.serialport_test.beans.RecyclerBean;

public interface RecyclerLoginView {

    String getAccount();

    String getPassWord();

    void loginSuccess(String recyclerBean);

    void loginFail(String msg);

    void loading();

    void hideLoading();

    void needUpdate(String url);

    void donotUpdata(String url);

    String versionCode();

    Context getContext();


}
