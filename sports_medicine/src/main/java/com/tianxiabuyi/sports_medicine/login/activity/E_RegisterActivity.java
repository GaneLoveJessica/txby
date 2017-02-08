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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aihook.alertview.library.AlertView;
import com.aihook.alertview.library.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.eeesys.frame.view.CleanableEditText;
import com.photo.model.TResult;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BasePhotoActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.event.RegisterEvent;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.MD5;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 专家注册
 */
public class E_RegisterActivity extends BasePhotoActivity implements OnItemClickListener {
    @Bind(R.id.et_username)
    CleanableEditText etUsername;
    @Bind(R.id.cet_pwd)
    CleanableEditText cetPwd;
    @Bind(R.id.confirm_pwd)
    TextView confirmPwd;
    @Bind(R.id.cet_repwd)
    CleanableEditText cetRepwd;
    @Bind(R.id.et_phone)
    CleanableEditText etPhone;
    @Bind(R.id.tip)
    TextView tip;
    @Bind(R.id.iv_preview)
    ImageView ivPreview;
    @Bind(R.id.iv_add_certificate)
    ImageView ivAddCertificate;
    @Bind(R.id.cb_rule)
    CheckBox cbRule;
    @Bind(R.id.tv_rule)
    TextView tvRule;
    @Bind(R.id.ll_rule)
    LinearLayout llRule;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    @Bind(R.id.certificate_layout)
    RelativeLayout certificateLayout;
    private String imgUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_e__register;
    }

    @Override
    protected void initContentView() {
        title.setText("专家注册");
        ButterKnife.bind(this);
        SpannableString spannableString = new SpannableString("已阅读并同意《免责申明》和《服务条款》");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#1eb6e7")), 6,
                12, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#1eb6e7")), 13,
                19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ClickableSpan useRuleClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(E_RegisterActivity.this, RuleActivity.class);
                intent.putExtra(Constant.KEY_1, 1);
                startActivity(intent);
            }
        };
        spannableString.setSpan(useRuleClick, 6, 12, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ClickableSpan secretRuleClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(E_RegisterActivity.this, RuleActivity.class);
                intent.putExtra(Constant.KEY_1, 2);
                startActivity(intent);
            }
        };
        spannableString.setSpan(secretRuleClick, 13, 19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvRule.setText(spannableString);
        tvRule.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void addCertificate(View view) {
        hideSoftKeyboard();
        new AlertView("添加证书", null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    /**
     * 注册按钮点击事件
     *
     * @param view
     */
    public void toRegister(View view) {
        if (!checkInfo()) return;
        register();
    }

    private boolean checkInfo() {
        String username = getText(etUsername);
        String phone = getText(etPhone);
        String pwd = getText(cetPwd);
        String rePwd = getText(cetRepwd);
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
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Pattern.matches(Constant.REGEX_PHONE, phone)) {
            Toast.makeText(this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(imgUrl)) {
            Toast.makeText(this, "请上传您的专业证书", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!cbRule.isChecked()) {
            ToastTool.show(this, "请先同意免责申明和服务条款");
            return false;
        }
        return true;
    }

    private void register() {
        final String username = getText(etUsername);
        final String platName = getIntent().getStringExtra(Constant.KEY_1);
        final String unionId = getIntent().getStringExtra(Constant.KEY_2);
        Param param = new Param(Constant.REGISTER);
        param.addRequstParams("user_name", username);
        param.addRequstParams("password", MD5.md5(getText(cetPwd)));
        param.addRequstParams("repassword", MD5.md5(getText(cetRepwd)));
        param.addRequstParams("phone", getText(etPhone));
        param.addRequstParams("type", 200);
        param.addRequstParams("source", platName);
        param.addRequstParams("union_id", unionId);
        param.addRequstParams("certification", imgUrl);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                ToastTool.show(E_RegisterActivity.this, "您的注册信息已提交，请等待审核");
                UserSpUtil.setUserName(E_RegisterActivity.this, username);
//                UserSpUtil.setPwd(E_RegisterActivity.this,getText(cetRePwd));
                EventBus.getDefault().post(new RegisterEvent(platName, unionId));
                startActivity(new Intent(E_RegisterActivity.this, LoginActivity.class));
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(E_RegisterActivity.this, httpresult.getMsg());
            }
        });
    }

    @Override
    public void onItemClick(Object o, int position) {
        if (position == 0) { // 拍照
            takePhoto(false);
        } else if (position == 1) { // 选择相册
            selectPhoto(false);
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        Glide.with(this)
                .load(result.getImage().getCompressPath())
                .into(ivPreview);
        uploadPhoto(result.getImage().getCompressPath(), new PhotoUploadBack() {

            @Override
            public void onSuccess(String imgUrl) {
                E_RegisterActivity.this.imgUrl = imgUrl;
            }

            @Override
            public void onError(String s) {

            }
        });
    }
}
