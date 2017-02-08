package com.tianxiabuyi.sports_medicine.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/26.
 */

public class BaseLove implements Parcelable {
    private int love;
    private int is_loved;
    private long loved_id;
    private long tread;
    private int is_treaded;
    private long treaded_id;

    public int getIs_loved() {
        return is_loved;
    }

    public void setIs_loved(int is_loved) {
        this.is_loved = is_loved;
    }

    public int getIs_treaded() {
        return is_treaded;
    }

    public void setIs_treaded(int is_treaded) {
        this.is_treaded = is_treaded;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public long getLoved_id() {
        return loved_id;
    }

    public void setLoved_id(long loved_id) {
        this.loved_id = loved_id;
    }

    public long getTread() {
        return tread;
    }

    public void setTread(long tread) {
        this.tread = tread;
    }

    public long getTreaded_id() {
        return treaded_id;
    }

    public void setTreaded_id(long treaded_id) {
        this.treaded_id = treaded_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.love);
        dest.writeInt(this.is_loved);
        dest.writeLong(this.loved_id);
        dest.writeLong(this.tread);
        dest.writeInt(this.is_treaded);
        dest.writeLong(this.treaded_id);
    }

    public BaseLove() {
    }

    protected BaseLove(Parcel in) {
        this.love = in.readInt();
        this.is_loved = in.readInt();
        this.loved_id = in.readLong();
        this.tread = in.readLong();
        this.is_treaded = in.readInt();
        this.treaded_id = in.readLong();
    }

}
