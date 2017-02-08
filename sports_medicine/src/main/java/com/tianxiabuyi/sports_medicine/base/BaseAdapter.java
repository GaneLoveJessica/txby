package com.tianxiabuyi.sports_medicine.base;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/12/8.
 */

public abstract class BaseAdapter<T> extends BaseQuickAdapter<T,BaseViewHolder> {
    public BaseAdapter(List<T> data) {
        super(data);
    }

    public BaseAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    @Override
    protected abstract void convert(BaseViewHolder holder, T t);
}
