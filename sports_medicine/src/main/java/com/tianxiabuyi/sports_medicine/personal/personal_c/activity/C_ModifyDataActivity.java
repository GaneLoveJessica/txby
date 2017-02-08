package com.tianxiabuyi.sports_medicine.personal.personal_c.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aihook.alertview.library.AlertView;
import com.aihook.alertview.library.OnItemClickListener;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.eeesys.frame.view.WheelView;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.model.User;

import java.util.Arrays;

/**
 * 修改资料
 */
public class C_ModifyDataActivity extends BaseActivity {
    private TextView tvGender;
    private EditText etAge;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_c__modify_data;
    }

    @Override
    protected void initContentView() {
        title.setText("个人资料");
        tvRight.setText(R.string.edit);
        tvRight.setVisibility(View.VISIBLE);
        tvGender = (TextView) findViewById(R.id.tv_gender);
        etAge = (EditText) findViewById(R.id.et_age);
        User user = UserSpUtil.getUser(this);
        if (user != null) {
            if (getGender(user.getGender()) != null) {
                tvGender.setText(getGender(user.getGender()));
            } else {
                tvGender.setText("未设置");
            }
            etAge.setText(user.getAge());
        }
    }

    private String getGender(String gender) {
        if ("0".equals(gender)) {
            return "女";
        }
        if ("1".equals(gender)) {
            return "男";
        }
        return null;
    }

    @Override
    protected void rightClick() {
        if (tvRight.getText().toString().equals("编辑")) {
            tvGender.setEnabled(true);
            etAge.setEnabled(true);
            tvRight.setText(R.string.save);
        } else {
            if (checkInfo()) {
                uploadData();
            }
        }
    }

    /**
     * 修改资料请求
     */
    private void uploadData() {
        hideSoftKeyboard();
        Param param = new Param(Constant.MODIFY);
        param.addToken();
        param.addRequstParams("gender", getText(tvGender).equals("男") ? 1 : 0);
        param.addRequstParams("age", getText(etAge));
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                tvRight.setText(R.string.edit);
                tvGender.setEnabled(false);
                etAge.setEnabled(false);
                ToastTool.show(C_ModifyDataActivity.this, "资料修改成功");
                User user = UserSpUtil.getUser(C_ModifyDataActivity.this);
                user.setGender(getText(tvGender).equals("男") ? "1" : "0");
                user.setAge(getText(etAge));
                UserSpUtil.setUserInfo(C_ModifyDataActivity.this, user);
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(C_ModifyDataActivity.this, httpresult.getMsg());
            }
        });
    }

    /**
     * 检查信息
     */
    private boolean checkInfo() {
/*        if (TextUtils.isEmpty(getText(etName))) {
            ToastTool.show(this, "请输入真实姓名");
            return false;
        }
        if (!Pattern.matches(Constant.REGEX_NAME, getText(etName))) {
            ToastTool.show(this, "姓名格式不正确");
            return false;
        }*/
        if (getText(tvGender).equals("未设置")) {
            ToastTool.show(this, "请选择性别");
            return false;
        }
        if (TextUtils.isEmpty(getText(etAge))) {
            ToastTool.show(this, "请输入年龄");
            return false;
        }
    /*    if (TextUtils.isEmpty(getText(etCardNumber))) {
            ToastTool.show(this, "请输入身份证号码");
            return false;
        }
        if (!Pattern.matches(Constant.REGEX_ID_CARD, getText(etCardNumber))) {
            ToastTool.show(this, "身份证号码格式不正确");
            return false;
        }*/
        return true;
    }

    /**
     * 修改性别
     */
    public void modifyGender(View view) {
        View v = getLayoutInflater().inflate(R.layout.dialog_wheelview, null);
        final WheelView wheelView = (WheelView) v.findViewById(R.id.wheelView);
        String[] gender = {"男", "女"};
        wheelView.setItems(Arrays.asList(gender));
        new AlertView(null, null, "取消", null, new String[]{"确定"}, this,
                AlertView.Style.AlertDialog, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    tvGender.setText(wheelView.getSeletedItem());
                }
            }
        })
                .addExtView(v)
                .show();
    }
}
