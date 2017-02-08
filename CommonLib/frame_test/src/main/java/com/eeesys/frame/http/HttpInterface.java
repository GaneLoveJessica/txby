package com.eeesys.frame.http;

import android.content.Context;

/**
 * Created by ASUS on 2016/4/13.
 */
public interface HttpInterface {

    interface MyHttp {
        /**
         * 本地判断token是否过期
         */
        boolean tokenOut();
        /**
         * 更新token
         * 更新超时时间
         *  更新httpBean的token的值
         */
        void upTokenState(Context context, HttpBean bean, String s);

        /**
         * 重新登录
         */
        void restartLogin(Context context);
    }

    interface HttpToken {
        /**
         * 设置刷新token的默认参数和url
         */
        void setRefTokenParam();
    }
}
