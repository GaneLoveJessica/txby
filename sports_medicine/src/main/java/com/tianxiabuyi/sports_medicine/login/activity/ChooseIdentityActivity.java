package com.tianxiabuyi.sports_medicine.login.activity;

import android.content.Intent;
import android.view.View;

import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.Constant;

/**
 * 选择身份
 */
public class ChooseIdentityActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_identity;
    }

    @Override
    protected void initContentView() {
    }

    /**
     * 患者注册
     * @param view
     */
    public void registerPatient(View view) {
        startActivity(C_RegisterActivity.class);
    }

    private void startActivity(Class cla) {
        Intent intent = new Intent(this, cla);
        intent.putExtra(Constant.KEY_1,getIntent().getStringExtra(Constant.KEY_1));
        intent.putExtra(Constant.KEY_2,getIntent().getStringExtra(Constant.KEY_2));
        startActivity(intent);
    }

    /**
     * 专家注册
     * @param view
     */
    public void registerExpert(View view) {
//        ToastTool.show(this,"对不起，暂未开放专家注册");
        startActivity(E_RegisterActivity.class);
    }

    public void back(View view){
        finish();
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }
}
