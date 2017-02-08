package com.tianxiabuyi.sports_medicine.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsImage implements Parcelable {
    private String ref;

    private String src;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ref);
        dest.writeString(this.src);
    }

    public NewsImage() {
    }

    protected NewsImage(Parcel in) {
        this.ref = in.readString();
        this.src = in.readString();
    }

    public static final Creator<NewsImage> CREATOR = new Creator<NewsImage>() {
        public NewsImage createFromParcel(Parcel source) {
            return new NewsImage(source);
        }

        public NewsImage[] newArray(int size) {
            return new NewsImage[size];
        }
    };
}
