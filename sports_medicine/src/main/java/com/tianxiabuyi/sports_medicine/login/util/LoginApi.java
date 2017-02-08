package com.tianxiabuyi.sports_medicine.login.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Administrator on 2016/9/30.
 */

public class LoginApi implements Handler.Callback {
    private static final int MSG_AUTH_CANCEL = 1;
    private static final int MSG_AUTH_ERROR = 2;
    private static final int MSG_AUTH_COMPLETE = 3;
    private static final String TAG = "LoginApi";
    private Handler handler;
    private Context context;
    private String platform;
    private OnAuthorizeListener listener;
    private ProgressDialog pd;

    public LoginApi() {
        handler = new Handler(Looper.getMainLooper(), this);
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setOnLoginListener(OnAuthorizeListener listener) {
        this.listener = listener;
    }

    public void login(Context context) {
        this.context = context.getApplicationContext();
        if (platform == null) {
            return;
        }

        //初始化SDK
        ShareSDK.initSDK(context);
        Platform plat = ShareSDK.getPlatform(platform);
        if (plat == null) {
            return;
        }

        if (plat.isAuthValid()) {
            listener.authorizeSuccess(plat);
            return;
        }

        //使用SSO授权，通过客户单授权
        plat.SSOSetting(false);
        pd = ProgressDialog.show(context, null, "加载中...", false, true);
        plat.setPlatformActionListener(new PlatformActionListener() {
            public void onComplete(Platform plat, int action,HashMap<String, Object> res) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_COMPLETE;
                    msg.arg2 = action;
                    msg.obj =  new Object[] {plat.getName(), res};
                    handler.sendMessage(msg);
                }
            }

            public void onError(Platform plat, int action, Throwable t) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_ERROR;
                    msg.arg2 = action;
                    msg.obj = t;
                    handler.sendMessage(msg);
                }
                t.printStackTrace();
            }

            public void onCancel(Platform plat, int action) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_CANCEL;
                    msg.arg2 = action;
                    msg.obj = plat;
                    handler.sendMessage(msg);
                }
            }
        });
        plat.showUser(null);
    }

    @Override
    public boolean handleMessage(Message msg) {
        pd.dismiss();
        switch (msg.what) {
            case MSG_AUTH_CANCEL: {
                // 取消
                Toast.makeText(context, "取消授权", Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_ERROR: {
                // 失败
                Throwable t = (Throwable) msg.obj;
                Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
            break;
            case MSG_AUTH_COMPLETE: {
                // 成功
                Object[] objs = (Object[]) msg.obj;
                String plat = (String) objs[0];
                Platform platform = ShareSDK.getPlatform(plat);
                listener.authorizeSuccess(platform);
            }
            break;
        }
        return false;
    }

    public interface OnAuthorizeListener {
        void authorizeSuccess(Platform platform);
    }
}
