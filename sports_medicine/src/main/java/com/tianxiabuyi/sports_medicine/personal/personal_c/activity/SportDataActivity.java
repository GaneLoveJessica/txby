package com.tianxiabuyi.sports_medicine.personal.personal_c.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.util.DateUtil;
import com.tianxiabuyi.sports_medicine.pedometer.pojo.StepData;
import com.tianxiabuyi.sports_medicine.pedometer.utils.DbUtils;
import com.tianxiabuyi.sports_medicine.personal.personal_c.view.MyMarkerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 健身数据
 */
public class SportDataActivity extends BaseActivity {

    @Bind(R.id.tv_step)
    TextView tvStep;
    @Bind(R.id.lineChart)
    LineChart mChart;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sport_data;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        title.setText(R.string.sport_data);
        DbUtils.createDb(this);
        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{DateUtil.getTodayDate()});
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator<StepData>() {
                @Override
                public int compare(StepData stepData, StepData t1) {
                    return stepData.getHour().compareTo(t1.getHour());
                }
            });
            tvStep.setText(TextUtils.concat(list.get(list.size() - 1).getStep() + "步"));
            initChart();
            setChartData(list);
        }
    }

    private void initChart() {
        mChart.setDescription("");
        // 打开或关闭对图表所有轴的的缩放
        mChart.setScaleEnabled(false);
        MyMarkerView mv = new MyMarkerView(this, R.layout.view_step_marker);
        mChart.setMarkerView(mv);
        // 设置纵坐标
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisMinValue(0);
        yAxis.setDrawTopYLabelEntry(true);
        yAxis.setSpaceTop(10);
        yAxis.setTextColor(getResources().getColor(android.R.color.white));
        yAxis.setAxisLineColor(getResources().getColor(android.R.color.white));
        yAxis.setGridColor(getResources().getColor(android.R.color.white));
        mChart.getAxisRight().setEnabled(false);
        // 设置横坐标
        XAxis x = mChart.getXAxis();
        x.setDrawGridLines(false);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setLabelCount(5, true);
        // 设置横坐标的颜色
        x.setTextColor(getResources().getColor(android.R.color.white));
        // 设置横坐标轴线的颜色
        x.setAxisLineColor(getResources().getColor(android.R.color.white));
        // 隐藏左下角图例
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);
    }

    private void setChartData(List<StepData> list) {
        List<Long> yList = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < 24; i++) {
            if (j < list.size() && list.get(j).getHour() == i) {
                yList.add(Long.valueOf(list.get(j).getStep()));
                j++;
            } else {
                yList.add(0L);
            }
        }
//        Log.i("HealthFragment", yList.toString());
        ArrayList<Entry> yVals = new ArrayList<Entry>();
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
            lineDataSet.setColor(Color.rgb(255, 255, 255));
//            lineDataSet.setCircleRadius(3f);
            lineDataSet.setDrawCircles(false);
//            lineDataSet.setLineWidth(2f);
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setDrawFilled(true);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.step_line_chart_gradient);
                lineDataSet.setFillDrawable(drawable);
            } else {
                lineDataSet.setFillColor(Color.rgb(234, 227, 252));
            }
            lineDataSet.setDrawValues(false);
            LineData data = new LineData(lineDataSet);
            mChart.setData(data);
        }
    }

    @OnClick(R.id.tv_show_all_data)
    public void onClick() {
        startActivity(new Intent(this, HistoryStepActivity.class));
    }
}
