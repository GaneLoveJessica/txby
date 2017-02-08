package com.tianxiabuyi.sports_medicine.personal;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aihook.alertview.library.AlertView;
import com.aihook.alertview.library.OnItemClickListener;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.photo.model.TResult;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BasePhotoActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.util.LoginApi;
import com.tianxiabuyi.sports_medicine.login.util.ThirdAccountDbUtil;
import com.tianxiabuyi.sports_medicine.model.ThirdAccount;
import com.tianxiabuyi.sports_medicine.model.User;
import com.tianxiabuyi.sports_medicine.personal.personal_c.activity.C_ModifyDataActivity;
import com.tianxiabuyi.sports_medicine.personal.personal_c.event.ModifyPhoneEvent;
import com.tianxiabuyi.sports_medicine.personal.personal_e.activity.E_ModifyDataActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import static com.tianxiabuyi.sports_medicine.common.util.UserSpUtil.getUser;

/**
 * 用户信息
 */
public class DataActivity extends BasePhotoActivity implements LoginApi.OnAuthorizeListener {
    private static final String TAG = "DataActivity";
    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.tv_point_detail)
    TextView tvPointDetail;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_qq)
    TextView tvQq;
    @Bind(R.id.tv_wechat)
    TextView tvWechat;
    @Bind(R.id.tv_weibo)
    TextView tvWeibo;
    private LoginApi loginApi;
    private User user;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_data;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        ShareSDK.initSDK(this);
        EventBus.getDefault().register(this);
        title.setText(R.string.personal_center);
        user = UserSpUtil.getUser(this);
        if (user.getType() == 100) {
            tvPointDetail.setVisibility(View.VISIBLE);
        }
        if (user.getAvatar() == null || user.getAvatar().equals("http://image.eeesys.com/default/user_m.png")
                || user.getAvatar().equals("http://image.eeesys.com/default/doctor_m.png")) {
            ivAvatar.setImageResource(R.mipmap.avatar);
        } else {
            GlideUtil.setCircleAvatar(this, ivAvatar, user.getAvatar());
        }
        tvUsername.setText(user.getUser_name());
        String phone = user.getPhone();
        if (phone.length() == 11) {
            tvPhone.setText(new StringBuilder(user.getPhone()).replace(3, 7, "****"));
        }
        setAccountData("qq", tvQq);
        setAccountData("wechat", tvWechat);
//        setAccountData("weibo", tvWeibo);

        loginApi = new LoginApi();
        loginApi.setOnLoginListener(this);
    }

    private void setAccountData(String source, TextView textView) {
        ThirdAccount account = ThirdAccountDbUtil.getInstance().query(source);
        if (account != null) {
            textView.setText("已绑定");
            textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        } else {
            textView.setText("未绑定");
            ShareSDK.getPlatform(getSource(source)).removeAccount(true);
            textView.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onModifyPhone(ModifyPhoneEvent event) {
        tvPhone.setText(new StringBuilder(user.getPhone()).replace(3, 7, "****"));
    }

    /**
     * 修改手机号码
     *
     * @param view
     */
    public void modifyPhone(View view) {
        startActivity(new Intent(this, ModifyPhoneActivity.class));
    }

    @OnClick({R.id.tv_share_app, R.id.tv_setting, R.id.ll_qq, R.id.ll_wechat, R.id.ll_weibo, R.id.iv_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
                modifyAvatar();
                break;
            case R.id.tv_share_app:
                shareApp();
                break;
            case R.id.tv_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.ll_qq:
                bindAccount(tvQq, "qq", QQ.NAME);
                break;
            case R.id.ll_wechat:
                bindAccount(tvWechat, "wechat", Wechat.NAME);
                break;
            case R.id.ll_weibo:
                ToastTool.show(this, "暂未开放");
//                bindAccount(tvWeiBo, "weibo", SinaWeibo.NAME);
                break;
        }
    }

    private void modifyAvatar() {
        new AlertView("添加图片", null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    takePhoto(true);
                } else if (position == 1) {
                    selectPhoto(true);
                }
            }
        })
                .show();
    }

    private void shareApp() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getResources().getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        String url = Constant.SHARE_PP_URL + user.getUid();
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("旨于倡导科学健身、关注运动防护、专业康复医疗。");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(Constant.SHARE_IMG_URL);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        // 启动分享GUI
        oks.show(this);
    }

    private void bindAccount(TextView textView, final String source, String name) {
        if (textView.getText().toString().equals("已绑定")) {
            new AlertView("提示", "确定要解除绑定吗？", "取消", null, new String[]{"确定"}, this,
                    AlertView.Style.AlertDialog, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position == 0) {
                        unBindThirdAccount(ThirdAccountDbUtil.getInstance().query(source));
                    }
                }
            }).show();
        } else {
            loginApi.setPlatform(name);
            loginApi.login(this);
        }
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
        if (name.equals("qq")) {
            return "QQ";
        }
        if (name.equals("wechat")) {
            return "Wechat";
        }
        if (name.equals("weibo")) {
            return "SinaWeibo";
        }
        return name;
    }

    /**
     * 绑定第三方账号
     *
     * @param source
     * @param userId
     */
    private void bindThirdAccount(final String source, final String userId) {
        Param param = new Param(Constant.BIND_THIRD_ACCOUNT);
        param.addToken();
        param.addRequstParams("source", source);
        param.addRequstParams("union_id", userId);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                if (source.equals("qq")) {
                    tvQq.setText("已绑定");
                    tvQq.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else if (source.equals("wechat")) {
                    tvWechat.setText("已绑定");
                    tvWechat.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else if (source.equals("weibo")) {
                    tvWeibo.setText("已绑定");
                    tvWeibo.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
                ThirdAccountDbUtil.getInstance().save(source, userId);
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(DataActivity.this, httpresult.getMsg());
            }
        });
    }

    /**
     * 解绑第三方账号请求
     *
     * @param thirdAccount
     */
    private void unBindThirdAccount(final ThirdAccount thirdAccount) {
        Param param = new Param(Constant.UNBIND_THIRD_ACCOUNT);
        param.addToken();
        param.addRequstParams("source", thirdAccount.getSource());
        param.addRequstParams("union_id", thirdAccount.getUnion_id());
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                if (thirdAccount.getSource().equals("qq")) {
                    tvQq.setText("未绑定");
                    tvQq.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                    ShareSDK.getPlatform(QQ.NAME).removeAccount(true);
                } else if (thirdAccount.getSource().equals("wechat")) {
                    tvWechat.setText("未绑定");
                    tvWechat.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                    ShareSDK.getPlatform(Wechat.NAME).removeAccount(true);
                } else if (thirdAccount.getSource().equals("weibo")) {
                    tvWeibo.setText("未绑定");
                    tvWeibo.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
//                    ShareSDK.getPlatform(SinaWeibo.NAME).removeAccount(true);
                }
                ThirdAccountDbUtil.getInstance().removeAccount(thirdAccount.getUnion_id());
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(DataActivity.this, httpresult.getMsg());
            }
        });
    }

    @Override
    public void authorizeSuccess(Platform platform) {
        bindThirdAccount(getSource(platform.getName()), platform.getDb().getUserId());
    }

    /**
     * 修改密码
     *
     * @param view
     */
    public void modifyPwd(View view) {
        startActivity(new Intent(this, ModifyPwdActivity.class));
    }

    /**
     * 修改资料
     *
     * @param view
     */
    public void modifyData(View view) {
        if (user.getType() == 100) {
            startActivity(new Intent(this, C_ModifyDataActivity.class));
        } else {
            startActivity(new Intent(this, E_ModifyDataActivity.class));
        }
    }

    /**
     * 积分详情
     *
     * @param view
     */
    public void myPointDetail(View view) {
        startActivity(new Intent(this, MyPointActivity.class));
    }

    /**
     * 绑定头像
     *
     * @param imgUrl
     */
    private void bindAvatar(final String imgUrl) {
        Param param = new Param(Constant.BIND_AVATAR);
        param.addToken();
        param.addRequstParams("avatar", imgUrl);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                User user = getUser(DataActivity.this);
                user.setAvatar(imgUrl);
                UserSpUtil.setUserInfo(DataActivity.this, user);
                GlideUtil.setCircleAvatar(DataActivity.this, ivAvatar, imgUrl);
                ToastTool.show(DataActivity.this, R.string.toast_modify_avatar_success);
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(DataActivity.this, httpresult.getMsg());
            }
        });
    }

    @Override
    public void takeSuccess(TResult result) {
        uploadPhoto(result.getImage().getCompressPath(), new PhotoUploadBack() {
            @Override
            public void onSuccess(String imgUrl) {
                bindAvatar(imgUrl);
            }

            @Override
            public void onError(String s) {

            }
        });
    }
}
