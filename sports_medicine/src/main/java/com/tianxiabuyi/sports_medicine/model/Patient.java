package com.tianxiabuyi.sports_medicine.model;

/**
 * Created by Administrator on 2016/9/18.
 */
public class Patient {

    /**
     * id : 46681
     * user_name : chen
     */

    private int id;
    private String user_name;
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * count : 1
     */

    private int count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
