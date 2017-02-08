package com.tianxiabuyi.sports_medicine.question.util;

import android.content.Context;
import android.view.View;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.model.Question;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/12/26.
 */

public class LoveRequest {
    public static void cancelLove(Context context, final View view, final Question question, final boolean isLove, final LoveCallback callback) {
        Param param = new Param(Constant.CANCEL_PRAISE);
        param.addToken();
        if (isLove) {
            param.addRequstParams("id", question.getLoved_id());
            param.addRequstParams("operate", 3);
        } else {
            param.addRequstParams("id", question.getTreaded_id());
            param.addRequstParams("operate", 4);
        }
        param.setIsShowLoading(false);
        view.setEnabled(false);
        new HttpUtil().HttpRequest(context, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                view.setEnabled(true);
                try {
                    JSONObject jsonObject = new JSONObject(httpresult.getResult());
                    if (isLove) {
                        question.setLoved_id(jsonObject.getLong("id"));
                        question.setLove(question.getLove() - 1);
                        question.setIs_loved(0);
                    } else {
                        question.setTreaded_id(jsonObject.getLong("id"));
                        question.setTread(question.getTread() - 1);
                        question.setIs_treaded(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (callback != null) {
                    callback.onSuccess();
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                view.setEnabled(true);
            }
        });
    }

    public static void toLove(Context context, final View view, final Question question, final boolean isLove, final LoveCallback callback) {
        Param param = new Param(Constant.PRAISE);
        param.addToken();
        param.addRequstParams("oid", question.getId());
        param.addRequstParams("category", 2);
        if (isLove) {
            param.addRequstParams("operate", 3);
        } else {
            param.addRequstParams("operate", 4);
        }
        param.setIsShowLoading(false);
        view.setEnabled(false);
        new HttpUtil().HttpRequest(context, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                view.setEnabled(true);
                try {
                    JSONObject jsonObject = new JSONObject(httpresult.getResult());
                    if (isLove) {
                        question.setLoved_id(jsonObject.getLong("id"));
                        question.setLove(question.getLove() + 1);
                        question.setIs_loved(1);
                    } else {
                        question.setTreaded_id(jsonObject.getLong("id"));
                        question.setTread(question.getTread() + 1);
                        question.setIs_treaded(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (callback != null) {
                    callback.onSuccess();
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                view.setEnabled(true);
            }
        });
    }

    public interface LoveCallback {
        void onSuccess();
    }
}
