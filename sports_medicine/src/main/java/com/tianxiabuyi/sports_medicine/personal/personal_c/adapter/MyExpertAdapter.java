package com.tianxiabuyi.sports_medicine.personal.personal_c.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseAdapter;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.model.Expert;

import java.util.List;

/**
 * 我的专家
 */
public class MyExpertAdapter extends BaseAdapter<Expert> {

    public MyExpertAdapter(List<Expert> data) {
        super(R.layout.list_item_my_expert,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Expert expert) {
        holder.setText(R.id.tv_name,expert.getName())
                .setText(R.id.tv_ask_number, expert.getCount() + "");
        if (expert.getJson()!=null) {
            holder.setText(R.id.tv_profession, expert.getJson().getMy_title())
                    .setText(R.id.tv_desc,expert.getJson().getMajor());
        }
        GlideUtil.setCircleAvatar(mContext, (ImageView) holder.getView(R.id.iv_avatar), expert.getAvatar());
    }
}
