package com.tianxiabuyi.sports_medicine.question.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.model.Praise;
import com.tianxiabuyi.sports_medicine.common.util.GlideCircleTransform;

import java.util.List;

/**
 * 点赞列表适配器
 */
public class PraiseAdapter extends BaseListAdapter<Praise> {
    public PraiseAdapter(Context context, List<Praise> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, Praise praise, int position) {
        if (praise.getAvatar() == null || praise.getAvatar().equals("http://image.eeesys.com/default/user_m.png")
                || praise.getAvatar().equals("http://image.eeesys.com/default/doctor_m.png")){
            holder.imageView_1.setImageResource(R.mipmap.avatar);
        }else {
            Glide.with(context)
                    .load(praise.getAvatar())
                    .bitmapTransform(new GlideCircleTransform(context))
                    .placeholder(R.mipmap.avatar)
                    .error(R.mipmap.avatar)
                    .into(holder.imageView_1);
        }
        holder.textView_1.setText(praise.getUser_name());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_item_praise;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_image);
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_name);
    }
}
