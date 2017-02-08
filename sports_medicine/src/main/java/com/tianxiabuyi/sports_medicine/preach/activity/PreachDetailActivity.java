package com.tianxiabuyi.sports_medicine.preach.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.model.NewsImage;
import com.tianxiabuyi.sports_medicine.model.Preach;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;

/**
 * 宣讲详情
 */
public class PreachDetailActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_author)
    TextView tvAuthor;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.iv_qr_code)
    ImageView ivQrCode;
    private Preach preach;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preach_detail;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        title.setText(R.string.detail);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.mipmap.icon_share);
        // 初始化分享
        ShareSDK.initSDK(this);
        preach = getIntent().getParcelableExtra(Constant.KEY_1);
        Glide.with(this).load(Constant.QRCODE_IMG_URL).into(ivQrCode);
        webView.getSettings().setSavePassword(false);
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibilityTraversal");
        webView.removeJavascriptInterface("accessibility");
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        requestData();
    }

    private void requestData() {
        Param param = new Param(Constant.PREACH_DETAIL);
        param.addRequstParams("news_id", preach.getNews_id());
        param.addRequstParams("uid", UserSpUtil.getUid(this));
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                Preach newsDetail = httpresult.getObjectResult("news", Preach.class);
                setData(newsDetail);
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(PreachDetailActivity.this, httpresult.getMsg());
            }
        });
    }

    private void setData(Preach preach) {
        tvTitle.setText(preach.getTitle());
        tvAuthor.setText(preach.getAuthor());
        String content = preach.getContent();
        if (preach.getImg() != null) {
            List<NewsImage> list = preach.getImg();
            for (int i = 0; i < list.size(); i++) {
                String image = "<img src=\"" + list.get(i).getSrc() + "\" width=\"100%\"/>";
                content = content.replace(list.get(i).getRef(), image);
            }
        }
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        ivQrCode.setVisibility(View.VISIBLE);
    }

    @Override
    protected void rightClick() {
        String url = Constant.NEWS_URL + preach.getNews_id() + "&hospital=1068";
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(preach.getTitle());
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(getString(R.string.app_name));
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        if (preach.getJson().getNews_thumbnail() != null) {
            oks.setImageUrl(preach.getJson().getNews_thumbnail());
        } else {
            oks.setImageUrl(Constant.SHARE_IMG_URL);
        }
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        // 设置自定义的外部回调
        oks.setCallback(new OneKeyShareCallback());
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if (QZone.NAME.equals(platform.getName()) || QQ.NAME.equals(platform.getName())){
                    paramsToShare.setText(preach.getTitle());
                    paramsToShare.setTitle(platform.getContext().getString(R.string.app_name));
                }
            }
        });
        // 启动分享GUI
        oks.show(this);
    }

    private class OneKeyShareCallback implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            ToastTool.show(PreachDetailActivity.this, "分享成功");
            if (UserSpUtil.isLogin(PreachDetailActivity.this)) {
                uploadScore();
            }
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(Platform platform, int i) {

        }
    }

    private void uploadScore() {
        Param param = new Param(Constant.ADD_SCORE);
        param.addRequstParams("id", preach.getId());
        param.addToken();
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {

            }

            @Override
            public void err(HttpResult httpresult) {

            }
        });
    }
}
