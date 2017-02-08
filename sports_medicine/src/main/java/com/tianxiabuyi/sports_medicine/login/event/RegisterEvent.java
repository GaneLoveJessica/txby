package com.tianxiabuyi.sports_medicine.login.event;

/**
 * Created by Administrator on 2016/11/3.
 */

public class RegisterEvent {
    private final String platName;
    private final String unionId;

    public RegisterEvent(String platName, String unionId) {
        this.platName = platName;
        this.unionId = unionId;
    }

    public String getPlatName() {
        return platName;
    }

    public String getUnionId() {
        return unionId;
    }
}
