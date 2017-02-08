package com.tianxiabuyi.sports_medicine.login.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.main.activity.MainActivity;

/**
 * 引导页
 */
public class StartActivity extends BaseActivity {

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initContentView() {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0, 1000);
    }
}
