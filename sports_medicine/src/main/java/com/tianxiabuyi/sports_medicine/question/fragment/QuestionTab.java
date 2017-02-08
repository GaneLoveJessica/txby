package com.tianxiabuyi.sports_medicine.question.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eeesys.frame.http.HttpResult;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.base.BaseRefreshFragment;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.CacheUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.model.Question;
import com.tianxiabuyi.sports_medicine.question.adapter.QuestionAdapter;
import com.tianxiabuyi.sports_medicine.question.event.LoveOrTreadEvent;
import com.tianxiabuyi.sports_medicine.question.util.MyViewHolder;
import com.tianxiabuyi.sports_medicine.question.view.QuestionLoadMoreView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 问答列表页面
 */
public class QuestionTab extends BaseRefreshFragment<Question> {
    private long categoryId;
    private boolean isSearch;
    private String searchContent;

    public static QuestionTab newInstance(long categoryId, boolean isSearch) {
        QuestionTab communicateTab = new QuestionTab();
        Bundle args = new Bundle();
        args.putLong(Constant.KEY_2, categoryId);
        args.putBoolean(Constant.KEY_1, isSearch);
        communicateTab.setArguments(args);
        return communicateTab;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            categoryId = getArguments().getLong(Constant.KEY_2);
            isSearch = getArguments().getBoolean(Constant.KEY_1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((QuestionAdapter) baseAdapter).setPosition(-1);
    }

    @Override
    protected void init() {
        baseAdapter.setLoadMoreView(new QuestionLoadMoreView());
    }

    @Override
    protected List<Question> getCache() {
        if (!isSearch) {
            return CacheUtil.getQuestion(getActivity(), CacheUtil.KEY_QUESTION + categoryId);
        }
        return null;
    }

    @Override
    protected BaseQuickAdapter<Question, MyViewHolder> getAdapter(List<Question> baseList) {
        return new QuestionAdapter(baseList);
    }

    public void search(String searchContent) {
        if (!searchContent.equals(this.searchContent)) {
            this.searchContent = searchContent;
            initData();
        }
    }

    @Override
    public void initData() {
        if (isSearch && TextUtils.isEmpty(searchContent)) return;
        super.initData();
    }

    @Override
    protected Param getRequestParam(Question question) {
        Param param = new Param(Constant.QUES_LIST);
        if (categoryId != 0) {
            param.addRequstParams("group", categoryId);
        }
        if (question != null) {
            param.addRequstParams("max_id", question.getId());
        }
        if (isSearch) {
            param.addRequstParams("content", searchContent);
            param.addRequstParams("type", 1);
        }
        param.addRequstParams("uid", UserSpUtil.getUid(getActivity()));
        return param;
    }

    @Override
    protected List<Question> getResult(HttpResult httpresult, boolean isRefresh) {
        List<Question> questionList = httpresult.getListResult("quests", new TypeToken<List<Question>>() {
        });
        if (isRefresh && !isSearch) {
            CacheUtil.saveCache(getActivity(), CacheUtil.KEY_QUESTION + categoryId, questionList);
        }
        return questionList;
    }

    @Override
    protected void onLoad() {
        if (UserSpUtil.isLogin(getActivity())) {
            loadData(false);
        } else {
            baseAdapter.loadMoreFail();
        }
    }

    @Subscribe
    public void onLoveOrTreadEvent(LoveOrTreadEvent event) {
        int position = ((QuestionAdapter) baseAdapter).getPosition();
        if (position >= 0 && event.isAction()) {
            Question question = event.getQuestion();
            baseAdapter.getData().set(position, question);
            baseAdapter.notifyDataSetChanged();
        }
    }
}
