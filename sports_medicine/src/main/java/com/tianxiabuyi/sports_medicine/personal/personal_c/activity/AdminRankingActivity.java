package com.tianxiabuyi.sports_medicine.personal.personal_c.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.eeesys.frame.http.HttpResult;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseRefreshActivity;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.model.Ranking;
import com.tianxiabuyi.sports_medicine.personal.personal_c.adapter.MonthRankingAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 管理员步数排行榜
 */
public class AdminRankingActivity extends BaseRefreshActivity<Ranking> {
    @Bind(R.id.tv_month)
    TextView tvMonth;
    @Bind(R.id.iv_next_month)
    ImageView ivNextMonth;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin;
    }

    @Override
    protected void init() {
        title.setText("月排行榜");
        setHasPage(true);
        tvMonth.setText(getCurrentMonth());
        ivNextMonth.setEnabled(false);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(AdminRankingActivity.this, HistoryStepActivity.class);
                Ranking ranking = (Ranking) baseQuickAdapter.getItem(i);
                intent.putExtra(Constant.KEY_1, ranking.getUid());
                startActivity(intent);
            }
        });
    }

    @Override
    protected BaseQuickAdapter<Ranking, BaseViewHolder> getAdapter(List<Ranking> baseList) {
        return new MonthRankingAdapter(baseList);
    }

    @Override
    protected Param getRequestParam(Ranking ranking) {
        final Param param = new Param(Constant.MONTH_RANKING);
        param.addRequstParams("time", getText(tvMonth).replace("年", "-").replace("月", ""));
        param.addToken();
        return param;
    }

    @Override
    protected List<Ranking> getResult(boolean isRefresh, HttpResult httpresult) {
        return httpresult.getListResult("stepList", new TypeToken<List<Ranking>>() {
        });
    }

    @OnClick({R.id.iv_last_month, R.id.iv_next_month})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_last_month:
                ivNextMonth.setEnabled(true);
                ivNextMonth.setImageResource(R.mipmap.next_month);
                tvMonth.setText(getLastMonth());
                onRefresh();
                break;
            case R.id.iv_next_month:
                if (getCurrentMonth().equals(getNextMonth())){
                    ivNextMonth.setEnabled(false);
                    ivNextMonth.setImageResource(R.mipmap.next_month_disable);
                }
                tvMonth.setText(getNextMonth());
                onRefresh();
                break;
        }
    }

    private String getCurrentMonth() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
        return sdf.format(date);
    }

    private String getLastMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
        try {
            Date date = sdf.parse(getText(tvMonth));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -1);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getNextMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
        try {
            Date date = sdf.parse(getText(tvMonth));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, +1);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
