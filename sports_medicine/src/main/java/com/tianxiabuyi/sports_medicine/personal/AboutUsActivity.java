package com.tianxiabuyi.sports_medicine.personal;

import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;

public class AboutUsActivity extends BaseActivity {

    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initContentView() {
        title.setText(R.string.about_us);
    }

}
