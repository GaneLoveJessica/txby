package com.tianxiabuyi.sports_medicine.question.event;

import com.tianxiabuyi.sports_medicine.model.Question;

/**
 * 点赞和踩事件
 */

public class LoveOrTreadEvent {
    private final boolean action;
    private final Question question;

    public LoveOrTreadEvent(boolean action, Question question) {
        this.action = action;
        this.question = question;
    }

    public boolean isAction() {
        return action;
    }

    public Question getQuestion() {
        return question;
    }
}
