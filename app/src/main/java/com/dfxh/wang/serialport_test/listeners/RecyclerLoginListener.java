package com.dfxh.wang.serialport_test.listeners;

import com.dfxh.wang.serialport_test.beans.RecyclerBean;

public interface RecyclerLoginListener {

    void LoginSuccess(String msg);

    void loginError(String msg);


}
