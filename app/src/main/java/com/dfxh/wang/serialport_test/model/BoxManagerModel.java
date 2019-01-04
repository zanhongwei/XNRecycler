package com.dfxh.wang.serialport_test.model;

import android.text.TextUtils;
import android.util.Log;

import com.dfxh.wang.serialport_test.biz.BoxManagerBiz;
import com.dfxh.wang.serialport_test.listeners.CloseSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.HandleDataListener;
import com.dfxh.wang.serialport_test.listeners.OpenSerialPortListener;
import com.dfxh.wang.serialport_test.listeners.SendListener;
import com.dfxh.wang.serialport_test.listeners.StartActivityListener;
import com.dfxh.wang.serialport_test.utils.BinStr;
import com.dfxh.wang.serialport_test.utils.Internet;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import android_serialport_api.SerialPort;
import okhttp3.Call;
import utils.SerialPortUtils;

public class BoxManagerModel implements BoxManagerBiz {

    private SerialPort serialPort;

    public void startActivity(String sort, String code, final StartActivityListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    listener.startSuccess();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    listener.startError();
                }
            }
        }).start();
    }


    @Override
    public void sendSerialPort(SerialPortUtils serialPortUtils, SerialPort serialPort, String data, SendListener listener) {
        if (serialPort != null) {
            serialPortUtils.sendSerialPort(data);
            listener.sendSuccess("发送成功");
        } else {
            listener.sendError("打开串口失败");
        }
    }

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
    public void readSerialData(SerialPortUtils serialPortUtils, SerialPort serialPort, SendListener listener) {
        String sData = serialPortUtils.readSerialPort();
        if (TextUtils.isEmpty(sData)) {
            listener.sendError("");
        } else {
            listener.sendSuccess(sData);
        }
    }

    @Override
    public void sendShortMessage(String num, final SendListener listener) {


        OkHttpUtils
                .post()
                .url(Internet.send_message)
                .addParams("num", num)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("aaa", "(BoxManagerModel.java:91)<---->" + e.getMessage());
                        listener.sendError("网络错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("aaa", "(BoxManagerModel.java:96)<---->" + response);
                        if (TextUtils.isEmpty(response)) {
                            listener.sendError("暂无数据");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int result = jsonObject.getInt("result");
                                if (result == 1) {
                                    listener.sendSuccess("发送成功");
                                } else {
                                    listener.sendError("通知失败");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.sendError("通知失败");
                            }
                        }
                    }
                });
    }


    public boolean[] handleData(String data) {

        boolean[] booleans = {true, true, true, true, true, true, true};
        String str = "01031444030000000000000000000000000000000000001868";

        Log.e("aaa", "(BoxManagerModel.java:78)<---->" + str.length());
        //接收到的  数据源
        String data1 = data.trim();
        Log.e("aaa", "(BoxManagerModel.java:79)<--下位机返回的数据-->" + data1);
        Log.e("aaa", "(BoxManagerModel.java:84)<--data1-->" + data1);
        Log.e("aaa", "(BoxManagerModel.java:85)<---->" + !TextUtils.isEmpty(data1));
        Log.e("aaa", "(BoxManagerModel.java:85)<---->" + data1.length());
        if (!TextUtils.isEmpty(data1) && data1.length() > 50) {
            String str1 = data1.substring(6, 10);
            Log.e("aaa", "(BoxManagerModel.java:85)<--截取的第一个字符串-->" + str1);
            String string1 = BinStr.hexString2binaryString(str1);
            Log.e("aaa", "(BoxManagerModel.java:87)<--转换后的第一个字符串-->" + string1);
            boolean b1 = analysisMetal(string1);
            booleans[0] = b1;//金属
            boolean b2 = analysisPlastics(string1);
            booleans[1] = b2;//塑料
            String str2 = data1.substring(10, 14);
            String string2 = BinStr.hexString2binaryString(str2);
            boolean b3 = analysisTextile(string2);
            booleans[2] = b3;//纺织物
            boolean b5 = analysisPaper(string2);
            booleans[4] = b5;//纸张
            String str3 = data1.substring(14, 18);
            String string3 = BinStr.hexString2binaryString(str3);
            boolean b4 = analysisBottle(string3);
            booleans[3] = b4;//瓶子
            boolean b6 = analysisWaste(string3);
            booleans[5] = b6;//垃圾
            boolean b7 = analysisGlass(string3);
            booleans[6] = b7;//玻璃
            String str4 = data1.substring(18, 22);
            String string4 = BinStr.hexString2binaryString(str4);
            String str5 = data1.substring(22, 26);
            String string5 = BinStr.hexString2binaryString(str5);
            String str6 = data1.substring(26, 30);
            String string6 = BinStr.hexString2binaryString(str6);
            String str7 = data1.substring(30, 34);
            String string7 = BinStr.hexString2binaryString(str7);
            String str8 = data1.substring(34, 38);
            String string8 = BinStr.hexString2binaryString(str8);
            String str9 = data1.substring(38, 42);
            String string9 = BinStr.hexString2binaryString(str9);
            String str10 = data1.substring(42, 46);
            String string10 = BinStr.hexString2binaryString(str10);

        }

        return booleans;
    }

    public boolean analysisMetal(String string) {
        String substring = string.substring(6, 7);
        Log.e("aaa", "(BoxManagerModel.java:116)<--截取2进制字符串-->" + substring);
        if (substring.equals("0"))
            return true;
        else return false;
    }

    public boolean analysisPlastics(String string) {
        String substring = string.substring(12, 13);
        if (substring.equals("0"))
            return true;
        else return false;
    }

    public boolean analysisTextile(String string) {
        String substring = string.substring(2, 3);
        String substring1 = string.substring(3, 4);
        if (substring.equals("0") && substring1.equals("0"))
            return true;
        else return false;

    }

    public boolean analysisPaper(String string) {

        String substring = string.substring(8, 9);
        if (substring.equals("0"))
            return true;
        else return false;
    }

    public boolean analysisBottle(String string) {

        String substring = string.substring(12, 13);
        if (substring.equals("0"))
            return true;
        else return false;
    }

    public boolean analysisWaste(String string) {

        String substring = string.substring(6, 7);
        if (substring.equals("0"))
            return true;
        else return false;
    }

    public boolean analysisGlass(String string) {

        String substring = string.substring(0, 1);
        if (substring.equals("0"))
            return true;
        else return false;
    }


}
