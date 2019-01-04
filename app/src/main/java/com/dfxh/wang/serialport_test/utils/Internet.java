package com.dfxh.wang.serialport_test.utils;

public class Internet {

    public static final String BASE_URL = "https://app.tjhengrun.cn/minOX/";//项目基地址

    public static String login = BASE_URL + "currentTime/login.do";

    //获取当前时间戳
    public static String get_timeStemp = BASE_URL + "currentTime/begin.do";

    //判断是否扫码成功
    public static String scan_code = BASE_URL + "currentTime/again.do";

    //上传数据
    public static String upload_data = BASE_URL + "Good/insert.do";

    //上传不带数据
    public static String upload_without_data = BASE_URL + "currentTime/finish.do";

    //更新版本
    public static String updata_apk = BASE_URL + "GetSys.do";


    public static String upload_log_msg = BASE_URL + "currentTime/saveAsFileWriter.do";

    public static String send_message = BASE_URL + "ShortMessage/go.do";

    public static String unLogin = BASE_URL + "currentTime/close.do";


}
