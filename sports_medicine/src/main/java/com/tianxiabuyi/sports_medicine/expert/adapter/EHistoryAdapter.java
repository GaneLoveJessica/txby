package com.tianxiabuyi.sports_medicine.expert.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseAdapter;
import com.tianxiabuyi.sports_medicine.model.ExpertHistory;

import java.util.List;

/**
 * 问答搜索历史记录适配器
 */
public class EHistoryAdapter extends BaseAdapter<ExpertHistory> {

    public EHistoryAdapter(List<ExpertHistory> data) {
        super(R.layout.list_item_history, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ExpertHistory expertHistory) {
        holder.setText(R.id.tv_history, expertHistory.getContent())
                .addOnClickListener(R.id.iv_delete);
    }
}
