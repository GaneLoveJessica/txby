package com.tianxiabuyi.sports_medicine.personal.personal_e.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseAdapter;
import com.tianxiabuyi.sports_medicine.common.util.DateUtil;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.model.Question;

import java.util.List;

/**
 * 我的回答
 */
public class MyAnswerAdapter extends BaseAdapter<Question> {

    public MyAnswerAdapter(List<Question> data) {
        super(R.layout.list_item_my_answer, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Question question) {
        GlideUtil.setCircleAvatar(mContext, (ImageView) holder.getView(R.id.iv_avatar), question.getAvatar());
        holder.setText(R.id.tv_time, DateUtil.getPrefix(question.getCreate_time()))
                .setText(R.id.tv_ask_person, TextUtils.concat("提问人：" + question.getUser_name()))
                .setText(R.id.tv_content, question.getContent());
    }
}
