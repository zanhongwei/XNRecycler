package com.dfxh.wang.serialport_test.utils;

import com.dfxh.wang.serialport_test.CalfApplication;

import java.text.DecimalFormat;

public class AnalyseDataUtil {

    public static double analyseData(String data, String sort) {

        double value = 0.0;

        String weight = "";
        switch (sort) {
            case "metal"://金属
                weight = data.substring(6, 14);
                break;
            case "plastics"://塑料
                weight = data.substring(14, 22);
                break;
            case "textile"://纺织物
                weight = data.substring(22, 30);
                break;
            case "paper"://纸张
                weight = data.substring(30, 38);
                break;
            case "bottle"://瓶子
                weight = data.substring(38, 42);
                break;
        }

        if (sort.equals("bottle")){
            value = Double.parseDouble(weight);
        }else {
            float metalF1 = Float.intBitsToFloat(Integer.valueOf(weight, 16));
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String metalWeight1 = decimalFormat.format(metalF1);
            value = Double.valueOf(metalWeight1);
        }



        return value;
    }

    public static String calculateData(double unitPrice, String data, String sort) {
        double value = analyseData(data, sort);
        double money = 0.0;
        switch (sort) {
            case "bottle":
                money = unitPrice * value;
                break;
            default:
                money = unitPrice * (value - CalfApplication.value);
                break;
        }

        DecimalFormat decimalFormat3 = new DecimalFormat("0.00");
        String money2 = decimalFormat3.format(money);
        return money2;
    }

    public static void handleMsg(String str){




    }


}
