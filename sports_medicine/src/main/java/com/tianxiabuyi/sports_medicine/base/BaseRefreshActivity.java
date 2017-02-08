package com.tianxiabuyi.sports_medicine.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.tianxiabuyi.sports_medicine.R.id.recyclerView;

/**
 * Created by Administrator on 2016/12/7.
 */

public abstract class BaseRefreshActivity<T> extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @Bind(recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    protected BaseQuickAdapter<T, ? extends BaseViewHolder> baseAdapter;
    private int page;
    private int totalPage;
    // 是否以page的形式分页
    private boolean hasPage;
    private View emptyView;

    @Override
    protected int getLayoutId() {
        return R.layout.base_refresh;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        List<T> baseList = new ArrayList<>();
        baseAdapter = getAdapter(baseList);
        baseAdapter.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(baseAdapter);
        emptyView = getLayoutInflater().inflate(R.layout.base_empty, (ViewGroup) mRecyclerView.getParent(), false);
        init();
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }


    @Override
    public void onRefresh() {
        page = 1;
        baseAdapter.setEnableLoadMore(false);
        loadData(true);
    }

    @Override
    public void onLoadMoreRequested() {
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (hasPage) {
                    page++;
                }
                loadData(false);
            }
        });
    }

    protected void loadData(final boolean isRefresh) {
        Param param;
        if (isRefresh) {
            param = getRequestParam(null);
        }else{
            param = getRequestParam(baseAdapter.getItem(baseAdapter.getData().size() - 1));
        }
        param.setIsShowLoading(false);
        if (hasPage) {
            param.addRequstParams("page", page);
        }
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                baseAdapter.setEnableLoadMore(true);
                List<T> data = getResult(isRefresh, httpresult);
                if (isRefresh) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    baseAdapter.setNewData(data);
                    baseAdapter.setEmptyView(emptyView);
                } else {
                    baseAdapter.addData(data);
                    baseAdapter.loadMoreComplete();
                }
                if (hasPage) {
                    page = httpresult.getNumber("page");
                    totalPage = httpresult.getNumber("page_count");
                    if (totalPage == 1) {
                        baseAdapter.loadMoreEnd(true);
                    } else if (page == totalPage) {
                        baseAdapter.loadMoreEnd();
                    }
                } else if (data.size() < 20) {
                    if (baseAdapter.getData().size() < 20) {
                        baseAdapter.loadMoreEnd(true);
                    } else {
                        baseAdapter.loadMoreEnd();
                    }
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                baseAdapter.setEnableLoadMore(true);
                if (isRefresh) {
                    baseAdapter.setNewData(null);
                    baseAdapter.setEmptyView(emptyView);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            baseAdapter.loadMoreFail();
                        }
                    }, 300);
                }
                ToastTool.show(BaseRefreshActivity.this, httpresult.getMsg());
            }
        });
    }

    protected View addHeadView(int layoutId) {
        View headView = getLayoutInflater().inflate(layoutId, (ViewGroup) mRecyclerView.getParent(), false);
        baseAdapter.addHeaderView(headView);
        return headView;
    }

    protected void setOnItemClickListener(OnItemClickListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);
    }

    protected void setHasPage(boolean hasPage) {
        this.hasPage = hasPage;
    }

    protected abstract void init();

    protected abstract BaseQuickAdapter<T, ? extends BaseViewHolder> getAdapter(List<T> baseList);

    protected abstract Param getRequestParam(T t);

    protected abstract List<T> getResult(boolean isRefresh, HttpResult httpresult);
}
