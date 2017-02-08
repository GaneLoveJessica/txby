package com.tianxiabuyi.sports_medicine.login.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.eeesys.frame.view.CleanableEditText;
import com.google.gson.reflect.TypeToken;
import com.tencent.android.tpush.XGPushManager;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.AndroidBug5497Workaround;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.event.LoginEvent;
import com.tianxiabuyi.sports_medicine.login.event.RegisterEvent;
import com.tianxiabuyi.sports_medicine.login.util.LoginApi;
import com.tianxiabuyi.sports_medicine.login.util.ThirdAccountDbUtil;
import com.tianxiabuyi.sports_medicine.model.ThirdAccount;
import com.tianxiabuyi.sports_medicine.model.Token;
import com.tianxiabuyi.sports_medicine.model.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements LoginApi.OnAuthorizeListener {
    private CleanableEditText cetUserName;
    private CleanableEditText petPwd;

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initContentView() {
        AndroidBug5497Workaround.assistActivity(this);
        EventBus.getDefault().register(this);
        cetUserName = (CleanableEditText) findViewById(R.id.cet_username);
        petPwd = (CleanableEditText) findViewById(R.id.pet_pwd);
        ShareSDK.initSDK(this);
        cetUserName.setText(UserSpUtil.getUsername(this));
    }

    @Subscribe
    public void onRegisterEvent(RegisterEvent event) {
        String platformName = event.getPlatName();
        String userId = event.getUnionId();
        String password = UserSpUtil.getPwd(this);
        if (!TextUtils.isEmpty(password)) {
            if (platformName != null && userId != null) {
                thirdLogin(platformName, userId);
            } else {
                loadUser(UserSpUtil.getUsername(this), password);
            }
        }
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new LoginEvent(false));
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void loadUser(String username, final String password) {
        hideSoftKeyboard();
        Param param = new Param(Constant.LOGIN);
        param.addRequstParams("user_name", username);
        param.addRequstParams("password", MD5.md5(password));
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                saveUserData(httpresult, null);
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(LoginActivity.this, httpresult.getMsg());
            }
        });
    }

    private void saveUserData(HttpResult httpresult, String userId) {
        User user = httpresult.getObjectResult("user", User.class);
        Token token = httpresult.getObjectResult("auth", Token.class);
        // 注册信鸽
        Context context = getApplicationContext();
        XGPushManager.registerPush(context, user.getUid() + "");
        UserSpUtil.setUserInfo(LoginActivity.this, user);
        UserSpUtil.setToken(LoginActivity.this, token.getToken());
        UserSpUtil.setPwd(LoginActivity.this, getText(petPwd));
        UserSpUtil.setTime(LoginActivity.this);
        UserSpUtil.setUnionId(LoginActivity.this, userId);
        try {
            JSONObject jsonObject = new JSONObject(httpresult.getResult());
            UserSpUtil.setManager(LoginActivity.this,jsonObject.optString("manager"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<ThirdAccount> accounts = httpresult.getListResult("account", new TypeToken<List<ThirdAccount>>() {
        });
        ThirdAccountDbUtil.getInstance().saveAll(accounts);
        EventBus.getDefault().post(new LoginEvent(true));
        finish();
    }

    private void thirdLogin(final String name, final String userId) {
        Param param = new Param(Constant.LOGIN);
        param.addRequstParams("source", name);
        param.addRequstParams("union_id", userId);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                saveUserData(httpresult,userId);
            }

            @Override
            public void err(HttpResult httpresult) {
                Intent intent = new Intent(LoginActivity.this, ChooseIdentityActivity.class);
                intent.putExtra(Constant.KEY_1, name);
                intent.putExtra(Constant.KEY_2, userId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void back(View view) {
        EventBus.getDefault().post(new LoginEvent(false));
        finish();
    }

    public void toLogin(View v) {
        if (!checkInfo()) return;
        loadUser(getText(cetUserName), getText(petPwd));
    }

    private boolean checkInfo() {
        String username = getText(cetUserName);
        String pwd = getText(petPwd);
        if (TextUtils.isEmpty(username)) {
            ToastTool.show(this, "请输入用户名");
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastTool.show(this, "请输入密码");
            return false;
        }
        return true;
    }

    public void toRegister(View v) {
        startActivity(new Intent(this, ChooseIdentityActivity.class));
    }

    /**
     * 第三方登录
     */
    public void otherLogin(View view) {
        LoginApi loginApi = new LoginApi();
        loginApi.setOnLoginListener(this);
        switch (view.getId()) {
            // QQ
            case R.id.iv_login_qq:
                loginApi.setPlatform(QQ.NAME);
                break;
            // 微信
            case R.id.iv_login_wechat:
                loginApi.setPlatform(Wechat.NAME);
                break;
            // 新浪微博
            case R.id.iv_login_blog:
                ToastTool.show(LoginActivity.this, "暂未开放");
//                loginApi.setPlatform(SinaWeibo.NAME);
                break;
        }
        loginApi.login(this);
    }

    private String getSource(String name) {
        if (name.equals("QQ")) {
            return "qq";
        }
        if (name.equals("Wechat")) {
            return "wechat";
        }
        if (name.equals("SinaWeibo")) {
            return "weibo";
        }
        return name;
    }

    @Override
    public void authorizeSuccess(Platform platform) {
        String userId = platform.getDb().getUserId();
        thirdLogin(getSource(platform.getName()), userId);
    }
}
