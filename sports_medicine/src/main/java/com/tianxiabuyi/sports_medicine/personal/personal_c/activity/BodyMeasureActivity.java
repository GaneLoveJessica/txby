package com.tianxiabuyi.sports_medicine.personal.personal_c.activity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 身体测量
 */
public class BodyMeasureActivity extends BaseActivity implements View.OnFocusChangeListener {
    @Bind(R.id.et_age)
    EditText etAge;
    @Bind(R.id.et_gender)
    TextView tvGender;
    @Bind(R.id.et_height)
    EditText etHeight;
    @Bind(R.id.et_weight)
    EditText etWeight;
    @Bind(R.id.et_waistline)
    EditText etWaistline;
    @Bind(R.id.activity_personal_info)
    LinearLayout activityPersonalInfo;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.ll_result)
    LinearLayout llResult;

    private AlertView chooseDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_body_measure;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        title.setText(R.string.body_measure);
        tvRight.setText(R.string.edit);
        tvRight.setVisibility(View.VISIBLE);
        etAge.setOnFocusChangeListener(this);
        etHeight.setOnFocusChangeListener(this);
        etWaistline.setOnFocusChangeListener(this);
        etWeight.setOnFocusChangeListener(this);
        User user = UserSpUtil.getUser(BodyMeasureActivity.this);
        if ("1".equals(user.getGender())) {
            tvGender.setText("男");
        } else if ("0".equals(user.getGender())) {
            tvGender.setText("女");
        }
        etAge.setText(TextUtils.concat(user.getAge() + ""));
        if (user.getJson() != null) {
            User.JsonBean.UserBean userBean = user.getJson().getUser();
            if (userBean != null) {
                etHeight.setText(TextUtils.concat(userBean.getHigh()));
                etWeight.setText(TextUtils.concat(userBean.getWeight()));
                etWaistline.setText(TextUtils.concat(userBean.getWaist()));
            }
        }
        setResult();
    }

    private void setResult() {
        User user = UserSpUtil.getUser(BodyMeasureActivity.this);
        tvResult.setText("");
        if (user.getJson() != null) {
            User.JsonBean.UserBean userBean = user.getJson().getUser();
            if (userBean != null) {
                int i = 1;
                if (userBean.getHigh() != null && userBean.getHigh().length() > 0 && !userBean.getHigh().equals("0")
                        && userBean.getWeight() != null && userBean.getWeight().length() > 0) {
                    float height = Float.valueOf(userBean.getHigh());
                    double bmi = Float.valueOf(userBean.getWeight()) / (height * 0.01 * height * 0.01);
                    if (bmi < 18.5) {
                        tvResult.setText(i + ".您的体重过低，发病危险（与肥胖相关疾病）系数：高\n");
                    } else if (bmi >= 18.5 && bmi <= 24.9) {
                        tvResult.setText(i + ".您的体重属于正常范围，发病危险（与肥胖相关疾病）系数：平均水平\n");
                    } else if (bmi >= 25 && bmi <= 29.9) {
                        tvResult.setText(i + ".您的体重超重，发病危险（与肥胖相关疾病）系数：增高\n");
                    } else if (bmi >= 30 && bmi <= 34.9) {
                        tvResult.setText(i + ".您的体重处于I度肥胖，发病危险（与肥胖相关疾病）系数：中等\n");
                    } else if (bmi >= 35 && bmi <= 39.9) {
                        tvResult.setText(i + ".您的体重处于II度肥胖，发病危险（与肥胖相关疾病）系数：严重\n");
                    } else {
                        tvResult.setText(i + ".您的体重处于III度肥胖，发病危险（与肥胖相关疾病）系数：极为严重\n");
                    }
                }
                if (getText(tvResult).length() > 0) {
                    i++;
                }
                if (userBean.getWaist() != null && userBean.getWaist().length() > 0) {
                    if ("1".equals(user.getGender()) && Integer.valueOf(userBean.getWaist()) >= 85) {
                        tvResult.append(i + ".您可能患有向心性肥胖。内脏脂肪相比于表皮脂肪更加严重得影响着人体" +
                                "正常的新陈代谢，并与多种慢性代谢病有着密切的关系，如心脑血管疾病，高血压，" +
                                "高血脂，肥胖症，糖尿病等。\n");
                    }
                    if ("0".equals(user.getGender()) && Integer.valueOf(userBean.getWaist()) >= 80) {
                        tvResult.append(i + ".您可能患有向心性肥胖。内脏脂肪相比于表皮脂肪更加严重得影响着人体" +
                                "正常的新陈代谢，并与多种慢性代谢病有着密切的关系，如心脑血管疾病，高血压，" +
                                "高血脂，肥胖症，糖尿病等。\n");
                    }
                }
                if (getText(tvResult).length() > 0) {
                    llResult.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void rightClick() {
        if (getText(tvRight).equals("编辑")) {
            tvRight.setText(R.string.complete);
            etAge.setEnabled(true);
            tvGender.setEnabled(true);
            etHeight.setEnabled(true);
            etWeight.setEnabled(true);
            etWaistline.setEnabled(true);
            llResult.setVisibility(View.INVISIBLE);
        } else {
            uploadData();
        }
    }

    private void uploadData() {
        User user = UserSpUtil.getUser(BodyMeasureActivity.this);
        Param param = new Param(Constant.HEALTH_DATA);
        param.addRequstParams("high", getText(etHeight));
        param.addRequstParams("weight", getText(etWeight));
        param.addRequstParams("waist", getText(etWaistline));
        if (user.getJson() != null) {
            User.JsonBean.UserBean userBean = user.getJson().getUser();
            if (userBean != null) {
                param.addRequstParams("name", userBean.getName());
                param.addRequstParams("blood", userBean.getBlood());
            }
        }
        if ("男".equals(getText(tvGender))) {
            param.addRequstParams("gender", "1");
        } else if ("女".equals(getText(tvGender))) {
            param.addRequstParams("gender", "0");
        }
        param.addRequstParams("age", getText(etAge));
        param.addToken();
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                ToastTool.show(BodyMeasureActivity.this, "修改成功");
                saveData();
                setResult();
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(BodyMeasureActivity.this, httpresult.getMsg());
            }
        });
    }

    private void saveData() {
        etAge.setEnabled(false);
        tvGender.setEnabled(false);
        etHeight.setEnabled(false);
        etWeight.setEnabled(false);
        etWaistline.setEnabled(false);
        tvRight.setText(R.string.edit);
        User user = UserSpUtil.getUser(this);
        if ("男".equals(getText(tvGender))) {
            user.setGender("1");
        } else if ("女".equals(getText(tvGender))) {
            user.setGender("0");
        }
        user.setAge(getText(etAge));
        if (user.getJson() == null) {
            user.setJson(new User.JsonBean());
        }
        User.JsonBean.UserBean userBean = user.getJson().getUser();
        if (userBean == null) {
            userBean = new User.JsonBean.UserBean();
            user.getJson().setUser(userBean);
        }
        userBean.setHigh(getText(etHeight));
        userBean.setWeight(getText(etWeight));
        userBean.setWaist(getText(etWaistline));
        UserSpUtil.setUserInfo(this, user);
    }

    @OnClick(R.id.et_gender)
    public void onClick() {
        tvGender.requestFocus();
        chooseGenderDialog();
    }

    private void chooseGenderDialog() {
        if (chooseDialog == null) {
            String[] gender = {"女", "男"};
            ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alert_wheelview, null);
            final WheelView wheelView = (WheelView) extView.findViewById(R.id.wheelView);
            wheelView.setItems(Arrays.asList(gender));
            chooseDialog = new AlertView("选择", null, "取消",
                    null, new String[]{"确定"}, this, AlertView.Style.AlertDialog, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position == 0) {
                        tvGender.setText(wheelView.getSeletedItem());
                    }
                }
            });
            chooseDialog.addExtView(extView);
        }
        chooseDialog.show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText editText = (EditText) v;
        if (hasFocus) {
            editText.setSelection(getText(editText).length());
        }
    }
}
