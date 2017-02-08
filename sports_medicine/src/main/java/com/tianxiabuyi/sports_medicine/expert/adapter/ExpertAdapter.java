package com.tianxiabuyi.sports_medicine.expert.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseAdapter;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.model.Expert;

import java.util.List;

import static com.youku.player.YoukuPlayerApplication.context;

/**
 * 专家
 */
public class ExpertAdapter extends BaseAdapter<Expert>  {

    public ExpertAdapter(List<Expert> data) {
        super(R.layout.list_item_expert, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Expert expert) {
        holder.setText(R.id.tv_name, expert.getName())
                .setText(R.id.tv_profession, expert.getJson().getMy_title())
                .setText(R.id.tv_desc, expert.getJson().getMajor())
                .setText(R.id.tv_love_number, expert.getLove() + "");
        if (expert.getIs_loved() == 1) {
            holder.setImageResource(R.id.iv_love, R.mipmap.heart_red);
        } else {
            holder.setImageResource(R.id.iv_love, R.mipmap.heart);
        }
        GlideUtil.setCircleAvatar(context, (ImageView) holder.getView(R.id.iv_avatar), expert.getAvatar());
        holder.addOnClickListener(R.id.ll_love);
    }
}
