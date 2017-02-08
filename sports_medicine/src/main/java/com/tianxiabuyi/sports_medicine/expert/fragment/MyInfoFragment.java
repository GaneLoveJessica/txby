package com.tianxiabuyi.sports_medicine.expert.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.widget.HeaderScrollHelper;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.fragment.LazyFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/17.
 */

public class MyInfoFragment extends LazyFragment implements HeaderScrollHelper.ScrollableContainer {
    @Bind(R.id.tv_info)
    TextView tvInfo;
    private View view;

    public static MyInfoFragment newInstance(String education) {
        MyInfoFragment fragment = new MyInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_1, education);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        String intro = "没有简介信息";
        if (getArguments() != null) {
            String text = getArguments().getString(Constant.KEY_1);
            if (text != null && text.length() > 0) {
                intro = text.replaceAll("\\\\n", "\n");
            }
        }
        tvInfo.setText(intro);
    }

    @Override
    public View getScrollableView() {
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
