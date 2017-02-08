package com.tianxiabuyi.sports_medicine.question.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseAdapter;
import com.tianxiabuyi.sports_medicine.model.CommunityHistory;

import java.util.List;

/**
 * 问答搜索历史记录适配器
 */
public class CHistoryAdapter extends BaseAdapter<CommunityHistory> {
    public CHistoryAdapter(List<CommunityHistory> data) {
        super(R.layout.list_item_history,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, CommunityHistory quesHistory) {
        holder.setText(R.id.tv_history, quesHistory.getContent())
                .addOnClickListener(R.id.iv_delete);
    }
}
