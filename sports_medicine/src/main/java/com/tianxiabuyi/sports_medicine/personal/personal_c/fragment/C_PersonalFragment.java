package com.tianxiabuyi.sports_medicine.personal.personal_c.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.Encrpt;
import com.eeesys.frame.utils.GsonTool;
import com.eeesys.frame.utils.ToastTool;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.fragment.LazyFragment;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.DateUtil;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.model.Step;
import com.tianxiabuyi.sports_medicine.pedometer.pojo.StepData;
import com.tianxiabuyi.sports_medicine.pedometer.utils.DbUtils;
import com.tianxiabuyi.sports_medicine.personal.DataActivity;
import com.tianxiabuyi.sports_medicine.personal.personal_c.activity.C_MyExpertActivity;
import com.tianxiabuyi.sports_medicine.personal.personal_c.activity.DayRankingActivity;
import com.tianxiabuyi.sports_medicine.personal.personal_c.activity.HealthFileActivity;
import com.tianxiabuyi.sports_medicine.personal.personal_c.activity.MyQuesActivity;
import com.tianxiabuyi.sports_medicine.personal.personal_c.event.StepEvent;
import com.tianxiabuyi.sports_medicine.personal.personal_c.view.MyMarkerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人中心
 */
public class C_PersonalFragment extends LazyFragment {
    private static final String TAG = "C_PersonalFragment";
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.civ_avatar)
    CircleImageView civAvatar;
    @Bind(R.id.tv_add_score)
    TextView tvAddScore;
    @Bind(R.id.tv_sign_in)
    TextView tvSignIn;
    @Bind(R.id.tv_beans)
    TextView tvBeans;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_step)
    TextView tvStep;
    @Bind(R.id.lineChart)
    LineChart mChart;
    private int cloud;

    public C_PersonalFragment() {
        // Required empty public constructor
    }

    public static C_PersonalFragment newInstance() {
        return new C_PersonalFragment();
    }

    private void uploadStep() {
        DbUtils.createDb(getActivity());
        List<StepData> stepDatas = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{DateUtil.getTodayDate()});
        if (stepDatas == null || stepDatas.size() == 0) return;
        List<Step> steps = new ArrayList<>();
        for (int i = stepDatas.size() - 1; i >= 0; i--) {
            StepData stepData = stepDatas.get(i);
            if (!stepData.getStep().equals("0")) {
                steps.add(new Step(stepData.getToday(), stepData.getStep()));
                break;
            }
        }
        if (steps.size() == 0) return;
//        Log.d(TAG, steps.toString());
        Param param = new Param(Constant.STEP);
        param.addRequstParams("uid", UserSpUtil.getUid(getActivity()));
        param.addRequstParams("steps", Encrpt.encryptStr(GsonTool.toJson(steps)));
        param.addToken();
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {

            }

            @Override
            public void err(HttpResult httpresult) {

            }
        });
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c__personal, container, false);
        ButterKnife.bind(this, view);
        tvDate.setText(DateUtil.getTodayDate());
        initChart();
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        setData();
    }

    private void setData() {
        if (UserSpUtil.isLogin(getActivity()) && UserSpUtil.getStatus(getActivity()) == 100) {
            loadPoints();
            uploadStep();
            String avatar = UserSpUtil.getUser(getActivity()).getAvatar();
            if (avatar == null || avatar.equals("http://image.eeesys.com/default/user_m.png")) {
                civAvatar.setImageResource(R.mipmap.avatar);
            } else {
                GlideUtil.setAvatar(getActivity(), civAvatar, UserSpUtil.getUser(getActivity()).getAvatar());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取健康豆数量
     */
    private void loadPoints() {
        Param param = new Param(Constant.TOTAL_POINT);
        param.addToken();
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                try {
                    JSONObject jsonObject = new JSONObject(httpresult.getResult());
                    cloud = jsonObject.optInt("score");
                    tvBeans.setText(TextUtils.concat("健康云：", cloud + "朵"));
                    int flag = jsonObject.optInt("sign");
                    if (flag != 0) {
                        tvSignIn.setEnabled(false);
                        tvSignIn.setText("已签到");
                    } else {
                        tvSignIn.setEnabled(true);
                        tvSignIn.setText("签到");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void err(HttpResult httpresult) {

            }
        });
    }

    @Subscribe
    public void onStepEvent(StepEvent event) {
        tvStep.setText(event.getStep() + "");
        setChartData();
        mChart.invalidate();
    }

    private void initChart() {
        mChart.setDescription("");
        // 打开或关闭对图表所有轴的的缩放
        mChart.setScaleEnabled(false);
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.view_step_marker);
        mChart.setMarkerView(mv);
        // 设置纵坐标
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisMinValue(0);
        yAxis.setDrawTopYLabelEntry(true);
        yAxis.setSpaceTop(20);
        mChart.getAxisRight().setEnabled(false);
        // 设置横坐标
        XAxis x = mChart.getXAxis();
        x.setDrawGridLines(false);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setLabelCount(5, true);
        // 隐藏左下角图例
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        setChartData();
    }

    private List<Long> yList = new ArrayList<>();
    private ArrayList<Entry> yVals = new ArrayList<Entry>();

    private void setChartData() {
        DbUtils.createDb(getActivity());
        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{DateUtil.getTodayDate()});
        Collections.sort(list, new Comparator<StepData>() {
            @Override
            public int compare(StepData stepData, StepData t1) {
                return stepData.getHour().compareTo(t1.getHour());
            }
        });
//        Log.i("C_PersonalFragment", list.toString());
        int j = 0;
        yList.clear();
        for (int i = 0; i < 24; i++) {
            if (j < list.size() && list.get(j).getHour() == i) {
                yList.add(Long.valueOf(list.get(j).getStep()));
                j++;
            } else {
                yList.add(0L);
            }
        }
        yVals.clear();
        for (int i = 0; i <= 24; i++) {
            if (i == 0) {
                yVals.add(new Entry(i, 0));
            } else if (i == 1) {
                yVals.add(new Entry(i, yList.get(0)));
            } else {
                long step = yList.get(i - 1) - yList.get(i - 2);
                yVals.add(new Entry(i, step > 0 ? step : 0));
            }
        }
        LineDataSet lineDataSet;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(yVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            lineDataSet = new LineDataSet(yVals, "");
            lineDataSet.setColor(Color.rgb(174, 124, 243));
//            lineDataSet.setCircleColor(Color.rgb(174, 124, 243));
//            lineDataSet.setCircleRadius(3f);
            lineDataSet.setDrawCircles(false);
//            lineDataSet.setLineWidth(2f);
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setDrawFilled(true);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.step_line_chart_gradient);
                lineDataSet.setFillDrawable(drawable);
            } else {
                lineDataSet.setFillColor(Color.rgb(234, 227, 252));
            }
            lineDataSet.setDrawValues(false);
            LineData data = new LineData(lineDataSet);
            mChart.setData(data);
        }
    }

    private void toSignIn() {
        Param param = new Param(Constant.SIGN_IN);
        param.addToken();
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                tvSignIn.setEnabled(false);
                tvSignIn.setText("已签到");
                tvBeans.setText(TextUtils.concat("健康云：", (cloud + 1) + "朵"));
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(getActivity(), httpresult.getMsg());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.civ_avatar, R.id.tv_sign_in, R.id.tv_data, R.id.tv_my_ques, R.id.tv_my_expert, R.id.tv_health_file})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.civ_avatar:
            case R.id.tv_data:
                getActivity().startActivity(new Intent(getActivity(), DataActivity.class));
                break;
            case R.id.tv_sign_in:
                toSignIn();
                tvAddScore.setVisibility(View.VISIBLE);
                AnimationSet set = new AnimationSet(true);
                set.addAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.sign_in_anim));
                set.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tvAddScore.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                tvAddScore.startAnimation(set);
                break;
            case R.id.tv_my_ques:
                startActivity(new Intent(getActivity(), MyQuesActivity.class));
                break;
            case R.id.tv_my_expert:
                startActivity(new Intent(getActivity(), C_MyExpertActivity.class));
                break;
            // 健康档案
            case R.id.tv_health_file:
                startActivity(new Intent(getActivity(), HealthFileActivity.class));
                break;
        }
    }

    @OnClick(R.id.iv_ranking)
    public void onClick() {
        startActivity(new Intent(getActivity(), DayRankingActivity.class));
    }
}
