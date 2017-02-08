package com.tianxiabuyi.sports_medicine.question.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;

import java.util.List;

/**
 * 发表问答图片适配器
 */
public class AskImageAdapter extends BaseListAdapter<String> implements View.OnClickListener {

    public AskImageAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, String path, int position) {
        Glide.with(context)
                .load(path)
                .dontAnimate()
                .into(holder.imageView_1);
        holder.imageView_2.setVisibility(View.VISIBLE);
        holder.imageView_2.setTag(position);
        holder.imageView_2.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.grid_item_picture;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_picture);
        holder.imageView_2 = (ImageView) view.findViewById(R.id.iv_delete);
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        list.remove(position);
        notifyDataSetChanged();
    }
}
