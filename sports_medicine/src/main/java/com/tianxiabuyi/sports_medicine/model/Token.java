package com.tianxiabuyi.sports_medicine.model;

/**
 * Created by Administrator on 2016/8/8.
 */
public class Token {
    /**
     * token : 4229a15db5b5c9f6b68145af3e7f81b0
     * expires_in : 3600
     */

    private String token;
    private int expires_in;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
