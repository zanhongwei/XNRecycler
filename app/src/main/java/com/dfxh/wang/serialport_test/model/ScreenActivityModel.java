package com.dfxh.wang.serialport_test.model;

import android.text.TextUtils;
import android.util.Log;

import com.dfxh.wang.serialport_test.biz.ScreenActivityBiz;
import com.dfxh.wang.serialport_test.biz.UpLoadListener;
import com.dfxh.wang.serialport_test.listeners.CloseSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.OpenSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.ReadDataWithSpListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;
import com.dfxh.wang.serialport_test.listeners.VerifyScanCodeListener;
import com.dfxh.wang.serialport_test.utils.Internet;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import android_serialport_api.SerialPort;
import okhttp3.Call;
import utils.SerialPortUtils;

public class ScreenActivityModel implements ScreenActivityBiz {

    private SerialPort serialPort;

    @Override
    public void openSerialPort(SerialPortUtils serialPortUtils, OpenSerialPortListener listener) {
        serialPort = serialPortUtils.openSerialPort();
        if (serialPort == null) {
            listener.openSerialPortError("串口打开失败");
        } else {
            listener.openSerialPortSuccess(serialPort);
        }
    }

    @Override
    public void closeSp(SerialPortUtils serialPortUtils, SerialPort serialPort, CloseSerialPortListener listener) {
        if (serialPort != null)
            serialPortUtils.closeSerialPort();
        listener.closeSuccess("关闭串口成功");
    }

    @Override
    public void verifySP(String deviceId, VerifyScanCodeListener listener) {
        //暂时不用这个方法
    }

    @Override
    public void sendDataWithSP(SerialPortUtils serialPortUtils, SerialPort serialPort, String data, SendListener listener) {
        if (serialPort != null) {
            serialPortUtils.sendSerialPort(data);
            listener.sendSuccess("发送成功");
        } else {
            listener.sendError("打开串口失败");
        }
    }

    @Override
    public void readSerialData(SerialPortUtils serialPortUtils, SerialPort serialPort, ReadDataWithSpListener listener) {
        String sData = serialPortUtils.readSerialPort();
        Log.e("aaa",
                "(ScreenActivityModel.java:55)<--sData-->" + sData);
        if (TextUtils.isEmpty(sData)) {
            listener.readDataError("");
        } else {
            listener.readDataSuccess(sData);
        }
    }

    @Override
    public void testMethod(boolean onoff, SendListener listener) {

        if (onoff) {
            listener.sendSuccess("成功");
        } else {
            listener.sendError("失败");
        }
    }

    @Override
    public void upLoadWithoutData(String device_id, final UpLoadListener loadListener) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("device_id", device_id);
//        params.put("device_id", "1d5d5965-3394-35fe-bd30-7c0033a8c6c1");
        Log.e("aaa", "(ScreenActivityModel.java:260)<---->" + params);
        OkHttpUtils.post()
                .url(Internet.upload_without_data)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("aaa",
                                "(ScreenActivityModel.java:229)<---->" + e.getMessage());
                        loadListener.upLoadError("网络错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("aaa",
                                "(ScreenActivityModel.java:236)<---->" + response);
                        if (TextUtils.isEmpty(response)) {
                            loadListener.upLoadError("上传失败");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int result = jsonObject.getInt("result");
                                if (result == 1) {
                                    loadListener.upLoadSuccess("上传成功");
                                } else {
                                    loadListener.upLoadError("上传失败");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                loadListener.upLoadError("状态异常");
                            }
                        }
                    }
                });

    }

    public void updata(HashMap<String, String> params, final UpLoadListener loadListener) {
        Log.e("aaa", "(ScanCodeAcModel.java:260)<---->" + params);
        OkHttpUtils.post()
                .url(Internet.upload_data)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("aaa",
                                "(ScanCodeAcModel.java:229)<---->" + e.getMessage());
                        loadListener.upLoadError("网络错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("aaa",
                                "(ScanCodeAcModel.java:236)<---->" + response);
                        if (TextUtils.isEmpty(response)) {
                            loadListener.upLoadError("上传失败");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int result = jsonObject.getInt("result");
                                if (result == 1) {
                                    loadListener.upLoadSuccess("上传成功");
                                } else {
                                    loadListener.upLoadError("上传失败");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                loadListener.upLoadError("状态异常");
                            }
                        }
                    }
                });
    }

}
