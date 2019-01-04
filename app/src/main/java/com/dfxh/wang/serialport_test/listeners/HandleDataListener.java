package com.dfxh.wang.serialport_test.listeners;

/**
 * Created by Axehome_Mr.z on 2018/12/4 17:07
 * $Describe
 */
public interface HandleDataListener {

    void handleSuccess(boolean[] handle);

    void handleError(String msg);

}
