package com.tianxiabuyi.sports_medicine.base;

import android.support.v4.app.Fragment;

import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;

/**
 * Created by Administrator on 2016/12/20.
 */

public abstract class BaseFragActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return R.layout.base_fragment;
    }

    @Override
    protected void initContentView() {
        Fragment fragment = getFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.base_container,fragment).hide(fragment).show(fragment).commit();
    }

    protected abstract Fragment getFragment();
}
