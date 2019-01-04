package com.dfxh.wang.serialport_test.ui.activitys;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.dfxh.wang.serialport_test.BaseActivity;
import com.dfxh.wang.serialport_test.R;
import com.dfxh.wang.serialport_test.beans.LogBean;
import com.dfxh.wang.serialport_test.ui.adapters.LogRecordAdapter;
import com.dfxh.wang.serialport_test.utils.SpUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogRecordActivity extends BaseActivity {

    @BindView(R.id.tv_tb_phone)
    TextView tvTbPhone;
    @BindView(R.id.lv_log)
    ListView lvLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_record);
        ButterKnife.bind(this);

        List<LogBean> logs = SpUtil.getList(this, "logs");
        LogRecordAdapter logRecordAdapter = new LogRecordAdapter(this, logs);
        lvLog.setAdapter(logRecordAdapter);
    }

    @OnClick(R.id.iv_right)
    public void onViewClicked() {
        finish();
    }
}
