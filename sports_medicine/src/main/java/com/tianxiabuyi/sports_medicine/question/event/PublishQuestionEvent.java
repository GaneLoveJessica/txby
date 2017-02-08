package com.tianxiabuyi.sports_medicine.question.event;

/**
 * 发表问题
 */

public class PublishQuestionEvent {
    private final int selectedCategory;
    private final String name;

    public PublishQuestionEvent(int selectedCategory, String name) {
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
