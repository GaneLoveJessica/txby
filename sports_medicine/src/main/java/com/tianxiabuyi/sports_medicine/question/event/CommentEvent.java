package com.tianxiabuyi.sports_medicine.question.event;

import com.tianxiabuyi.sports_medicine.model.Reply;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */

public class CommentEvent {
    private final List<Reply> replies;

    public CommentEvent(List<Reply> replies) {
        this.replies = replies;
    }

    public List<Reply> getReplies() {
        return replies;
    }
}
