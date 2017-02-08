package com.tianxiabuyi.sports_medicine.expert.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.util.DateUtil;
import com.tianxiabuyi.sports_medicine.model.Question;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */

public class LatestAnswerAdapter extends BaseListAdapter<Question> {
    public LatestAnswerAdapter(Context context, List<Question> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, Question question, int position) {
//       holder.textView_1.setText(question.getTitle());
        holder.textView_2.setText(question.getContent());
        holder.textView_3.setText(DateUtil.getPrefix(question.getCreate_time()));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_item_latest_answer;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
//         holder.textView_1 = (TextView) view.findViewById(R.id.tv_title);
        holder.textView_2 = (TextView) view.findViewById(R.id.tv_content);
        holder.textView_3 = (TextView) view.findViewById(R.id.tv_time);
    }
}
