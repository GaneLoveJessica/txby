package com.tianxiabuyi.sports_medicine.personal.personal_c.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.model.Ranking;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

public class DayRankingAdapter extends BaseMultiItemQuickAdapter<Ranking, BaseViewHolder> {

    public DayRankingAdapter(List<Ranking> data) {
        super(data);
        addItemType(Ranking.ITEM_MY, R.layout.list_head_ranking);
        addItemType(Ranking.ITEM, R.layout.list_item_ranking);
    }

    @Override
    protected void convert(BaseViewHolder holder, Ranking ranking) {
        if (holder.getItemViewType() == Ranking.ITEM_MY) {
            holder.setText(R.id.tv_ranking, ranking.getRanking() + "");
        } else {
            int position = holder.getLayoutPosition();
            if (position == 1) {
                holder.setVisible(R.id.iv_ranking, true)
                        .setVisible(R.id.tv_ranking, false)
                        .setImageResource(R.id.iv_ranking, R.mipmap.ranking_first);
            } else if (position == 2) {
                holder.setVisible(R.id.iv_ranking, true)
                        .setVisible(R.id.tv_ranking, false)
                        .setImageResource(R.id.iv_ranking, R.mipmap.ranking_second);
            } else if (position == 3) {
                holder.setVisible(R.id.iv_ranking, true)
                        .setVisible(R.id.tv_ranking, false)
                        .setImageResource(R.id.iv_ranking, R.mipmap.ranking_third);
            } else {
                holder.setVisible(R.id.iv_ranking, false)
                        .setVisible(R.id.tv_ranking, true)
                        .setText(R.id.tv_ranking, position + "");
            }
        }
        if (ranking.getAvatar() != null && !ranking.getAvatar().equals("http://image.eeesys.com/default/user_m.png")) {
            GlideUtil.setCircleAvatar(mContext, (ImageView) holder.getView(R.id.iv_avatar), ranking.getAvatar());
        } else {
            holder.setImageResource(R.id.iv_avatar,R.mipmap.avatar);
        }
        holder.setText(R.id.tv_name, ranking.getUser_name())
                .setText(R.id.tv_step, TextUtils.concat(ranking.getStep() + " 步"));

    }
}
