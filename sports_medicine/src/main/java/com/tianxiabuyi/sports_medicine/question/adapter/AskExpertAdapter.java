package com.tianxiabuyi.sports_medicine.question.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.util.GlideCircleTransform;
import com.tianxiabuyi.sports_medicine.model.Expert;

import java.util.List;

/**
 * 提问的专家列表
 */
public class AskExpertAdapter extends BaseListAdapter<Expert> {
    private int chooseId;

    public AskExpertAdapter(Context context, List<Expert> askExperts) {
        super(context, askExperts);
    }

    @Override
    public void showView(CViewHolder holder, Expert askExpert, int position) {
        holder.textView_1.setText(askExpert.getName());
        holder.textView_2.setText(askExpert.getTitle());
        if (askExpert.getId() == chooseId || askExpert.isFlag()) {
            holder.imageView_1.setImageResource(R.mipmap.expert_check);
        } else {
            holder.imageView_1.setImageResource(R.mipmap.expert_uncheck);
        }
        Glide.with(context)
                .load(askExpert.getAvatar())
                .placeholder(R.mipmap.avatar)
                .bitmapTransform(new GlideCircleTransform(context))
                .error(R.mipmap.avatar)
                .into(holder.imageView_2);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.grid_item_ask_expert;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_name);
        holder.textView_2 = (TextView) view.findViewById(R.id.tv_profession);
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_choose);
        holder.imageView_2 = (ImageView) view.findViewById(R.id.iv_avatar);
    }

    public void setList(List<Expert> askExperts) {
        list.clear();
        list.addAll(askExperts);
        notifyDataSetChanged();
    }

    public void setChooseExpertId(int id) {
        this.chooseId = id;
        notifyDataSetChanged();
    }

    public int getChooseId() {
        return chooseId;
    }
}
