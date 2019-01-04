package com.dfxh.wang.serialport_test.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.dfxh.wang.serialport_test.R;
import com.dfxh.wang.serialport_test.beans.ApkVersion;
import com.dfxh.wang.serialport_test.beans.RecyclerBean;
import com.dfxh.wang.serialport_test.biz.LoginBiz;
import com.dfxh.wang.serialport_test.listeners.RecyclerLoginListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;
import com.dfxh.wang.serialport_test.utils.Internet;
import com.dfxh.wang.serialport_test.utils.MyUtils;
import com.dfxh.wang.serialport_test.utils.QRCode;
import com.dfxh.wang.serialport_test.view.RecyclerLoginView;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import android.os.Handler;

import okhttp3.Call;


public class RecyclerLoginModel implements LoginBiz {

    @Override
    public void login(final String account, final String password, final RecyclerLoginListener listener) {

        OkHttpUtils.post()
                .url(Internet.login)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("aaa", "onError 登录接口失败返回  === " + e.getMessage());
                        listener.loginError("网络错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("aaa", "(RecyclerLoginModel.java:51)<---->" + response);
                        if (TextUtils.isEmpty(response)) {
                            listener.loginError("暂无数据");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("result");
                                if (code == 1) {
//                                    RecyclerBean recyclerBean = new Gson().fromJson(response, RecyclerBean.class);
                                    listener.LoginSuccess("登录成功");

                                } else {
                                    listener.loginError("网络不给力");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.loginError("数据异常");
                            }
                        }
                    }
                });

    }

    @Override
    public void updataApk(final String currentVersion, final SendListener listener) {

        OkHttpUtils.post()
                .url(Internet.updata_apk)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("aaa",
                                "(RecyclerLoginModel.java:113)<---->" + e.getMessage());
                        listener.sendError("网络错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("aaa",
                                "(RecyclerLoginModel.java:119)<---->" + response);
                        if (TextUtils.isEmpty(response)) {
                            listener.sendError("暂无新版本");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code == 200) {
                                    ApkVersion apkVersion = new Gson().fromJson(response, ApkVersion.class);
                                    String sys_number = apkVersion.getSys_type().get(0).getSys_number();
                                    Log.e("aaa",
                                            "(RecyclerLoginModel.java:132)<--currentVersion-->" + sys_number);
                                    Log.e("aaa",
                                            "(RecyclerLoginModel.java:134)<--currentVersion-->" + currentVersion);
                                    if (currentVersion.equals(sys_number)) {
                                        listener.sendError("暂无新版本");
                                    } else {
                                        listener.sendSuccess(apkVersion.getSys_type().get(0).getSys_path());
                                    }
                                } else {
                                    listener.sendError("网络错误");
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    @Override
    public Bitmap createCode(String adminLoginCode, Context context) {
        Bitmap bitmap = MyUtils.drawableToBitmap(context.getResources().getDrawable(R.drawable.xiaoniu));
        Bitmap qrCodeWithLogo = QRCode.createQRCodeWithLogo6(adminLoginCode, 500, bitmap);
        return qrCodeWithLogo;
    }

    @Override
    public void unLlogin() {
        OkHttpUtils.post()
                .url(Internet.unLogin)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("aaa", "(RecyclerLoginModel.java:138)<---->" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("aaa", "(RecyclerLoginModel.java:143)<---->" + response);
                    }
                });

    }


}
