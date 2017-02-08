package com.tianxiabuyi.sports_medicine.base;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.fragment.LazyFragment;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/19.
 */

public abstract class BaseListFragment<T> extends LazyFragment {
    @Bind(R.id.recyclerView)
    protected RecyclerView baseRecyclerView;
    protected BaseQuickAdapter<T, BaseViewHolder> baseAdapter;
    private View emptyView;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.base_list, container, false);
        ButterKnife.bind(this, rootView);
        baseRecyclerView.setHasFixedSize(true);
        baseRecyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity()));
        List<T> baseList = new ArrayList<>();
        baseAdapter = getAdapter(baseList);
        baseRecyclerView.setAdapter(baseAdapter);
        emptyView = inflater.inflate(R.layout.base_empty, (ViewGroup) baseRecyclerView.getParent(), false);
        init();
        return rootView;
    }

    @Override
    protected void initData() {
        Param param = getRequestParam();
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                List<T> data = getResult(httpresult);
                baseAdapter.setNewData(data);
                baseAdapter.setEmptyView(emptyView);
            }

            @Override
            public void err(HttpResult httpresult) {
                onError(httpresult);
            }
        });
    }

    protected void onError(HttpResult httpresult) {
        baseAdapter.setNewData(null);
        baseAdapter.setEmptyView(emptyView);
        ToastTool.show(getContext(), httpresult.getMsg());
    }

    protected abstract void init();

    protected abstract BaseQuickAdapter<T, BaseViewHolder> getAdapter(List<T> baseList);

    protected abstract Param getRequestParam();

    protected abstract List<T> getResult(HttpResult httpresult);

    protected void addDivider() {
        baseRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    protected void addDivider(int drawableId) {
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getActivity().getResources().getDrawable(drawableId));
        baseRecyclerView.addItemDecoration(decoration);
    }

    protected void setOnItemClickListener(OnItemClickListener listener) {
        baseRecyclerView.addOnItemTouchListener(listener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
