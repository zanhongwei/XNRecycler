package com.dfxh.wang.serialport_test.biz;

import android.content.Context;
import android.graphics.Bitmap;

import com.dfxh.wang.serialport_test.listeners.RecyclerLoginListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;

public interface LoginBiz {

    void login(String account, String password, RecyclerLoginListener listener);

    void updataApk(String currentVersion, SendListener listener);

    Bitmap createCode(String adminLoginCode, Context context);

    void unLlogin();

}
