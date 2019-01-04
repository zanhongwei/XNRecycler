package com.dfxh.wang.serialport_test.utils;

import android.os.Build;
import android.view.KeyEvent;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.dfxh.wang.serialport_test.BaseActivity;
import com.dfxh.wang.serialport_test.ui.activitys.MainActivity;

public class CloseBarUtil {
    /**
     * 关闭底部导航条
     */
    public static void closeBar() {
        try {
            // 需要root 权限
            Build.VERSION_CODES vc = new Build.VERSION_CODES();
            Build.VERSION vr = new Build.VERSION();
            String ProcID = "79";
            if (vr.SDK_INT >= vc.ICE_CREAM_SANDWICH) {
                ProcID = "42"; // ICS AND NEWER
            }
            // 需要root 权限
            Process proc = Runtime.getRuntime().exec(
                    new String[]{
                            "su",
                            "-c",
                            "service call activity " + ProcID
                                    + " s16 com.android.systemui"}); // WAS 79
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示底部导航条
     */
    public static void showBar() {
        try {
            Process proc = Runtime.getRuntime().exec(
                    new String[]{"am", "startservice", "-n",
                            "com.android.systemui/.SystemUIService"});
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
