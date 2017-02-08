package com.tianxiabuyi.sports_medicine.personal.personal_c.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.model.Info;

import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */

public class HealthFileAdapter extends BaseListAdapter<Info> {
    public HealthFileAdapter(Context context, List<Info> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, Info info, int position) {
        holder.textView_1.setText(info.getTitle());
        holder.textView_1.setCompoundDrawablesWithIntrinsicBounds(info.getImgId(), 0, R.mipmap.detail, 0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_item_health_file;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_title);
    }
}
