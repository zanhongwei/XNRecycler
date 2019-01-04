package com.dfxh.wang.serialport_test.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dfxh.wang.serialport_test.ui.activitys.BoxManagerActivity;
import com.dfxh.wang.serialport_test.ui.activitys.ScreenActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {

    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("aaa",
                "(BootBroadcastReceiver.java:17)<--接收到广播为-->" + intent.getAction());
        if (intent.getAction().equals(ACTION)) {
            Intent mainActivityIntent = new Intent(context, ScreenActivity.class);  // 要启动的Activity
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
        }
    }
}