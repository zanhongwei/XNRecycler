package com.dfxh.wang.serialport_test.listeners;

/**
 * Created by Axehome_Mr.z
 * 2018/11/28 14:11
 * $Describe
 */
public interface ReadDataWithSpListener {

    void readDataSuccess(String data);

    void readDataError(String data);
}
