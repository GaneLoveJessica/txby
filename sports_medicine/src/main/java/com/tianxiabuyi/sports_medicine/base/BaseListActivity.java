package com.tianxiabuyi.sports_medicine.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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

/**
 * Created by Administrator on 2016/12/8.
 */

public abstract class BaseListActivity<T> extends BaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    protected BaseQuickAdapter<T, BaseViewHolder> baseAdapter;
    private View emptyView;

    @Override
    protected int getLayoutId() {
        return R.layout.base_list;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(this));
        List<T> baseList = new ArrayList<>();
        baseAdapter = getAdapter(baseList);
        mRecyclerView.setAdapter(baseAdapter);
        emptyView = getLayoutInflater().inflate(R.layout.base_empty, (ViewGroup) mRecyclerView.getParent(), false);
        init();
        loadData();
    }

    protected void loadData() {
        Param param = getRequestParam();
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                List<T> data = getResult(httpresult);
                baseAdapter.setNewData(data);
                baseAdapter.setEmptyView(emptyView);
            }

            @Override
            public void err(HttpResult httpresult) {
                baseAdapter.setNewData(null);
                baseAdapter.setEmptyView(emptyView);
                ToastTool.show(BaseListActivity.this, httpresult.getMsg());
            }
        });
    }

    protected View addHeadView(int layoutId) {
        View headView = getLayoutInflater().inflate(layoutId, (ViewGroup) mRecyclerView.getParent(), false);
        baseAdapter.addHeaderView(headView);
        return headView;
    }

    protected abstract void init();

    protected abstract BaseQuickAdapter<T, BaseViewHolder> getAdapter(List<T> baseList);

    protected abstract Param getRequestParam();

    protected abstract List<T> getResult(HttpResult httpresult);
}
