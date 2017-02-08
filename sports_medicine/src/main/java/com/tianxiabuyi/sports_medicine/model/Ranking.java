package com.tianxiabuyi.sports_medicine.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2016/11/28.
 */

public class Ranking implements MultiItemEntity {

    public static final int ITEM = 0;
    public static final int ITEM_MY = 1;
    /**
     * uid : 54871
     * user_name : lixm
     * avatar : http://image.eeesys.com/small/2016/20161128/20161128150913.png
     * step : 30
     */

    private String uid;
    private String user_name;
    private String avatar;
    private int step;// 日排行榜步数
    private int totstep;// 月排行榜步数

    private int itemType;
    private int ranking;

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getTotstep() {
        return totstep;
    }

    public void setTotstep(int totstep) {
        this.totstep = totstep;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
