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
 * 首页云视频
 */

public class HomeVideoAdapter extends BaseListAdapter<Preach> {
    public HomeVideoAdapter(Context context, List<Preach> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, Preach video, int position) {
        GlideUtil.setImage(context, holder.imageView_1, video.getJson().getThumb());
        holder.textView_1.setText(video.getTitle());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_item_home_video;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_image);
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_title);
    }
}
