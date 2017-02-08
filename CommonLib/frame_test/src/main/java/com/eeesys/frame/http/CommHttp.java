package com.eeesys.frame.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;

import com.eeesys.frame.BuildConfig;
import com.eeesys.frame.utils.Encrpt;
import com.eeesys.frame.utils.GsonTool;
import com.eeesys.frame.utils.NetUtils;
import com.eeesys.frame.utils.ToastTool;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by madc
 * 对xutils网络访问进行适用本公司流程的封装
 * 要实现如下逻辑
 * 判断网络状态
 * 是否有token 有--->判断token超时--->刷新token
 * 判断是否有参数，判断是否加密
 * 所有逻辑方法写在这儿
 * 所有方法的实现写在继承这个类的里面
 * http://xxxxxxxxxx?json=xxxxxxxxxx
 */
public abstract class CommHttp implements HttpInterface.MyHttp {
    // 传递的参数名
    public String requstParam = "json";
    // token过期
    public int TOKEN_ERR_O = 20002;
    // token未认证
    public int TOKEN_ERR_T = 20004;
    public ProgressDialog pd;

    public static volatile boolean isRefreshingToken = false;    //是否正在刷新token，解决并发请求重复刷新token导致的token未认证问题
    private Callback.Cancelable cancelable;

    public void HttpRequest(final Context context, final HttpBean bean, final HttpBack back) {
        RequestParams requestParams = new RequestParams(bean.getUrl());
        requestParams.setConnectTimeout(20000);
        //判断网络
        if (!NetUtils.isConnected(context)) {
            HttpResult httpResult = new HttpResult("{\"errcode\":100,\"errmsg\":\"网络异常,请连接网络\"}");
            back.err(httpResult);
//            ToastTool.show(context, R.string.connection_err);
            return;
        }
        if (bean.getIsShowLoading() && pd == null) {
            pd = ProgressDialog.show(context, null, "加载中...", false, true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (cancelable != null) {
                        cancelable.cancel();
                    }
                }
            });
            pd.setCanceledOnTouchOutside(false);
        }
        // 是否是带token的请求
        if (bean.containsToken()) {
            bean.setRefTokenParam();
            // 判断TOKEN是否超时，超时则刷新
            if (tokenOut()) {
                refreshToken(context, bean, back);
                return;
            }
        }
        if (!bean.getIsOnlyUrl()) {
            Log.e("requestParams", bean.toString());

            if (bean.getPostEncrpt()) {
                requestParams.addBodyParameter(requstParam, Encrpt.encryptStr(GsonTool.toJson(bean.getMap())));
            } else {
                requestParams.addBodyParameter(requstParam, GsonTool.toJson(bean.getMap()));
            }
        }
        cancelable = x.http().post(requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if (bean.getResultEncrpt()) {
                    result = Encrpt.decryptStr(result);
                }
                HttpResult httpresult = new HttpResult(result);
                Log.e("response", TextUtils.concat(bean.getUrl(), " ", result).toString());
                // token相关错误
                if (httpresult.getErrCode() == TOKEN_ERR_O || httpresult.getErrCode() == TOKEN_ERR_T) {
                    if (BuildConfig.DEBUG) {
                        ToastTool.show(context, httpresult.getMsg());
                    }
                    refreshToken(context, bean, back);
                } else if (httpresult.isSuccess()) { // 成功
                    back.success(httpresult);
                } else { // 其他状态码

                    //ToastTool.show(context,httpresult.getResult());
                    back.err(httpresult);

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
//                ToastTool.show(context, "网络异常");
                HttpResult httpResult = new HttpResult("{\"errcode\":101,\"errmsg\":\"网络异常\"}");
                back.err(httpResult);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                cex.printStackTrace();
            }

            @Override
            public void onFinished() {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });
    }

    public interface HttpBack {
        void success(HttpResult httpresult);

        void err(HttpResult httpresult);
    }


    private void refreshToken(final Context context, final HttpBean b, final HttpBack back) {
        if (!CommHttp.isRefreshingToken) {
            CommHttp.isRefreshingToken = true;
            RequestParams requestParams = new RequestParams(b.getDefultRefTokenUrl());
            requestParams.setConnectTimeout(10000);
            requestParams.addBodyParameter(requstParam, b.getDefultRefTokenParmers());
            x.http().post(requestParams, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    CommHttp.isRefreshingToken = false;

                    result = Encrpt.decryptStr(result);
                    Log.e("refreshToken", result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getInt("errcode") == 0) {
                            upTokenState(context, b, jsonObject.getString("token"));
                            // 再次请求后台
                            HttpRequest(context, b, back);
                        } else {
                            restartLogin(context);
                        }
                    } catch (JSONException e) {
                        restartLogin(context);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    restartLogin(context);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    CommHttp.isRefreshingToken = false;
                }
            });
        } //TODO else throw new Exception
    }
}
