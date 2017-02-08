package com.tianxiabuyi.sports_medicine.personal.personal_c.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eeesys.frame.http.HttpResult;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseListActivity;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.model.Step;
import com.tianxiabuyi.sports_medicine.personal.personal_c.adapter.StepAdapter;

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
 * 历史步数
 */
public class HistoryStepActivity extends BaseListActivity<Step> {
    @Bind(R.id.tv_month)
    TextView tvMonth;
    @Bind(R.id.iv_next_month)
    ImageView ivNextMonth;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_step_data;
    }

    @Override
    protected void init() {
        title.setText("历史步数");
        tvMonth.setText(getCurrentMonth());
        ivNextMonth.setEnabled(false);
    }

    @Override
    protected BaseQuickAdapter<Step, BaseViewHolder> getAdapter(List<Step> baseList) {
        return new StepAdapter(baseList);
    }

    @Override
    protected Param getRequestParam() {
        Param param = new Param(Constant.MONTH_STEP);
        param.addToken();
        param.addRequstParams("uid", getIntent().getStringExtra(Constant.KEY_1));
        param.addRequstParams("time", getText(tvMonth).replace("年", "-").replace("月", ""));
        return param;
    }

    @Override
    protected List<Step> getResult(HttpResult httpresult) {
        return httpresult.getListResult("detail_step", new TypeToken<List<Step>>() {
        });
    }

    @OnClick({R.id.tv_month, R.id.iv_last_month, R.id.iv_next_month})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_month:
                break;
            case R.id.iv_last_month:
                ivNextMonth.setEnabled(true);
                ivNextMonth.setImageResource(R.mipmap.next_month);
                tvMonth.setText(getLastMonth());
                loadData();
                break;
            case R.id.iv_next_month:
                if (getCurrentMonth().equals(getNextMonth())){
                    ivNextMonth.setEnabled(false);
                    ivNextMonth.setImageResource(R.mipmap.next_month_disable);
                }
                tvMonth.setText(getNextMonth());
                loadData();
                break;
        }
    }

    /**
     * 获取这个月
     *
     * @return
     */
    private String getCurrentMonth() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
        return sdf.format(date);
    }

    /**
     * 获取上个月
     *
     * @return
     */
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

    /**
     * 获取下个月
     *
     * @return
     */
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
