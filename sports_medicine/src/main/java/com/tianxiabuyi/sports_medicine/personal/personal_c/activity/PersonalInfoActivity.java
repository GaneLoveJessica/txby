package com.tianxiabuyi.sports_medicine.personal.personal_c.activity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;
import java.util.regex.Pattern;

public class PersonalInfoActivity extends BaseActivity {
    private EditText etName;
    private TextView tvGender;
    private TextView tvBloodGroup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void initContentView() {
        title.setText("本人信息");
        tvRight.setText(R.string.edit);
        tvRight.setVisibility(View.VISIBLE);
        etName = (EditText) findViewById(R.id.et_name);
        tvGender = (TextView) findViewById(R.id.tv_gender);
        tvBloodGroup = (TextView) findViewById(R.id.tv_blood_group);
        User user = UserSpUtil.getUser(PersonalInfoActivity.this);
        if ("1".equals(user.getGender())) {
            tvGender.setText("男");
        } else if ("0".equals(user.getGender())) {
            tvGender.setText("女");
        }
        if (user.getJson() != null) {
            User.JsonBean.UserBean userBean = user.getJson().getUser();
            if (userBean != null) {
                etName.setText(userBean.getName());
                tvBloodGroup.setText(userBean.getBlood());
            }
        }
    }

    @Override
    protected void rightClick() {
        if (getText(tvRight).equals("编辑")) {
            tvRight.setText("完成");
            etName.setEnabled(true);
            tvGender.setEnabled(true);
            tvBloodGroup.setEnabled(true);
        } else {
            if (!TextUtils.isEmpty(getText(etName)) && !Pattern.matches(Constant.REGEX_NAME, getText(etName))) {
                ToastTool.show(this, "请输入正确的姓名");
                return;
            }
            uploadData();
        }
    }

    private void uploadData() {
        if (getText(etName).length() == 0 && getText(tvGender).equals("未设置") && getText(tvBloodGroup).equals("未设置")) {
            setData();
            return;
        }
        User user = UserSpUtil.getUser(this);
        Param param = new Param(Constant.HEALTH_DATA);
        param.addRequstParams("name", getText(etName));
        if ("男".equals(getText(tvGender))) {
            param.addRequstParams("gender", "1");
        } else if ("女".equals(getText(tvGender))) {
            param.addRequstParams("gender", "0");
        }
        param.addRequstParams("blood", getText(tvBloodGroup));
        param.addRequstParams("age", user.getAge());
        if (user.getJson() != null) {
            User.JsonBean.UserBean userBean = user.getJson().getUser();
            if (userBean != null) {
                param.addRequstParams("high", userBean.getHigh());
                param.addRequstParams("weight", userBean.getWeight());
                param.addRequstParams("waist", userBean.getWaist());
            }
        }
        param.addToken();
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                ToastTool.show(PersonalInfoActivity.this, "修改成功");
                setData();
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(PersonalInfoActivity.this, httpresult.getMsg());
            }
        });
    }

    private void setData() {
        etName.setEnabled(false);
        tvGender.setEnabled(false);
        tvBloodGroup.setEnabled(false);
        tvRight.setText(R.string.edit);
        User user = UserSpUtil.getUser(PersonalInfoActivity.this);
        if (user.getJson() == null) {
            user.setJson(new User.JsonBean());
        }
        User.JsonBean.UserBean userBean = user.getJson().getUser();
        if (userBean == null) {
            userBean = new User.JsonBean.UserBean();
            user.getJson().setUser(userBean);
        }
        userBean.setName(getText(etName));
        userBean.setBlood(getText(tvBloodGroup));
        if ("男".equals(getText(tvGender))) {
            user.setGender("1");
        } else if ("女".equals(getText(tvGender))) {
            user.setGender("0");
        }
        UserSpUtil.setUserInfo(PersonalInfoActivity.this, user);
        resetTextColor();
    }

    public void onClick(View view) {
        resetTextColor();
        TextView textView = (TextView) view;
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        switch (view.getId()) {
            case R.id.tv_gender:
                String[] gender = {"女", "男"};
                chooseDialog(tvGender, Arrays.asList(gender));
                break;
            case R.id.tv_blood_group:
                String[] bloodGroup = {"未设置", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
                chooseDialog(tvBloodGroup, Arrays.asList(bloodGroup));
                break;
        }
    }

    private void resetTextColor() {
        etName.setTextColor(getResources().getColor(android.R.color.darker_gray));
        tvGender.setTextColor(getResources().getColor(android.R.color.darker_gray));
        tvBloodGroup.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    private void chooseDialog(final TextView textView, List<String> items) {
        ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alert_wheelview, null);
        final WheelView wheelView = (WheelView) extView.findViewById(R.id.wheelView);
        wheelView.setItems(items);
        AlertView chooseDialog = new AlertView("选择", null, null,
                new String[]{"确定"}, new String[]{"取消"}, this, AlertView.Style.AlertDialog, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    textView.setText(wheelView.getSeletedItem());
                }
            }
        });
        chooseDialog.addExtView(extView);
        chooseDialog.show();
    }
}
