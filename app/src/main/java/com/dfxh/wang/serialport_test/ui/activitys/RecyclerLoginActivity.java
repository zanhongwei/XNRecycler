package com.dfxh.wang.serialport_test.ui.activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.dfxh.wang.serialport_test.BaseActivity;
import com.dfxh.wang.serialport_test.R;
import com.dfxh.wang.serialport_test.beans.ApkVersion;
import com.dfxh.wang.serialport_test.beans.RecyclerBean;
import com.dfxh.wang.serialport_test.listeners.SendUpdateListener;
import com.dfxh.wang.serialport_test.presenter.RecyclerLoginPrecenter;
import com.dfxh.wang.serialport_test.ui.view.CommonProgressDialog;
import com.dfxh.wang.serialport_test.utils.CountDownTimerUtils;
import com.dfxh.wang.serialport_test.utils.Internet;
import com.dfxh.wang.serialport_test.utils.MyUtils;
import com.dfxh.wang.serialport_test.view.RecyclerLoginView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.DownloadTask;

public class RecyclerLoginActivity extends BaseActivity implements RecyclerLoginView {

    // 下载存储的文件名
    private static final String DOWNLOAD_NAME = "channelWe";
    @BindView(R.id.tv_tb_phone)
    TextView tvTbPhone;
    @BindView(R.id.tv_company_num)
    TextView tvCompanyNum;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_login_code)
    ImageView ivLoginCode;
    @BindView(R.id.tv_time)
    TextView tvTime;
    private RecyclerLoginPrecenter mPresenter;
    private LoadingDailog dialog;
    private CommonProgressDialog pBar;
    private Timer timer;
    private TimerTask task;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mPresenter.login();
        }
    };
    private CountDownTimerUtils countDownTimerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_login);
        ButterKnife.bind(this);
        initView();
        mPresenter = new RecyclerLoginPrecenter(this);
        mPresenter.updateApk();
        Bitmap loginImgCode = mPresenter.createLoginImgCode();
        ivLoginCode.setImageBitmap(loginImgCode);

        //定时器获取登录扫码状态
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 2000, 2000);
        countDownTimerUtils = new CountDownTimerUtils(tvTime, 119000, 1000, new CountDownTimerUtils.MyListener() {
            @Override
            public void refresh() {
                startActivity(new Intent(RecyclerLoginActivity.this, ScreenActivity.class));
                finish();
            }
        });
        countDownTimerUtils.start();
    }

    private void initView() {
        ivRight.setImageResource(R.drawable.fanhuishouye);
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                .setMessage("加载中...")
                .setCancelable(true)
                .setCancelOutside(true);
        dialog = loadBuilder.create();

    }

    @OnClick({R.id.iv_right, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                countDownTimerUtils.cancel();
                timer.cancel();
                finish();
                break;
            case R.id.btn_login:
//                mPresenter.login();
                break;
        }
    }

    @Override
    public String getAccount() {

        String account = etAccount.getText().toString().trim();
        return account;
    }

    @Override
    public String getPassWord() {
        String password = etPassword.getText().toString().trim();
        return password;
    }

    @Override
    public void loginSuccess(String recyclerBean) {
        // TODO: 2018/9/29 暂时假设回收员登录
        countDownTimerUtils.cancel();
        timer.cancel();
        Intent intent = new Intent(this, ManagerActivity.class);
        startActivity(intent);
        mPresenter.unLogin();
//        startActivity();
    }

    @Override
    public void loginFail(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loading() {

        try {

            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideLoading() {

        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void needUpdate(String url) {
//        ShowDialog();
        showDialog("有新版本需要更新", url);
    }

    @Override
    public void donotUpdata(String url) {

    }

    @Override
    public String versionCode() {
        String version = MyUtils.getVersion(this);
        return String.valueOf(version);
    }

    @Override
    public Context getContext() {
        return RecyclerLoginActivity.this;
    }

    /**
     * 升级系统
     *
     * @param content
     * @param url
     */
    private void showDialog(String content, final String url) {
//        final MaterialDialog dialog = new MaterialDialog(this);//自定义的对话框，可以呀alertdialog
//        dialog.content(content).btnText("取消", "更新").title("版本更新 ")
//                .titleTextSize(15f).show();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setOnBtnClickL(new OnBtnClickL() {// left btn click listener
//            @Override
//            public void onBtnClick() {
//                dialog.dismiss();
//            }
//        }, new OnBtnClickL() {// right btn click listener
//
//            @Override
//            public void onBtnClick() {
//                dialog.dismiss();
//                // pBar = new ProgressDialog(MainActivity.this,
//                // R.style.dialog);
//                pBar = new CommonProgressDialog(MainActivity.this);
//                pBar.setCanceledOnTouchOutside(false);
//                pBar.setTitle("正在下载");
//                pBar.setCustomTitle(LayoutInflater.from(
//                        MainActivity.this).inflate(
//                        R.layout.title_dialog, null));
//                pBar.setMessage("正在下载");
//                pBar.setIndeterminate(true);
//                pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                pBar.setCancelable(true);
//                // downFile(URLData.DOWNLOAD_URL);
//                final DownloadTask downloadTask = new DownloadTask(
//                        MainActivity.this);
//                downloadTask.execute(url);
//                pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        downloadTask.cancel(true);
//                    }
//                });
//            }
//        });

        new android.app.AlertDialog.Builder(this)
                .setTitle("版本更新")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        pBar = new CommonProgressDialog(RecyclerLoginActivity.this);
                        pBar.setCanceledOnTouchOutside(false);
                        pBar.setTitle("正在下载");
                        pBar.setCustomTitle(LayoutInflater.from(
                                RecyclerLoginActivity.this).inflate(
                                R.layout.title_dialog, null));
                        pBar.setMessage("正在下载");
                        pBar.setIndeterminate(true);
                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pBar.setCancelable(true);
                        // downFile(URLData.DOWNLOAD_URL);
                        final DownloadTask downloadTask = new DownloadTask(
                                RecyclerLoginActivity.this, pBar, new SendUpdateListener() {
                            @Override
                            public void updata() {
                                update();
                            }
                        });
                        downloadTask.execute(Internet.BASE_URL + url);
                        pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                downloadTask.cancel(true);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void update() {
        //安装应用
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), DOWNLOAD_NAME)),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimerUtils != null) countDownTimerUtils.cancel();
        if (timer != null) timer.cancel();
    }
}
