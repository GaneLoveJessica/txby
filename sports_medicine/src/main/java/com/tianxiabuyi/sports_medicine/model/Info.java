package com.tianxiabuyi.sports_medicine.model;

/**
 * Created by Administrator on 2016/8/2.
 */
public class Info {
    private String title;
    private String content;
    private boolean canModify;
    private String category;
    private int imgId;

    public Info(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Info(String title, String content, boolean canModify) {
        this.title = title;
        this.content = content;
        this.canModify = canModify;
    }

    public Info(String title, int imgId) {
        this.title = title;
        this.imgId = imgId;
    }

    public Info(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public Info(String title, String content, String category, boolean canModify) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.canModify = canModify;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCanModify() {
        return canModify;
    }

    public void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
