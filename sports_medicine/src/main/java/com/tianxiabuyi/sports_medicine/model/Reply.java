package com.tianxiabuyi.sports_medicine.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 问答回复实体类
 */
public class Reply extends BaseLove implements Parcelable {
    private int id;
    private String content;
    private String user_name;
    private int browse;
    private String create_time;
    private int comment;
    private int type;
    private String avatar;
    private int attention;
    private List<String> imgs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getBrowse() {
        return browse;
    }

    public void setBrowse(int browse) {
        this.browse = browse;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public Reply() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
        dest.writeString(this.content);
        dest.writeString(this.user_name);
        dest.writeInt(this.browse);
        dest.writeString(this.create_time);
        dest.writeInt(this.comment);
        dest.writeInt(this.type);
        dest.writeString(this.avatar);
        dest.writeInt(this.attention);
        dest.writeStringList(this.imgs);
    }

    protected Reply(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.content = in.readString();
        this.user_name = in.readString();
        this.browse = in.readInt();
        this.create_time = in.readString();
        this.comment = in.readInt();
        this.type = in.readInt();
        this.avatar = in.readString();
        this.attention = in.readInt();
        this.imgs = in.createStringArrayList();
    }

    public static final Creator<Reply> CREATOR = new Creator<Reply>() {
        @Override
        public Reply createFromParcel(Parcel source) {
            return new Reply(source);
        }

        @Override
        public Reply[] newArray(int size) {
            return new Reply[size];
        }
    };
}
