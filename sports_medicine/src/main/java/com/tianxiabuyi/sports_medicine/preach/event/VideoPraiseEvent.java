package com.tianxiabuyi.sports_medicine.preach.event;

import com.tianxiabuyi.sports_medicine.model.Preach;

/**
 * Created by Administrator on 2016/12/12.
 */

public class VideoPraiseEvent {
    private final Preach preach;

    public VideoPraiseEvent(Preach preach) {
        this.preach = preach;
    }

    public Preach getPreach() {
        return preach;
    }
}
