package com.tianxiabuyi.sports_medicine.personal;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.aihook.alertview.library.AlertView;
import com.aihook.alertview.library.OnItemClickListener;
import com.eeesys.frame.utils.ToastTool;
import com.tencent.android.tpush.XGPushManager;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.util.ThirdAccountDbUtil;
import com.tianxiabuyi.sports_medicine.main.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initContentView() {
        title.setText(R.string.settings);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_modify_pwd, R.id.tv_version_update, R.id.tv_feedback, R.id.tv_about_us, R.id.tv_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_modify_pwd:
                startActivity(new Intent(this, ModifyPwdActivity.class));
                break;
            case R.id.tv_version_update:
                versionUpdate();
                break;
            case R.id.tv_feedback:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.tv_about_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.tv_logout:
                logout();
                break;
        }
    }

    private void versionUpdate() {
        ToastTool.show(this, "您已更新到最新版本啦");
    }

    private void logout() {
        new AlertView("提示", "确定要退出登录吗？", null, new String[]{"退出"}, new String[]{"取消"},
                this, AlertView.Style.AlertDialog, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    // 解绑信鸽
                    Context context = getApplicationContext();
                    XGPushManager.unregisterPush(context);
                    UserSpUtil.removeUserInfo(SettingActivity.this);
                    ThirdAccountDbUtil.getInstance().clear();
//                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                    startActivity(new Intent(SettingActivity.this, MainActivity.class));
                    EventBus.getDefault().post(new LogoutEvent());
                    finish();
                }
            }
        }).show();
    }
}
