package com.dfxh.wang.serialport_test.ui.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.dfxh.wang.serialport_test.BaseActivity;
import com.dfxh.wang.serialport_test.R;
import com.dfxh.wang.serialport_test.beans.LogBean;
import com.dfxh.wang.serialport_test.presenter.BoxManagerActivityPresenter;
import com.dfxh.wang.serialport_test.ui.view.SelfDialog;
import com.dfxh.wang.serialport_test.utils.CountDownTimerUtils;
import com.dfxh.wang.serialport_test.utils.SpUtil;
import com.dfxh.wang.serialport_test.view.BoxManagerView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPort;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import utils.SerialPortUtils;

public class BoxManagerActivity extends BaseActivity implements BoxManagerView {

    @BindView(R.id.tv_tb_phone)
    TextView tvTbPhone;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_metal_name)
    TextView tvMetalName;
    @BindView(R.id.tv_metal_price)
    TextView tvMetalPrice;
    @BindView(R.id.tv_plastics_name)
    TextView tvPlasticsName;
    @BindView(R.id.tv_plastics_price)
    TextView tvPlasticsPrice;
    @BindView(R.id.tv_textile_name)
    TextView tvTextileName;
    @BindView(R.id.tv_textile_price)
    TextView tvTextilePrice;
    @BindView(R.id.tv_bottle_name)
    TextView tvBottleName;
    @BindView(R.id.tv_bottle_price)
    TextView tvBottlePrice;
    @BindView(R.id.tv_paper_name)
    TextView tvPaperName;
    @BindView(R.id.tv_paper_price)
    TextView tvPaperPrice;
    @BindView(R.id.tv_harmful_waste)
    TextView tvHarmfulWaste;
    @BindView(R.id.tv_glass)
    TextView tvGlass;
    @BindView(R.id.convenient)
    ConvenientBanner convenient;
    @BindView(R.id.tv_company_num)
    TextView tvCompanyNum;
    @BindView(R.id.tv_time)
    TextView tvTime;


    private Unbinder bind;
    private SoundPool soundPool;
    private int load;
    private String sort = "";
    private String code = "";
    private LoadingDailog dialog;
    private BoxManagerActivityPresenter mPresenter;
    private CountDownTimerUtils countDownTimerUtils;

    private boolean metalCanOpen = false;
    private boolean plasticCanOpen = false;
    private boolean textileCanOpen = false;
    private boolean bottleCanOpen = false;
    private boolean paperCanOpen = false;
    private boolean wasteCanOpen = false;
    private boolean glassCanOpen = false;
    private boolean canNext = false;

    private SerialPort sp;
    private SerialPortUtils serialPortUtils;
    private List<LogBean> logBeans = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("aaa", "(BoxManagerActivity.java:104)<---->" + "run BoxManagerActivity  fun onCreate() ");
        setContentView(R.layout.activity_box_manager);
        bind = ButterKnife.bind(this);
        /* 初始化界面 */
        initView();
        /* 初始化界面逻辑 */
        initData();

    }

    private void initView() {
        ivRight.setImageResource(R.drawable.xiaochengxuma);//设置小程序码
        canNext = false;

        countDownTimerUtils = new CountDownTimerUtils(tvTime, 119000, 1000, new CountDownTimerUtils.MyListener() {
            @Override
            public void refresh() {
                startActivity(new Intent(BoxManagerActivity.this, ScreenActivity.class));
                finish();
            }
        });
        soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 5);
        //加载资源
        load = soundPool.load(this, R.raw.change_sort, 1);
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                .setMessage("加载中...")
                .setCancelable(true)
                .setCancelOutside(false);
        dialog = loadBuilder.create();
        serialPortUtils = new SerialPortUtils();
        mPresenter = new BoxManagerActivityPresenter(this);
    }

    private void initData() {
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                soundPool.play(load, 1, 1, 1, 0, 1);//播放
            }
        });

        mPresenter.openSerialPort();
        countDownTimerUtils.start();
        upLoadLog(new Gson().toJson(new LogBean(System.currentTimeMillis(), "打开页面选择界面")));
    }

    @OnClick({R.id.iv_center, R.id.ll_metal, R.id.ll_plastics, R.id.ll_textile, R.id.ll_bottle, R.id.ll_paper, R.id.ll_harmful_waste, R.id.ll_glass, R.id.tv_recycle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_center:
                break;
            case R.id.ll_metal:
                if (canNext) {
                    sort = "metal";
                    code = "01050CC9FF005F54";
                    if (metalCanOpen)
                        mPresenter.startActivity();
                    else
                        showMyLoading("metal");
                } else {
                    Toast.makeText(this, "请稍等一下", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ll_plastics:

                if (canNext) {
                    sort = "plastics";
                    code = "01050CCBFF00FE94";
                    if (plasticCanOpen)
                        mPresenter.startActivity();
                    else
                        showMyLoading("plastics");
                } else {
                    Toast.makeText(this, "请稍等一下", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ll_textile:
                if (canNext) {
                    sort = "textile";
                    code = "01050CCDFF001E95";
                    if (textileCanOpen) {
                        mPresenter.startActivity();
                    } else
                        showMyLoading("textile");
                } else {
                    Toast.makeText(this, "请稍等一下", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ll_bottle:
                if (canNext) {
                    sort = "bottle";
                    code = "01050CD5FF009E92";
                    if (bottleCanOpen)
                        mPresenter.startActivity();
                    else showMyLoading("bottle");
                } else {
                    Toast.makeText(this, "请稍等一下", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_paper:
                if (canNext) {
                    sort = "paper";
                    code = "01050CCFFF00BF55";
                    if (paperCanOpen)
                        mPresenter.startActivity();
                    else showMyLoading("paper");
                } else {
                    Toast.makeText(this, "请稍等一下", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ll_harmful_waste:
                if (canNext) {
                    sort = "waste";
                    code = "01050CD3FF007E93";
                    if (wasteCanOpen)
                        mPresenter.startActivity();
                    else showMyLoading("waste");
                } else {
                    Toast.makeText(this, "请稍等一下", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_glass:
                if (canNext) {
                    sort = "glass";
                    code = "01050CD1FF00DF53";
                    if (glassCanOpen)
                        mPresenter.startActivity();
                    else showMyLoading("glass");
                } else {
                    Toast.makeText(this, "请稍等一下", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.tv_recycle:
                countDownTimerUtils.cancel();
                startActivity(new Intent(this, RecyclerLoginActivity.class));
                mPresenter.closeSerialPort();
                finish();
                break;
        }


    }

    private void showMyLoading(String flag) {

        final SelfDialog selfDialog = new SelfDialog(this);

        selfDialog.setYesOnclickListener("", new SelfDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                selfDialog.dismiss();

            }
        });

        selfDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                backgroundAlpha(1f);
            }
        });

        selfDialog.show();
        backgroundAlpha(0.6f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        soundPool.release();
        soundPool = null;
        if (countDownTimerUtils != null) {
            countDownTimerUtils.cancel();
            countDownTimerUtils = null;
        }
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getSort() {
        return sort;
    }

    @Override
    public void startActivitySuccess() {

        if (countDownTimerUtils != null) countDownTimerUtils.cancel();
        countDownTimerUtils = null;
        startActivity(new Intent(this, ScanCodeActivity.class)
                .putExtra("code", code)
                .putExtra("sort", sort)
        );
        mPresenter.closeSerialPort();
        finish();
    }

    @Override
    public void startActivityError() {
        Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        try {
            if (!dialog.isShowing()) dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideLoading() {
        try {
            if (dialog.isShowing()) dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openSerialPort(SerialPort sp) {
        this.sp = sp;
        mPresenter.sendSerialPort("010305E8000A4535");
        mPresenter.readData();
        canNext = true;
    }

    @Override
    public void closeSerialPort(String msg) {
        //关闭串口
    }

    @Override
    public void readDataSuccess(String sData) {
        logBeans.add(new LogBean(System.currentTimeMillis(), "下位机返回来的读取状态的string" + sData));
        SpUtil.putList(this, "logs", logBeans);
        upLoadLog("下位机返回来的---读取状态----的string" + sData);
        canNext = true;
        boolean[] booleans = mPresenter.handleData(sData);
        metalCanOpen = booleans[0];
        plasticCanOpen = booleans[1];
        textileCanOpen = booleans[2];
        bottleCanOpen = booleans[3];
        paperCanOpen = booleans[4];
        wasteCanOpen = booleans[5];
        glassCanOpen = booleans[6];

        String num = "";

        if (!metalCanOpen) {
            num = "1";
        }
        if (!plasticCanOpen) {
            num = num + "2";
        }
        if (!textileCanOpen) {
            num = num + "3";
        }
        if (!bottleCanOpen) {
            num = num + "4";
        }
        if (!paperCanOpen) {
            num = num + "5";
        }
        if (!wasteCanOpen) {
            num = num + "7";
        }
        if (!glassCanOpen) {
            num = num + "6";
        }

        Log.e("aaa", "(BoxManagerActivity.java:386)<---->" + num);
        if (!TextUtils.isEmpty(num))
            mPresenter.sendShortMessage(num);
//        next();

    }

    @Override
    public void readDataError() {

    }

    @Override
    public SerialPort vertifySerialPort() {
        return this.sp;
    }

    @Override
    public SerialPortUtils senSPUtils() {
        return serialPortUtils;
    }

    @Override
    public void successListener(String msg) {
        //下位机数据发送成功
    }

    @Override
    public void errorListener(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 读取状态成功
     */
    private void next() {

        switch (sort) {
            case "metal":
                if (metalCanOpen) mPresenter.startActivity();
                else showMyLoading("metal");
                break;
            case "plastics":
                if (plasticCanOpen) mPresenter.startActivity();
                else showMyLoading("plastics");
                break;
            case "textile":
                if (textileCanOpen) mPresenter.startActivity();
                else showMyLoading("textile");
                break;
            case "bottle":
                if (bottleCanOpen) mPresenter.startActivity();
                else showMyLoading("bottle");
                break;
            case "paper":
                if (paperCanOpen) mPresenter.startActivity();
                else showMyLoading("paper");
                break;
            case "glass":
                if (glassCanOpen) mPresenter.startActivity();
                else showMyLoading("glass");
                break;
            case "waste":
                if (wasteCanOpen) mPresenter.startActivity();
                else showMyLoading("waste");
                break;
        }


    }

}
