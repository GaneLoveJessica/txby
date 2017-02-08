package com.tianxiabuyi.sports_medicine.preach.activity;

import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.preach.fragment.CloudEssenceFragment;
import com.tianxiabuyi.sports_medicine.preach.fragment.PreachTabFragment;

/**
 * 云荟萃
 */
public class CloudEssenceActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.base_fragment;
    }

    @Override
    protected void initContentView() {
        String titleStr = getIntent().getStringExtra(Constant.KEY_1);
        title.setText(titleStr);
        if ("云荟萃".equals(titleStr)) {
            CloudEssenceFragment fragment = CloudEssenceFragment.newInstance(getIntent().getIntExtra(Constant.KEY_2, 0));
            getSupportFragmentManager().beginTransaction().add(R.id.base_container, fragment).hide(fragment).show(fragment).commit();
        } else {
            PreachTabFragment fragment = PreachTabFragment.newInstance(getIntent().getIntExtra(Constant.KEY_2, 0));
            getSupportFragmentManager().beginTransaction().add(R.id.base_container,fragment).hide(fragment).show(fragment).commit();
        }
    }
}
