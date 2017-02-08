package com.tianxiabuyi.sports_medicine.question.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.Constant;

import java.util.List;

/**
 * 问答图片适配器
 */
public class PictureAdapter extends BaseListAdapter<String> {
    public PictureAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, String url, int position) {
        String thumbUrl = Constant.THUMB + "?name=" + url.substring(url.lastIndexOf("/") + 1);
        Glide.with(context).load(thumbUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.loading)
                .error(R.mipmap.loading)
                .into(holder.imageView_1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.grid_item_picture;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_picture);
    }
}
