package com.tianxiabuyi.sports_medicine.personal.personal_c.activity;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eeesys.frame.http.HttpResult;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseRefreshActivity;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.DateUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.model.Ranking;
import com.tianxiabuyi.sports_medicine.model.User;
import com.tianxiabuyi.sports_medicine.pedometer.pojo.StepData;
import com.tianxiabuyi.sports_medicine.pedometer.service.StepDcretor;
import com.tianxiabuyi.sports_medicine.pedometer.utils.DbUtils;
import com.tianxiabuyi.sports_medicine.personal.personal_c.adapter.DayRankingAdapter;

import java.util.Calendar;
import java.util.List;

/**
 * 日排行榜
 */
public class DayRankingActivity extends BaseRefreshActivity<Ranking> {

    @Override
    protected void init() {
        title.setText("今日排行榜");
        if (UserSpUtil.isManager(this)) {
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageResource(R.mipmap.ranking_month);
        }
        setHasPage(true);
    }

    @Override
    protected void rightClick() {
        startActivity(new Intent(this, AdminRankingActivity.class));
    }

    @Override
    protected BaseQuickAdapter<Ranking, BaseViewHolder> getAdapter(List<Ranking> baseList) {
        return new DayRankingAdapter(baseList);
    }

    @Override
    protected List<Ranking> getResult(boolean isRefresh, HttpResult httpresult) {
        List<Ranking> list = httpresult.getListResult("stepList", new TypeToken<List<Ranking>>() {
        });
        if (isRefresh) {
            Ranking ranking = new Ranking();
            User user = UserSpUtil.getUser(this);
            if (httpresult.getNumber("rankingId") == 0) {
                ranking.setRanking(list.size() + 1);
            } else {
                ranking.setRanking(httpresult.getNumber("rankingId"));
            }
            int step = httpresult.getNumber("dayStep");
            if (step > 0) {
                updateLocalSteps(step);
            }
            ranking.setAvatar(user.getAvatar());
            ranking.setUser_name(user.getUser_name());
            ranking.setStep(step);
            ranking.setItemType(1);
            list.add(0, ranking);
        } else {
            Ranking ranking = baseAdapter.getItem(0);
            if (ranking.getStep() == 0) {
                ranking.setRanking(ranking.getRanking() + list.size());
            } else {
                ranking.setRanking(httpresult.getNumber("rankingId"));
            }
            int step = httpresult.getNumber("dayStep");
            if (step > 0) {
                updateLocalSteps(step);
            }
            ranking.setStep(step);
        }
        return list;
    }

    private void updateLocalSteps(int step) {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        List<StepData> stepDatas = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{DateUtil.getTodayDate()}
                , "hour", new Integer[]{currentHour});
        if (stepDatas == null || stepDatas.size() == 0) {
            StepData data = new StepData();
            data.setToday(DateUtil.getTodayDate());
            data.setHour(currentHour);
            data.setStep(step + "");
            DbUtils.insert(data);
            StepDcretor.CURRENT_SETP = step;
        } else {
            StepData data = stepDatas.get(0);
            if (Integer.valueOf(data.getStep()) < step) {
                data.setStep(step + "");
                DbUtils.update(data);
                StepDcretor.CURRENT_SETP = step;
            }
        }
    }

    @Override
    protected Param getRequestParam(Ranking ranking) {
        Param param = new Param(Constant.DAY_RANKING);
        param.addRequstParams("step_time", DateUtil.getTodayDate());
        param.addToken();
        return param;
    }
}
