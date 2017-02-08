package com.tianxiabuyi.sports_medicine.model;

/**
 * Created by Administrator on 2016/8/24.
 */
public class Praise {

    /**
     * avatar : http://image.eeesys.com/jhhosp/10001.jpg
     * user_name : 18501546991
     */
    private String avatar;
    private String user_name;

    public Praise(String avatar,String user_name) {
        this.avatar = avatar;
        this.user_name = user_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
