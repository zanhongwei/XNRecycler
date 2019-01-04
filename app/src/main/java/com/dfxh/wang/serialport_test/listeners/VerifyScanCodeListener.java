package com.dfxh.wang.serialport_test.listeners;

import com.dfxh.wang.serialport_test.beans.ScanCodeStateBean;

public interface VerifyScanCodeListener {

    void scanSuccess(ScanCodeStateBean bean);

    void scanError(String msg);
}
