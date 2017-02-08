package com.tianxiabuyi.sports_medicine.common.net;

import android.content.Context;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpBean;
import com.eeesys.frame.utils.IntentTool;
import com.eeesys.frame.utils.ToastTool;
import com.tencent.android.tpush.XGPushManager;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.MyApp;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.util.ThirdAccountDbUtil;
import com.tianxiabuyi.sports_medicine.main.activity.MainActivity;
import com.tianxiabuyi.sports_medicine.personal.LogoutEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by madc on 2016/4/14.
 */
public class HttpUtil extends CommHttp {

    public HttpUtil() {
    }

    @Override
    public boolean tokenOut() {
        Long saveTime = UserSpUtil.getTime(MyApp.getInstance());
        return System.currentTimeMillis() - saveTime > 1000 * 60 * 60;
    }

    @Override
    public void upTokenState(Context context, HttpBean bean, String s) {
        // do.. 存储获得的新token和时间
        // （重要） 改变HttpBean里的token
        UserSpUtil.setToken(context, s);
        UserSpUtil.setTime(context);
        bean.addRequstParams(Constant.TOKEN, s);
    }

    @Override
    public void restartLogin(Context context) {
        ToastTool.show(context,"您的登录信息已过期，请重新登录");
        Context ctx = context.getApplicationContext();
        XGPushManager.unregisterPush(ctx);
        ThirdAccountDbUtil.getInstance().clear();
        UserSpUtil.removeUserInfo(context);
        context.startActivity(IntentTool.getIntent(context, MainActivity.class, null));
        EventBus.getDefault().post(new LogoutEvent());
    }
}
