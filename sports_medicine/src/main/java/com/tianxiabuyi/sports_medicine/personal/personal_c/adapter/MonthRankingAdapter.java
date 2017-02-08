package com.tianxiabuyi.sports_medicine.personal.personal_c.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseAdapter;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.model.Ranking;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

public class MonthRankingAdapter extends BaseAdapter<Ranking> {
    public MonthRankingAdapter(List<Ranking> data) {
        super(R.layout.list_item_ranking, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Ranking ranking) {
        int position = holder.getLayoutPosition();
        if (position == 0) {
            holder.setVisible(R.id.iv_ranking, true)
                    .setVisible(R.id.tv_ranking, false)
                    .setImageResource(R.id.iv_ranking, R.mipmap.ranking_first);
        } else if (position == 1) {
            holder.setVisible(R.id.iv_ranking, true)
                    .setVisible(R.id.tv_ranking, false)
                    .setImageResource(R.id.iv_ranking, R.mipmap.ranking_second);
        } else if (position == 2) {
            holder.setVisible(R.id.iv_ranking, true)
                    .setVisible(R.id.tv_ranking, false)
                    .setImageResource(R.id.iv_ranking, R.mipmap.ranking_third);
        } else {
            holder.setVisible(R.id.iv_ranking, false)
                    .setVisible(R.id.tv_ranking, true)
                    .setText(R.id.tv_ranking, position + 1+"");
        }
        if (ranking.getAvatar() != null && !ranking.getAvatar().equals("http://image.eeesys.com/default/user_m.png")) {
            GlideUtil.setCircleAvatar(mContext, (ImageView) holder.getView(R.id.iv_avatar), ranking.getAvatar());
        } else {
            holder.setImageResource(R.id.iv_avatar,R.mipmap.avatar);
        }
        holder.setText(R.id.tv_name, ranking.getUser_name())
                .setText(R.id.tv_step, TextUtils.concat(ranking.getTotstep() + " 步"));
    }
}
