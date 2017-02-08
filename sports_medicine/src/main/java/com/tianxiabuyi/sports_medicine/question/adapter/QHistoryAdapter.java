package com.tianxiabuyi.sports_medicine.question.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseAdapter;
import com.tianxiabuyi.sports_medicine.model.QuesHistory;

import java.util.List;

/**
 * 问答搜索历史记录适配器
 */
public class QHistoryAdapter extends BaseAdapter<QuesHistory> {
    public QHistoryAdapter(List<QuesHistory> data) {
        super(R.layout.list_item_history,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, QuesHistory quesHistory) {
        holder.setText(R.id.tv_history, quesHistory.getContent())
                .addOnClickListener(R.id.iv_delete);
    }
}
