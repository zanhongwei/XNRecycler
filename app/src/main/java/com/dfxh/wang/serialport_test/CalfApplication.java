package com.dfxh.wang.serialport_test;

import android.app.Activity;
import android.app.Application;

import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.LinkedList;
import java.util.List;

public class CalfApplication extends Application {

    public static CalfApplication instance = null;
    private List<Activity> activityList = new LinkedList<Activity>();
    public static double value = 0.0;

    @Override
    public void onCreate() {
        super.onCreate();

        AutoLayoutConifg.getInstance().useDeviceSize();

//        RuntimeExec.getInstance().runRootCmd("settings put global policy_control immersive.full=*");
    }

    //单例模式中获取唯一的MyApplication实例
    public static CalfApplication getInstance() {
        if (null == instance) {
            instance = new CalfApplication();
        }
        return instance;
    }

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

}
