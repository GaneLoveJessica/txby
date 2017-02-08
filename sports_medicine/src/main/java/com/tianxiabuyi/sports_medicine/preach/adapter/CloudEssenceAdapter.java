package com.tianxiabuyi.sports_medicine.preach.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseAdapter;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.model.Preach;

import java.util.List;

/**
 * 云荟萃
 */
public class CloudEssenceAdapter extends BaseAdapter<Preach> {

    public CloudEssenceAdapter(List<Preach> data) {
        super(R.layout.list_item_cloud_essence, data);
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
                .setText(R.id.tv_time, preach.getTime())
                .setText(R.id.tv_browse, "阅读数 " + preach.getBrowse())
                .setText(R.id.tv_love_number, preach.getLove() + "");
        if (preach.getIs_loved() == 1) {
            holder.setImageResource(R.id.iv_love, R.mipmap.heart_red);
        } else {
            holder.setImageResource(R.id.iv_love, R.mipmap.heart);
        }
        holder.addOnClickListener(R.id.ll_love);
    }
}
