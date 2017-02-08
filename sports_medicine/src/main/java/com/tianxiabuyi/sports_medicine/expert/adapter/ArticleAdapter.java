package com.tianxiabuyi.sports_medicine.expert.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.model.Preach;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */

public class ArticleAdapter extends BaseListAdapter<Preach> {
    public ArticleAdapter(Context context, List<Preach> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, Preach preach, int position) {
        holder.textView_1.setText(preach.getTitle());
        holder.textView_2.setText(preach.getTime());
        if (preach.getTitle().equals("没有文章信息")){
            holder.imageView_1.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_item_article;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_title);
        holder.textView_2 = (TextView) view.findViewById(R.id.tv_time);
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_detail);
    }
}
