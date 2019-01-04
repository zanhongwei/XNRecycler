package com.dfxh.wang.serialport_test.beans;

import java.util.List;

public class ApkVersion {


    /**
     * code : 200
     * msg : 线上系统版本号
     * sys_type : [{"sys_id":1,"sys_name":"minox_sys","sys_number":"12423567","sys_path":"uploads/1234.txt"}]
     */

    private String code;
    private String msg;
    private List<SysTypeBean> sys_type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<SysTypeBean> getSys_type() {
        return sys_type;
    }

    public void setSys_type(List<SysTypeBean> sys_type) {
        this.sys_type = sys_type;
    }

    public static class SysTypeBean {
        /**
         * sys_id : 1
         * sys_name : minox_sys
         * sys_number : 12423567
         * sys_path : uploads/1234.txt
         */

        private int sys_id;
        private String sys_name;
        private String sys_number;
        private String sys_path;

        public int getSys_id() {
            return sys_id;
        }

        public void setSys_id(int sys_id) {
            this.sys_id = sys_id;
        }

        public String getSys_name() {
            return sys_name;
        }

        public void setSys_name(String sys_name) {
            this.sys_name = sys_name;
        }

        public String getSys_number() {
            return sys_number;
        }

        public void setSys_number(String sys_number) {
            this.sys_number = sys_number;
        }

        public String getSys_path() {
            return sys_path;
        }

        public void setSys_path(String sys_path) {
            this.sys_path = sys_path;
        }
    }
}
