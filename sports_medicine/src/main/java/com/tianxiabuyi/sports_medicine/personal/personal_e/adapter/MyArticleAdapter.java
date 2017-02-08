package com.tianxiabuyi.sports_medicine.personal.personal_e.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.model.Preach;

import java.util.List;

/**
 * Created by Administrator on 2016/10/15.
 */

public class MyArticleAdapter extends BaseListAdapter<Preach> {
    public MyArticleAdapter(Context context, List<Preach> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, Preach preach, int position) {
        holder.textView_1.setText(preach.getTime());
        holder.textView_2.setText(preach.getTitle());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_item_my_article;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_time);
        holder.textView_2 = (TextView) view.findViewById(R.id.tv_title);
    }
}
