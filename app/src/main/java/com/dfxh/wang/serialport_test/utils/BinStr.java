package com.dfxh.wang.serialport_test.utils;

import android.text.TextUtils;

import com.google.zxing.common.StringUtils;

/**
 * Created by Axehome_Mr.z on 2018/12/4 10:25
 * $16进制与2进制的转换
 */
public class BinStr {

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private static char szbyte2str[][] = {
            {'0', '0'}, {'0', '1'}, {'0', '2'}, {'0', '3'}, {'0', '4'}, {'0', '5'}, {'0', '6'}, {'0', '7'},
            {'0', '8'}, {'0', '9'}, {'0', 'A'}, {'0', 'B'}, {'0', 'C'}, {'0', 'D'}, {'0', 'E'}, {'0', 'F'},

            {'1', '0'}, {'1', '1'}, {'1', '2'}, {'1', '3'}, {'1', '4'}, {'1', '5'}, {'1', '6'}, {'1', '7'},
            {'1', '8'}, {'1', '9'}, {'1', 'A'}, {'1', 'B'}, {'1', 'C'}, {'1', 'D'}, {'1', 'E'}, {'1', 'F'},

            {'2', '0'}, {'2', '1'}, {'2', '2'}, {'2', '3'}, {'2', '4'}, {'2', '5'}, {'2', '6'}, {'2', '7'},
            {'2', '8'}, {'2', '9'}, {'2', 'A'}, {'2', 'B'}, {'2', 'C'}, {'2', 'D'}, {'2', 'E'}, {'2', 'F'},

            {'3', '0'}, {'3', '1'}, {'3', '2'}, {'3', '3'}, {'3', '4'}, {'3', '5'}, {'3', '6'}, {'3', '7'},
            {'3', '8'}, {'3', '9'}, {'3', 'A'}, {'3', 'B'}, {'3', 'C'}, {'3', 'D'}, {'3', 'E'}, {'3', 'F'},

            {'4', '0'}, {'4', '1'}, {'4', '2'}, {'4', '3'}, {'4', '4'}, {'4', '5'}, {'4', '6'}, {'4', '7'},
            {'4', '8'}, {'4', '9'}, {'4', 'A'}, {'4', 'B'}, {'4', 'C'}, {'4', 'D'}, {'4', 'E'}, {'4', 'F'},

            {'5', '0'}, {'5', '1'}, {'5', '2'}, {'5', '3'}, {'5', '4'}, {'5', '5'}, {'5', '6'}, {'5', '7'},
            {'5', '8'}, {'5', '9'}, {'5', 'A'}, {'5', 'B'}, {'5', 'C'}, {'5', 'D'}, {'5', 'E'}, {'5', 'F'},

            {'6', '0'}, {'6', '1'}, {'6', '2'}, {'6', '3'}, {'6', '4'}, {'6', '5'}, {'6', '6'}, {'6', '7'},
            {'6', '8'}, {'6', '9'}, {'6', 'A'}, {'6', 'B'}, {'6', 'C'}, {'6', 'D'}, {'6', 'E'}, {'6', 'F'},

            {'7', '0'}, {'7', '1'}, {'7', '2'}, {'7', '3'}, {'7', '4'}, {'7', '5'}, {'7', '6'}, {'7', '7'},
            {'7', '8'}, {'7', '9'}, {'7', 'A'}, {'7', 'B'}, {'7', 'C'}, {'7', 'D'}, {'7', 'E'}, {'7', 'F'},

            {'8', '0'}, {'8', '1'}, {'8', '2'}, {'8', '3'}, {'8', '4'}, {'8', '5'}, {'8', '6'}, {'8', '7'},
            {'8', '8'}, {'8', '9'}, {'8', 'A'}, {'8', 'B'}, {'8', 'C'}, {'8', 'D'}, {'8', 'E'}, {'8', 'F'},

            {'9', '0'}, {'9', '1'}, {'9', '2'}, {'9', '3'}, {'9', '4'}, {'9', '5'}, {'9', '6'}, {'9', '7'},
            {'9', '8'}, {'9', '9'}, {'9', 'A'}, {'9', 'B'}, {'9', 'C'}, {'9', 'D'}, {'9', 'E'}, {'9', 'F'},

            {'A', '0'}, {'A', '1'}, {'A', '2'}, {'A', '3'}, {'A', '4'}, {'A', '5'}, {'A', '6'}, {'A', '7'},
            {'A', '8'}, {'A', '9'}, {'A', 'A'}, {'A', 'B'}, {'A', 'C'}, {'A', 'D'}, {'A', 'E'}, {'A', 'F'},

            {'B', '0'}, {'B', '1'}, {'B', '2'}, {'B', '3'}, {'B', '4'}, {'B', '5'}, {'B', '6'}, {'B', '7'},
            {'B', '8'}, {'B', '9'}, {'B', 'A'}, {'B', 'B'}, {'B', 'C'}, {'B', 'D'}, {'B', 'E'}, {'B', 'F'},

            {'C', '0'}, {'C', '1'}, {'C', '2'}, {'C', '3'}, {'C', '4'}, {'C', '5'}, {'C', '6'}, {'C', '7'},
            {'C', '8'}, {'C', '9'}, {'C', 'A'}, {'C', 'B'}, {'C', 'C'}, {'C', 'D'}, {'C', 'E'}, {'C', 'F'},

            {'D', '0'}, {'D', '1'}, {'D', '2'}, {'D', '3'}, {'D', '4'}, {'D', '5'}, {'D', '6'}, {'D', '7'},
            {'D', '8'}, {'D', '9'}, {'D', 'A'}, {'D', 'B'}, {'D', 'C'}, {'D', 'D'}, {'D', 'E'}, {'D', 'F'},

            {'E', '0'}, {'E', '1'}, {'E', '2'}, {'E', '3'}, {'E', '4'}, {'E', '5'}, {'E', '6'}, {'E', '7'},
            {'E', '8'}, {'E', '9'}, {'E', 'A'}, {'E', 'B'}, {'E', 'C'}, {'E', 'D'}, {'E', 'E'}, {'E', 'F'},

            {'F', '0'}, {'F', '1'}, {'F', '2'}, {'F', '3'}, {'F', '4'}, {'F', '5'}, {'F', '6'}, {'F', '7'},
            {'F', '8'}, {'F', '9'}, {'F', 'A'}, {'F', 'B'}, {'F', 'C'}, {'F', 'D'}, {'F', 'E'}, {'F', 'F'}
    };
    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private static char szstr2bin[] = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0, 0, 0, 0, 0,
            0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    public static String byte2str(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            sb.append(szbyte2str[buf[i] & 0xFF][0]);
            sb.append(szbyte2str[buf[i] & 0xFF][1]);
        }
        return sb.toString();
    }

    public static byte[] str2byte(String str) {
        int length = str.length();
        if (length < 1) return null;
        if (length % 2 != 0) return null;
        byte[] result = new byte[str.length() / 2];
        for (int i = 0; i < length; ) {
            char H = szstr2bin[str.charAt(i++) & 0xFF];
            char L = szstr2bin[str.charAt(i++) & 0xFF];
            result[(i / 2) - 1] = (byte) (H * 16 + L);
        }
        return result;
    }

    /**
     * 将byte数组化为十六进制串
     */
    public static final StringBuilder byte2hex(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder(data.length);
        for (byte byteChar : data) {
            stringBuilder.append(String.format("%02X ", byteChar).trim());
        }
        return stringBuilder;
    }

    /**
     * 将byte数组转化成浮点数（4个字节带小数的浮点数）
     */
    public static float byte2int_Float(byte b[]) {
        int bits = b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16 | (b[0] & 0xff) << 24;
        int sign = ((bits & 0x80000000) == 0) ? 1 : -1;
        int exponent = ((bits & 0x7f800000) >> 23);
        int mantissa = (bits & 0x007fffff);
        mantissa |= 0x00800000; // Calculate the result:
        float f = (float) (sign * mantissa * Math.pow(2, exponent - 150));
        return f;
    }

    /**
     * 将十六进制串转化为byte数组
     */
    public static final byte[] hex2byte(String hex) throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }


    /**
     * 16进制的字符串表示转成字节数组
     *
     * @param hexString 16进制格式的字符串
     * @return 转换后的字节数组
     **/
    public static byte[] toByteArray(String hexString) {
        if (TextUtils.isEmpty(hexString))
            throw new IllegalArgumentException("this hexString must not be empty");

        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    /**
     * 将十六进制串转换为二进制
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 将字符串转成ASCII值
     */
    public static String strToASCII(String data) {
        String requestStr = "";
        for (int i = 0; i < data.length(); i++) {
            char a = data.charAt(i);
            int aInt = (int) a;
            requestStr = requestStr + integerToHexString(aInt);
        }
        return requestStr;
    }

    /**
     * 将十进制整数转为十六进制数，并补位
     */
    public static String integerToHexString(int s) {
        String ss = Integer.toHexString(s);
        if (ss.length() % 2 != 0) {
            ss = "0" + ss;//0F格式
        }
        return ss.toUpperCase();
    }

    /**
     * 将二进制转换成十六进制串
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 16进制字符串转成2进制字符串
     *
     * @param hexString
     * @return
     */
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    /**
     * byte数组转成字符串
     */
    public String btye2Str(byte[] data) {
        String str = new String(data);
        return str;
    }

}