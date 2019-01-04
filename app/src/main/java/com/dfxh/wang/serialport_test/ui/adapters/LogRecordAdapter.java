package com.dfxh.wang.serialport_test.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfxh.wang.serialport_test.R;
import com.dfxh.wang.serialport_test.beans.LogBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogRecordAdapter extends BaseAdapter {

    private Context context;
    private List<LogBean> mList;

    public LogRecordAdapter(Context context, List<LogBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (view == null) {
            view = View.inflate(context, R.layout.item_log_record_lv, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Date date = new Date(mList.get(i).getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss

        holder.tvTime.setText("执行时间： " + simpleDateFormat.format(date));
        holder.tvEvent.setText(mList.get(i).getEvent());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_event)
        TextView tvEvent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
