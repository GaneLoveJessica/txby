package com.tianxiabuyi.sports_medicine.expert.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseAdapter;
import com.tianxiabuyi.sports_medicine.model.Preach;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */

public class ArticleAdapter2 extends BaseAdapter<Preach> {

    public ArticleAdapter2(List<Preach> data) {
        super(R.layout.list_item_article2,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Preach preach) {
        holder.setText(R.id.tv_title,preach.getTitle())
                .setText(R.id.tv_time,preach.getTime());
        if (preach.getTitle().equals("没有文章信息")){
            holder.setVisible(R.id.iv_detail,false);
        }
    }
}
