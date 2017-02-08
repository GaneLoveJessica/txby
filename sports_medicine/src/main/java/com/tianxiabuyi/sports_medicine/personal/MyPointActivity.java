package com.tianxiabuyi.sports_medicine.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.model.Point;
import com.tianxiabuyi.sports_medicine.personal.personal_e.adapter.MyPointAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * 我的积分
 */
public class MyPointActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    @Bind(R.id.slv_point)
    StickyListHeadersListView slvPoint;
    @Bind(R.id.rl_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.tv_empty)
    ImageView tvEmpty;
    private List<Point.DataBean> list = new ArrayList<>();
    private MyPointAdapter adapter;
    private TextView tvLoadMore;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_point;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        title.setText("积分详情");
        tvRight.setText("说明");
        tvRight.setVisibility(View.VISIBLE);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        View footView = getLayoutInflater().inflate(R.layout.load_more, null);
        slvPoint.addFooterView(footView);
        tvLoadMore = (TextView) findViewById(R.id.tv_load_more);
        tvLoadMore.setOnClickListener(this);
        slvPoint.setAreHeadersSticky(true);
        slvPoint.setDrawingListUnderStickyHeader(true);
        refreshLayout.setOnRefreshListener(this);
        adapter = new MyPointAdapter(MyPointActivity.this, list);
        slvPoint.setAdapter(adapter);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    protected void rightClick() {
        Intent intent = new Intent(this, PointExplainActivity.class);
        intent.putExtra(Constant.KEY_1, 3);
        startActivity(intent);
    }

    private void loadData(final boolean isRefresh) {
        Param param = new Param(Constant.MY_POINT);
        if (!isRefresh) {
            param.addRequstParams("max_id", list.get(list.size() - 1).getId());
        }
        param.addToken();
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                refreshLayout.setRefreshing(false);
                List<Point> points = httpresult.getListResult("score", new TypeToken<List<Point>>() {
                });
                if (isRefresh) {
                    list.clear();
                }
                for (int i = 0; i < points.size(); i++) {
                    Point point = points.get(i);
                    for (int j = 0; j < point.getData().size(); j++) {
                        point.getData().get(j).setMonth(point.getMonth());
                    }
                    list.addAll(point.getData());
                }
                adapter.notifyDataSetChanged();
                slvPoint.setEmptyView(tvEmpty);
                if (list.size() < 20 || points.size() == 0) {
                    tvLoadMore.setVisibility(View.VISIBLE);
                    tvLoadMore.setEnabled(false);
                    tvLoadMore.setText("没有更多数据了哦");
                } else {
                    tvLoadMore.setVisibility(View.VISIBLE);
                    tvLoadMore.setEnabled(true);
                    tvLoadMore.setText("点击加载更多");
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                refreshLayout.setRefreshing(false);
                slvPoint.setEmptyView(tvEmpty);
                tvLoadMore.setEnabled(true);
                tvLoadMore.setText("点击加载更多");
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void onClick(View v) {
        loadData(false);
        tvLoadMore.setEnabled(false);
        tvLoadMore.setText("正在加载中...");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
