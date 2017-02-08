package com.tianxiabuyi.sports_medicine.question.event;

/**
 * 发表问题
 */

public class PublishCommunityEvent {
    private final int selectedCategory;
    private final String name;

    public PublishCommunityEvent(int selectedCategory, String name) {
        this.selectedCategory = selectedCategory;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getSelectedCategory() {
        return selectedCategory;
    }
}
