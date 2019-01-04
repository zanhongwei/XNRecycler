package com.dfxh.wang.serialport_test.listeners;

import android.graphics.Bitmap;

public interface QRCodeGenderListener {


    void ImageGenderSuccess(Bitmap bitmap);

    void ImageGenderError(String msg);
}
