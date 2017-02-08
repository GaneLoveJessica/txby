package com.tianxiabuyi.sports_medicine.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.model.Preach;

import java.util.List;

/**
 * 首页云基地
 */

public class HomeLineBaseAdapter extends BaseListAdapter<Preach> {
    public HomeLineBaseAdapter(Context context, List<Preach> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, Preach preach, int position) {
        GlideUtil.setImage(context, holder.imageView_1, preach.getJson().getNews_thumbnail());
        holder.textView_1.setText(preach.getTitle());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.grid_item_line_base;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_image);
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_title);
    }
}
