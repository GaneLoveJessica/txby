package com.tianxiabuyi.sports_medicine.question.event;

/**
 * Created by Administrator on 2016/9/30.
 */

public class MsgEvent {
    private final String id;

    public MsgEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
