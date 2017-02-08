package com.tianxiabuyi.sports_medicine.personal.personal_e.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.eeesys.frame.http.HttpResult;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.base.BaseRefreshFragment;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.model.Question;
import com.tianxiabuyi.sports_medicine.personal.personal_e.adapter.MyAnswerAdapter;
import com.tianxiabuyi.sports_medicine.question.activity.QuesDetActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnswerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerFragment extends BaseRefreshFragment<Question> {
    private static final String ARG_PARAM1 = "param1";

    public AnswerFragment() {
        // Required empty public constructor
    }

    public static AnswerFragment newInstance(int param1) {
        AnswerFragment fragment = new AnswerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void init() {
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Question question = (Question) baseQuickAdapter.getItem(i);
                Intent intent = new Intent();
                intent.setClass(getActivity(), QuesDetActivity.class);
                intent.putExtra(Constant.KEY_1, question.getId() + "");
                intent.putExtra(Constant.KEY_2, true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected BaseQuickAdapter<Question, BaseViewHolder> getAdapter(List<Question> baseList) {
        return new MyAnswerAdapter(baseList);
    }

    @Override
    protected Param getRequestParam(Question question) {
        String url;
        if (getArguments().getInt(ARG_PARAM1) == 1) {
            url = Constant.MY_UN_ANSWER;
        } else {
            url = Constant.MY_ANSWERED;
        }
        Param param = new Param(url);
        if (question != null) {
            param.addRequstParams("max_id", question.getId());
        }
        param.addToken();
        return param;
    }

    @Override
    protected List<Question> getResult(HttpResult httpresult, boolean isRefresh) {
        return httpresult.getListResult("quests", new TypeToken<List<Question>>() {
        });
    }
}
