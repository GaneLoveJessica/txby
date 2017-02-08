package com.tianxiabuyi.sports_medicine.login.event;

/**
 * Created by Administrator on 2016/9/30.
 */

public class LoginEvent {
    private final boolean isLogin;

    public LoginEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
