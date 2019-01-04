package com.dfxh.wang.serialport_test.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.dfxh.wang.serialport_test.R;
import com.dfxh.wang.serialport_test.beans.ScanCodeStateBean;
import com.dfxh.wang.serialport_test.biz.ScanCodeBiz;
import com.dfxh.wang.serialport_test.biz.UpLoadListener;
import com.dfxh.wang.serialport_test.listeners.CloseSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.OpenSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;
import com.dfxh.wang.serialport_test.listeners.StartActivityListener;
import com.dfxh.wang.serialport_test.listeners.TimeStempListener;
import com.dfxh.wang.serialport_test.listeners.VerifyScanCodeListener;
import com.dfxh.wang.serialport_test.utils.Internet;
import com.dfxh.wang.serialport_test.utils.MyUtils;
import com.dfxh.wang.serialport_test.utils.QRCode;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import android_serialport_api.SerialPort;
import okhttp3.Call;
import utils.SerialPortUtils;

public class ScanCodeAcModel implements ScanCodeBiz {
    private SerialPort serialPort;

    /**
     * 发送指令的逻辑
     *
     * @param data
     */
    @Override
    public void sendSerialPort(SerialPortUtils serialPortUtils, SerialPort serialPort, String data, SendListener listener) {
        if (serialPort != null) {
            serialPortUtils.sendSerialPort(data);
            listener.sendSuccess("发送成功");
        } else {
            listener.sendError("打开串口失败");
        }

    }

    /**
     * @param serialPortUtils
     * @param listener
     */
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
    public void closeSerialPort(SerialPortUtils serialPortUtils, SerialPort serialPort, CloseSerialPortListener listener) {
        if (serialPort != null)
            serialPortUtils.closeSerialPort();
        listener.closeSuccess("关闭串口成功");
    }

    @Override
    public Bitmap setQRCodeImage(String text, Context context) {

        Bitmap bitmap = MyUtils.drawableToBitmap(context.getResources().getDrawable(R.drawable.xiaoniu));
        Bitmap qrCodeWithLogo = QRCode.createQRCodeWithLogo6(text, 500, bitmap);

        return qrCodeWithLogo;
    }

    @Override
    public void getTimeStemp(final TimeStempListener listener) {

        OkHttpUtils.post()
                .url(Internet.get_timeStemp)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("aaa",
                                "(ScanCodeAcModel.java:76)<---->" + e.getMessage());
                        listener.getError("网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("aaa",
                                "(ScanCodeAcModel.java:84)<---->" + response);
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int result = jsonObject.getInt("result");
                                Log.e("aaa",
                                        "(ScanCodeAcModel.java:102)<---->" + result);
                                if (result == 1) {
                                    String data = jsonObject.getString("data");
                                    listener.getSuccess(data);
                                } else {
                                    listener.getError("网络异常");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.getError("网络异常");
                            }
                        } else {
                            listener.getError("暂无数据");
                        }
                    }
                });

    }

    @Override
    public void verifyScanCodeState(String deviceId, final VerifyScanCodeListener listener) {

        Log.e("aaa",
                "(ScanCodeAcModel.java:124)<--执行查询扫码状态方法  verifyScanCodeState() -->");
        Log.e("aaa",
                "(ScanCodeAcModel.java:126)<--deviceId-->" + deviceId);
        OkHttpUtils.post()
                .url(Internet.scan_code)
                .addParams("device_id", deviceId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("aaa",
                                "(ScanCodeAcModel.java:123)<---->" + e.getMessage());
                        listener.scanError("网络错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("aaa",
                                "(ScanCodeAcModel.java:129)<---->" + response);
                        if (TextUtils.isEmpty(response)) {
                            listener.scanError("网络错误");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int result = jsonObject.getInt("result");
                                if (result == 1) {
                                    ScanCodeStateBean scanCodeStateBean = new Gson().fromJson(response, ScanCodeStateBean.class);
                                    listener.scanSuccess(scanCodeStateBean);
                                } else {
                                    listener.scanError("扫描失败");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.scanError("状态异常");
                            }
                        }
                    }
                });


    }

    @Override
    public void upLoadData(String userId, String goods_type, String goods_price, String goods_weight, String device_id, String goods_money, final UpLoadListener loadListener) {

        final HashMap<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(goods_price)) {
            params.put("goods_price", "");
        } else {
            params.put("goods_price", goods_price);
        }

        if (TextUtils.isEmpty(goods_weight)) {
            params.put("goods_weight", "");
        } else {
            params.put("goods_weight", goods_weight);
        }
        if (TextUtils.isEmpty(goods_money)) {
            params.put("goods_money", "");
        } else {
            params.put("goods_money", goods_money);
        }

        params.put("goods_type", goods_type);
        params.put("device_id", device_id);
        params.put("user_id", userId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    updata(params, loadListener);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void upLoadWithoutData(String device_id, final UpLoadListener loadListener) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("device_id", device_id);
//        params.put("device_id", "1d5d5965-3394-35fe-bd30-7c0033a8c6c1");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    updata(params, loadListener);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        updata(params, loadListener);
    }

    @Override
    public void readSerialData(SerialPortUtils serialPortUtils, SerialPort serialPort, SendListener listener) {
        String sData = serialPortUtils.readSerialPort();

        Log.e("aaa",
                "(ScanCodeAcModel.java:217)<--sData-->" + sData);
        if (TextUtils.isEmpty(sData)) {
            listener.sendError("");
        } else {
            listener.sendSuccess(sData);
        }

    }

    @Override
    public void startActivity(final StartActivityListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    listener.startSuccess();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    listener.startError();
                }
            }
        }).start();
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
