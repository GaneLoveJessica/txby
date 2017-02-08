package com.tianxiabuyi.sports_medicine.login.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.eeesys.frame.view.CleanableEditText;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.event.RegisterEvent;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.MD5;

import java.util.regex.Pattern;

/**
 * 普通用户注册
 */
public class C_RegisterActivity extends BaseActivity {
    private EditText etPhone;
    private CleanableEditText cetPwd;
    private CleanableEditText cetRePwd;
    private EditText etUsername;
    private CheckBox cbRule;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_c__register;
    }

    @Override
    protected void initContentView() {
        title.setText("普通注册");
        etUsername = (EditText) findViewById(R.id.et_account);
        etPhone = (EditText) findViewById(R.id.et_phone);
        cetPwd = (CleanableEditText) findViewById(R.id.cet_pwd);
        cetRePwd = (CleanableEditText) findViewById(R.id.cet_repwd);
        cbRule = (CheckBox) findViewById(R.id.cb_rule);
        TextView tvRule = (TextView) findViewById(R.id.tv_rule);
        SpannableString spannableString = new SpannableString("已阅读并同意《免责申明》和《服务条款》");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#1eb6e7")), 6,
                12, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#1eb6e7")), 13,
                19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ClickableSpan useRuleClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(C_RegisterActivity.this, RuleActivity.class);
                intent.putExtra(Constant.KEY_1, 1);
                startActivity(intent);
            }
        };
        spannableString.setSpan(useRuleClick, 6, 12, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ClickableSpan secretRuleClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(C_RegisterActivity.this, RuleActivity.class);
                intent.putExtra(Constant.KEY_1, 2);
                startActivity(intent);
            }
        };
        spannableString.setSpan(secretRuleClick, 13, 19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvRule.setText(spannableString);
        tvRule.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void toRegister(View view) {
        if (!checkInfo()) return;
        register();
    }

    /**
     * 检查输入信息
     *
     * @return
     */
    private boolean checkInfo() {
        String username = getText(etUsername);
        String pwd = getText(cetPwd);
        String rePwd = getText(cetRePwd);
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Pattern.matches(Constant.REGEX_USER_NAME, username)) {
            Toast.makeText(this, "用户名格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastTool.show(this, "请输入密码");
            return false;
        }
        if (!Pattern.matches(Constant.REGEX_PASSWORD, pwd)) {
            ToastTool.show(this, "密码格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(rePwd)) {
            ToastTool.show(this, "请输入确认密码");
            return false;
        }
        if (!pwd.equals(rePwd)) {
            ToastTool.show(this, "两次输入的密码不一致");
            return false;
        }
        if (!checkPhoneNumber()) {
            return false;
        }
        if (!cbRule.isChecked()) {
            ToastTool.show(this, "请先同意免责申明和服务条款");
            return false;
        }
        return true;
    }

    /**
     * 检查手机号码
     *
     * @return
     */
    private boolean checkPhoneNumber() {
        String phone = getText(etPhone);
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Pattern.matches(Constant.REGEX_PHONE, phone)) {
            Toast.makeText(this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 注册请求
     */
    private void register() {
        final String platName = getIntent().getStringExtra(Constant.KEY_1);
        final String unionId = getIntent().getStringExtra(Constant.KEY_2);
        Param param = new Param(Constant.REGISTER);
        param.addRequstParams("user_name", getText(etUsername));
        param.addRequstParams("password", MD5.md5(getText(cetPwd)));
        param.addRequstParams("repassword", MD5.md5(getText(cetRePwd)));
        param.addRequstParams("phone", getText(etPhone));
        param.addRequstParams("type", "100");
        param.addRequstParams("source", platName);
        param.addRequstParams("union_id", unionId);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                ToastTool.show(C_RegisterActivity.this, "注册成功");
                UserSpUtil.setUserName(C_RegisterActivity.this, getText(etUsername));
                UserSpUtil.setPwd(C_RegisterActivity.this, getText(cetRePwd));
                EventBus.getDefault().post(new RegisterEvent(platName, unionId));
                startActivity(new Intent(C_RegisterActivity.this, LoginActivity.class));

            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(C_RegisterActivity.this, httpresult.getMsg());
            }
        });
    }
}
