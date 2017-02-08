package com.tianxiabuyi.sports_medicine.personal.personal_c.activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eeesys.frame.http.HttpResult;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseRefreshActivity;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.model.Question;
import com.tianxiabuyi.sports_medicine.question.adapter.CommunityAdapter;
import com.tianxiabuyi.sports_medicine.question.event.LoveOrTreadEvent;
import com.tianxiabuyi.sports_medicine.question.util.MyViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 我的问答
 */
public class MyQuesActivity extends BaseRefreshActivity<Question> {

    @Override
    protected void init() {
        title.setText(R.string.my_ques);
        EventBus.getDefault().register(this);
    }

    @Override
    protected BaseQuickAdapter<Question, MyViewHolder> getAdapter(List<Question> baseList) {
        return new CommunityAdapter(baseList);
    }

    @Override
    protected Param getRequestParam(Question question) {
        Param param = new Param(Constant.MY_QUESTION);
        param.setIsShowLoading(false);
        param.addToken();
        if (question != null) {
            param.addRequstParams("max_id", question.getId());
        }
        return param;
    }

    @Override
    protected List<Question> getResult(boolean isRefresh, HttpResult httpresult) {
        return httpresult.getListResult("quests", new TypeToken<List<Question>>() {
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((CommunityAdapter) baseAdapter).setPosition(-1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onLoveOrTreadEvent(LoveOrTreadEvent event) {
        int position = ((CommunityAdapter) baseAdapter).getPosition();
        if (position >= 0) {
            Question question = event.getQuestion();
            baseAdapter.getData().set(position, question);
            baseAdapter.notifyDataSetChanged();
        }
    }
}
