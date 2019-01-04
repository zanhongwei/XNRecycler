package com.dfxh.wang.serialport_test.utils;

import android.util.Log;

public class BytesHexStrTranslate {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 方法一：
     * byte[] to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexFun1(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for (byte b : bytes) { // 使用除与取余进行转换
            if (b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }

            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }

        return new String(buf);
    }

    /**
     * 方法二：
     * byte[] to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexFun2(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return new String(buf);
    }

    /**
     * 方法三：
     * byte[] to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexFun3(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }

        return buf.toString();
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
            Log.e("aaa",
                    "(BytesHexStrTranslate.java:84)<---->" + bytes[i]);
        }

        return bytes;
    }


    /**
     * String字符串转二进制输出
     * @param hex
     * @return String
     * @author axej-20181019
     */
    public static String hexStringToByte(String hex) {
        String str_result="";
        int len = (hex.length());
        byte[] by_result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            by_result[i] = (byte) (toByte(achar[i]));
            str_result+=String.format("%04d", Long.parseLong(Long.toBinaryString(by_result[i])));
        }

        return str_result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

//    char HexChar(char c) {
//        if((c>='0')&&(c<='9'))
//            return (char) (c-'0');     //将0-9的数字字符转为十六进制格式
//        else if((c>='A')&&(c<='F'))
//            return (char) (c-'A'+10);  //将A-F的字符转为十六进制格式，例如字符'C'-'A'+10=12=0x0C
//        else if((c>='a')&&(c<='f'))
//            return (char) (c-'a'+10);   //将a-f的字符转为十六进制格式
//        else
//            return 0x10;
//    }
//
//    int Str2Hex(String str, BYTE *data) {
//        int t,t1;
//        int rlen=0,len=str.length();
//        if(len==1)
//        {
//            char h=str[0];
//            t=HexChar(h);
//            data[0]=(BYTE)t;
//            rlen++;
//        }
//        //data.SetSize(len/2);
//        for(int i=0;i<len;)
//        {
//            char l,h=str[i];
//            if(h==' ')
//            {
//                i++;
//                continue;
//            }
//            i++;
//            if(i>=len)
//                break;
//            l=str[i];
//            t=HexChar(h);
//            t1=HexChar(l);
//            if((t==16)||(t1==16))//判断为非法的16进制数
//                break;
//            else
//                t=t*16+t1;
//            i++;
//            data[rlen]=(BYTE)t;
//            rlen++;
//        }
//        return rlen;
//    }

}
