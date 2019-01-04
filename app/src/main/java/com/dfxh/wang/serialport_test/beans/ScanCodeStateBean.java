package com.dfxh.wang.serialport_test.beans;

import java.io.Serializable;

public class ScanCodeStateBean implements Serializable{


    /**
     * result : 1
     * msg : success
     * data : {"id":1,"device_id":"1234567890","device_state":"1","device_dress":"xxx","user_id":0}
     */

    private int result;
    private String msg;
    private DataBean data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 1
         * device_id : 1234567890
         * device_state : 1
         * device_dress : xxx
         * user_id : 0
         */

        private int id;
        private String device_id;
        private String device_state;
        private String device_dress;
        private int user_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getDevice_state() {
            return device_state;
        }

        public void setDevice_state(String device_state) {
            this.device_state = device_state;
        }

        public String getDevice_dress() {
            return device_dress;
        }

        public void setDevice_dress(String device_dress) {
            this.device_dress = device_dress;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }
}
