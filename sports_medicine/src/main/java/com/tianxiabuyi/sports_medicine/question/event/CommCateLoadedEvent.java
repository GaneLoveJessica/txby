package com.tianxiabuyi.sports_medicine.question.event;

import com.tianxiabuyi.sports_medicine.model.Category;

import java.util.List;

/**
 * Created by Administrator on 2017/1/10.
 */

public class CommCateLoadedEvent {
    private final List<Category> categories;

    public CommCateLoadedEvent(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
