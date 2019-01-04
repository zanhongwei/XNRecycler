package utils;

import android.text.TextUtils;
import android.util.Log;

import com.dfxh.wang.serialport_test.utils.BytesHexStrTranslate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import android_serialport_api.SerialPort;

/**
 * Created by WangChaowei on 2017/12/7.
 */

public class SerialPortUtils {

    private final String TAG = "SerialPortUtils";
    private String path = "/dev/ttyS2";
    private int baudrate = 9600;
    public boolean serialPortStatus = false; //是否打开串口标志
    public String data_;
    public boolean threadStatus; //线程状态，为了安全终止线程

    public SerialPort serialPort = null;
    public InputStream inputStream = null;
    public OutputStream outputStream = null;
    public ChangeTool changeTool = new ChangeTool();
    public OnDataReceiveListener onDataReceiveListener = null;
    public OnDataReceiveListenerS onDataReceiveListeners = null;
    public String flag;
    private String sData;
    private ReadThread readThread;


    public SerialPortUtils(OnDataReceiveListener onDataReceiveListener) {
        this.onDataReceiveListener = onDataReceiveListener;

        if (onDataReceiveListener == null) {
            Log.e(TAG, "SerialPortUtils: 一个参数的构造方法内的onDataReceiveListener == null");
        }
    }

    public SerialPortUtils(String flag) {
        this.flag = flag;
    }

    public SerialPortUtils() {
    }

    /**
     * 打开串口
     *
     * @return serialPort串口对象
     */
    public SerialPort openSerialPort() {
        try {
            serialPort = new SerialPort(new File(path), baudrate, 0);
            this.serialPortStatus = true;
            //获取打开的串口中的输入输出流，以便于串口数据的收发
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();

//            readThread = new ReadThread();
        } catch (IOException e) {
            Log.e(TAG, "openSerialPort: 打开串口异常：" + e.toString());


            return serialPort;
        }
        Log.d(TAG, "openSerialPort: 打开串口");
        return serialPort;
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        try {
            inputStream.close();
            outputStream.close();

            this.serialPortStatus = false;
            this.threadStatus = true; //线程状态
            serialPort.close();
        } catch (IOException e) {
            Log.e(TAG, "closeSerialPort: 关闭串口异常：" + e.toString());
            return;
        }

        Log.d(TAG, "closeSerialPort: 关闭串口成功");
    }

    /**
     * 发送串口指令（字符串）
     *
     * @param data String数据指令
     */
    public void sendSerialPort(String data) {
        Log.e(TAG, "sendSerialPort: 发送数据");

        try {
            byte[] sendData = BytesHexStrTranslate.toBytes(data); //string转byte[]
//            byte[] sendData = data.getBytes(); //string转byte[]

            this.data_ = new String(sendData); //byte[]转string
            Log.e(TAG, "sendSerialPort: 发送的串口数据为 ===  " + data_);
            Log.e(TAG, "sendSerialPort: 打印的字节为 === " + sendData.toString());


            if (sendData.length > 0) {
                outputStream.write(sendData);
//                outputStream.write('\n');
                //outputStream.write('\r'+'\n');
                outputStream.flush();
                Log.d(TAG, "sendSerialPort: 串口数据发送成功");
            }
//            byte[] buffer = new byte[64];
//            int size; //读取数据的大小
//            try {
//                size = inputStream.read(buffer);
//                if (size > 0) {
//                    Log.e(TAG, "run: 接收到了数据：" + changeTool.ByteArrToHex(buffer));
//                    Log.e(TAG, "run: 接收到了数据大小：" + String.valueOf(size));
//
//                    if (flag.equals("scode")) {
//                        if (onDataReceiveListener == null)
//                            Log.e(TAG, "run: onDataReceiveListener == null");
//                        else
//                            onDataReceiveListener.onDataReceive(buffer, size);
//                    } else if (flag.equals("success")) {
//                        if (onDataReceiveListeners == null)
//                            Log.e(TAG, "run: onDataReceiveListener == null");
//                        else
//                            onDataReceiveListeners.onDataReceive(buffer, size);
//                    } else {
//                        if (onDataReceiveListener == null)
//                            Log.e(TAG, "run: onDataReceiveListener == null");
//                        else
//                            onDataReceiveListener.onDataReceive(buffer, size);
//                    }
////                        if (onDataReceiveListener == null)
////                            Log.e(TAG, "run: onDataReceiveListener == null");
////                        else
////                            onDataReceiveListener.onDataReceive(buffer, size);
//                }else {
//
//                }
//            } catch (IOException e) {
//                Log.e(TAG, "run: 数据读取异常：" + e.toString());
//            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "sendSerialPort: 串口数据发送失败：" + e.getMessage());
        }

    }

    /**
     * 单开一线程，来读数据
     */
    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            //判断进程是否在运行，更安全的结束进程
            while (!threadStatus) {
                Log.d(TAG, "进入线程run");
                //64   1024
                byte[] buffer = new byte[64];
                int size; //读取数据的大小
                try {
                    size = inputStream.read(buffer);
                    if (size > 0) {
                        Log.e(TAG, "run: 接收到了数据：" + changeTool.ByteArrToHex(buffer));
                        Log.e(TAG, "run: 接收到了数据大小：" + String.valueOf(size));

                        threadStatus = true;
                        sData = BytesHexStrTranslate.bytesToHexFun1(buffer);
//                        if (flag.equals("scode")) {
////                            if (onDataReceiveListener == null)
////                                Log.e(TAG, "run: onDataReceiveListener == null");
////                            else
////                                onDataReceiveListener.onDataReceive(buffer, size);
////                        } else if (flag.equals("success")) {
////                            if (onDataReceiveListeners == null)
////                                Log.e(TAG, "run: onDataReceiveListener == null");
////                            else
////                                onDataReceiveListeners.onDataReceive(buffer, size);
////                        } else {
////                            if (onDataReceiveListener == null)
////                                Log.e(TAG, "run: onDataReceiveListener == null");
////                            else
////                                onDataReceiveListener.onDataReceive(buffer, size);
////                        }
////
                    }
                } catch (IOException e) {
                    Log.e(TAG, "run: 数据读取异常：" + e.toString());
                }
            }

        }
    }

    /**
     * 打开串口，不带线程
     * Date:20181025
     * Auth By:axej
     */
    public SerialPort openSerialPort_noThread() {
        try {
            serialPort = new SerialPort(new File(path), baudrate, 0);
            this.serialPortStatus = true;
            threadStatus = false; //线程状态

            //获取打开的串口中的输入输出流，以便于串口数据的收发
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();

        } catch (IOException e) {
            Log.e(TAG, "openSerialPort: 打开串口异常：" + e.toString());

            return serialPort;
        }
        Log.d(TAG, "openSerialPort: 打开串口");
        return serialPort;
    }


    /**
     * 关闭串口，不带线程
     * Date:20181025
     * Auth By axej
     */
    public void closeSerialPort_noThread() {
        try {
            inputStream.close();
            outputStream.close();

            this.serialPortStatus = false;
            this.threadStatus = true; //线程状态

        } catch (IOException e) {
            Log.e(TAG, "closeSerialPort: 关闭串口异常：" + e.toString());
            return;
        }

        Log.d(TAG, "closeSerialPort: 关闭串口成功");
    }


    /**
     * 读取串口数据，不在线程中使用
     * Data 20181025
     * Auth by axej
     */
    public String readSerialPort() {
        Log.e("aaa",
                "(SerialPortUtils.java:263)<--readSerialPort-->" + "执行readSerialPort()");
        byte[] buffer = new byte[64];
        int size; //读取数据的大小
        try {
            size = inputStream.read(buffer);
            Log.e("aaa",
                    "(SerialPortUtils.java:269)<--inputStream-->" + "执行inputStream()");
            if (size > 0) {
                Log.e(TAG, "run: 接收到了数据：" + changeTool.ByteArrToHex(buffer));
                Log.e(TAG, "run: 接收到了数据大小：" + String.valueOf(size));

                return BytesHexStrTranslate.bytesToHexFun1(buffer);
            } else {
                return "";
            }

        } catch (IOException e) {
            Log.e(TAG, "run: 数据读取异常：" + e.toString());
            return "";
        }
    }

    public String readSerialPortWithThread() {
        threadStatus = false; //线程状态
        if (!readThread.isAlive())
            readThread.start();
//        new ReadThread().start(); //开始线程监控是否有数据要接收

        if (!TextUtils.isEmpty(sData)) {
            readThread.stop();
            return sData;
        } else {
            return "";
        }
    }


    public interface OnDataReceiveListener {
        void onDataReceive(byte[] buffer, int size);
    }

    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }

    public interface OnDataReceiveListenerS {
        void onDataReceive(byte[] buffer, int size);
    }

    public void setOnDataReceiveListeners(OnDataReceiveListenerS onDataReceiveListeners) {
        this.onDataReceiveListeners = onDataReceiveListeners;
    }
}
