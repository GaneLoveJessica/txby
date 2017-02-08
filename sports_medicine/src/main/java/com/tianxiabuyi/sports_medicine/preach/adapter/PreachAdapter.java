package com.tianxiabuyi.sports_medicine.preach.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseAdapter;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.model.Preach;

import java.util.List;

/**
 * 宣讲适配器
 */
public class PreachAdapter extends BaseAdapter<Preach> {

    public PreachAdapter(List<Preach> data) {
        super(R.layout.list_item_preach, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Preach preach) {
        Preach.JsonBean json = preach.getJson();
        if (json != null && json.getNews_thumbnail() != null) {
            holder.setVisible(R.id.iv_image, true);
            GlideUtil.setImage(mContext, (ImageView) holder.getView(R.id.iv_image), json.getNews_thumbnail());
        } else {
            holder.setVisible(R.id.iv_image, false);
        }
        holder.setText(R.id.tv_title, preach.getTitle())
                .setText(R.id.tv_browse, preach.getBrowse() + "阅读")
                .setText(R.id.tv_love_number, preach.getLove() + "次")
                .addOnClickListener(R.id.ll_love);
        if (preach.getIs_loved() == 1) {
            holder.setImageResource(R.id.iv_love, R.mipmap.heart_red);
        } else {
            holder.setImageResource(R.id.iv_love, R.mipmap.heart);
        }
    }
}
