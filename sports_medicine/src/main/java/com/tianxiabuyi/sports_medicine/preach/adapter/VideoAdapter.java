package com.tianxiabuyi.sports_medicine.preach.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.model.Preach;

import java.util.List;

import static com.youku.player.YoukuPlayerApplication.context;

/**
 * Created by Administrator on 2016/10/15.
 */

public class VideoAdapter extends BaseQuickAdapter<Preach, BaseViewHolder> {

    public VideoAdapter(List<Preach> data) {
        super(R.layout.list_item_video, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Preach preach) {
        GlideUtil.setImage(context, (ImageView) baseViewHolder.getView(R.id.iv_image), preach.getJson().getThumb());
        baseViewHolder.setText(R.id.tv_title, preach.getTitle())
                .setText(R.id.tv_time, preach.getTime());
    }
}
