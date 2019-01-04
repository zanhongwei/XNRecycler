package utils;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.widget.Toast;

import com.dfxh.wang.serialport_test.listeners.SendUpdateListener;
import com.dfxh.wang.serialport_test.ui.view.CommonProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载应用
 *
 * @author Administrator
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private PowerManager.WakeLock mWakeLock;
    // 下载存储的文件名
    private static final String DOWNLOAD_NAME = "channelWe";
    private CommonProgressDialog pBar;
    private SendUpdateListener listener;

    public DownloadTask(Context context, CommonProgressDialog pBar, SendUpdateListener listener) {
        this.context = context;
        this.pBar = pBar;
        this.listener = listener;
    }


    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        File file = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            // expect HTTP 200 OK, so we don't mistakenly save error
            // report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP "
                        + connection.getResponseCode() + " "
                        + connection.getResponseMessage();
            }
            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                file = new File(Environment.getExternalStorageDirectory(),
                        DOWNLOAD_NAME);

                if (!file.exists()) {
                    // 判断父文件夹是否存在
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                }

            } else {
                Toast.makeText(context, "sd卡未挂载",
                        Toast.LENGTH_LONG).show();
            }
            input = connection.getInputStream();
            output = new FileOutputStream(file);
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);

            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return e.toString();

        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        pBar.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        pBar.setIndeterminate(false);
        pBar.setMax(100);
        pBar.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        pBar.dismiss();
        if (result != null) {

//                // 申请多个权限。大神的界面
//                AndPermission.with(MainActivity.this)
//                        .requestCode(REQUEST_CODE_PERMISSION_OTHER)
//                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                        .rationale(new RationaleListener() {
//                                       @Override
//                                       public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
//                                           // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
//                                           AndPermission.rationaleDialog(MainActivity.this, rationale).show();
//                                       }
//                                   }
//                        )
//                        .send();
            // 申请多个权限。
//            AndPermission.with(MainActivity.this)
//                    .requestCode(REQUEST_CODE_PERMISSION_SD)
//                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                    .rationale(rationaleListener
//                    )
//                    .send();
//
//
//            Toast.makeText(context, "您未打开SD卡权限" + result, Toast.LENGTH_LONG).show();
        } else {
            // Toast.makeText(context, "File downloaded",
            // Toast.LENGTH_SHORT)
            // .show();

            listener.updata();
//            context.update();
        }
    }

}
