package com.dfxh.wang.serialport_test.beans;

import java.io.Serializable;

public class LogBean implements Serializable {

    private String step;//步骤

    private long time;//记录时间

    private String event;//记录事件

    private String reserve1; //备用1

    private String reserve2; //备用2

    private String reserve3; //备用3

    public LogBean(long time, String event) {
        this.time = time;
        this.event = event;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }
}
