package com.tianxiabuyi.sports_medicine.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Question extends BaseLove implements Parcelable {
    private long id;
    private String content;
    private String user_name;
    private String title;
    private long browse;// 浏览次数
    private String create_time;
    private long aid;
    private long comment;
    private String avatar;
    private long attention;
    private ArrayList<String> imgs;
    private String group_name;
    private int group;

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public long getAttention() {
        return attention;
    }

    public void setAttention(long attention) {
        this.attention = attention;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getBrowse() {
        return browse;
    }

    public void setBrowse(long browse) {
        this.browse = browse;
    }

    public long getComment() {
        return comment;
    }

    public void setComment(long comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.content);
        dest.writeString(this.user_name);
        dest.writeString(this.title);
        dest.writeLong(this.browse);
        dest.writeString(this.create_time);
        dest.writeLong(this.aid);
        dest.writeLong(this.comment);
        dest.writeString(this.avatar);
        dest.writeLong(this.attention);
        dest.writeStringList(this.imgs);
        dest.writeString(this.group_name);
        dest.writeInt(this.group);
    }

    public Question() {
    }

    protected Question(Parcel in) {
        this.id = in.readLong();
        this.content = in.readString();
        this.user_name = in.readString();
        this.title = in.readString();
        this.browse = in.readLong();
        this.create_time = in.readString();
        this.aid = in.readLong();
        this.comment = in.readLong();
        this.avatar = in.readString();
        this.attention = in.readLong();
        this.imgs = in.createStringArrayList();
        this.group_name = in.readString();
        this.group = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
