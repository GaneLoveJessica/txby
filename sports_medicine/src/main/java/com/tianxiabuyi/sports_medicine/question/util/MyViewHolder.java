package com.tianxiabuyi.sports_medicine.question.util;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Administrator on 2016/12/27.
 */

public class MyViewHolder extends BaseViewHolder implements View.OnClickListener {
    private MyItemClickListener mListener;

    public MyViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(v, getLayoutPosition());
        }
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mListener = listener;
    }
}
