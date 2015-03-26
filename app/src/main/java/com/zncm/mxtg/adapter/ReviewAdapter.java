package com.zncm.mxtg.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.malinskiy.materialicons.widget.IconTextView;
import com.zncm.mxtg.R;
import com.zncm.mxtg.data.BaseData;

import java.util.List;

public abstract class ReviewAdapter extends BaseAdapter {

    private List<? extends BaseData> items;
    private Activity ctx;

    public ReviewAdapter(Activity ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<? extends BaseData> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (items != null) {
            return items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (items != null) {
            return position;
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PjViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(
                    R.layout.cell_review, null);
            holder = new PjViewHolder();
            holder.tvSpendTime = (TextView) convertView
                    .findViewById(R.id.tvSpendTime);
            holder.tvStopTime = (TextView) convertView
                    .findViewById(R.id.tvStopTime);
            holder.operate = (IconTextView) convertView
                    .findViewById(R.id.operate);
            holder.tvTag = (TextView) convertView.findViewById(R.id.tvTag);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.llBg = (LinearLayout) convertView.findViewById(R.id.llBg);
            holder.viewDiv = (View) convertView.findViewById(R.id.viewDiv);
            holder.tvTop = (TextView) convertView.findViewById(R.id.tvTop);
            convertView.setTag(holder);
        } else {
            holder = (PjViewHolder) convertView.getTag();
        }
        setData(position, holder);
        return convertView;
    }

    public abstract void setData(int position, PjViewHolder holder);

    public class PjViewHolder {
        public TextView tvTitle;
        public TextView tvSpendTime;
        public TextView tvStopTime;
        public TextView tvTag;
        public IconTextView operate;
        public LinearLayout llBg;
        public TextView tvTop;
        public View viewDiv;
    }
}