package com.dfxh.wang.serialport_test.view;

import android_serialport_api.SerialPort;
import utils.SerialPortUtils;

/**
 * Created by Axehome_Mr.z
 * 2018/11/28--11:58
 * $Describe
 */
public interface ScreenActivityView {

    /**
     * 所有失败的监听
     */
    void errorListener(String msgError);

    /**
     * 打开串口
     */
    void openSerialPort(SerialPort sp);

    /**
     * 关闭串口
     */
    void closeSerialPort(String msg);

    /**
     * 下位机正常返回的监听
     *
     * @param sData
     */
    void readDataSuccess(String sData);

    /**
     * 下位机错误的返回监听
     */
    void readDataError();

    /**
     * 验证串口正常的监听
     *
     * @return
     */
    SerialPort vertifySerialPort();

    /**
     * @return
     */
    SerialPortUtils senSPUtils();

    /**
     * 发送数据成功的监听
     *
     * @param msgSuccess
     */
    void sendDataSuccess(String msgSuccess);

    /**
     * 发送数据失败的监听
     *
     * @param msgError
     */
    void sendDataError(String msgError);


    String getDeviceId();
}
