package com.tianxiabuyi.sports_medicine.home.adapter;

import android.content.Context;
import android.text.TextUtils;
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
 * 首页云动态适配器
 */
public class HomeNewsAdapter extends BaseListAdapter<Preach> {
    public HomeNewsAdapter(Context context, List<Preach> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, Preach preach, int position) {
        Preach.JsonBean json = preach.getJson();
        if (json != null  && json.getNews_thumbnail()!=null) {
            holder.imageView_1.setVisibility(View.VISIBLE);
            GlideUtil.setImage(context,holder.imageView_1,json.getNews_thumbnail());
        } else {
            holder.imageView_1.setVisibility(View.GONE);
        }
        holder.textView_1.setText(preach.getTitle());
        holder.textView_2.setText(TextUtils.concat(preach.getBrowse() + "", "阅读"));
        holder.textView_3.setText(preach.getTime());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_item_home_news;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_image);
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_title);
        holder.textView_2 = (TextView) view.findViewById(R.id.tv_browse);
        holder.textView_3 = (TextView) view.findViewById(R.id.tv_time);
    }
}
