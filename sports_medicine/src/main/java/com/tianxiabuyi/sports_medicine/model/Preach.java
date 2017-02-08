package com.tianxiabuyi.sports_medicine.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class Preach extends BaseLove implements Parcelable {
    private int news_id;
    private String title;
    private String time;
    private String summary;
    private List<NewsImage> img;
    private String content;
    private String author;
    /**
     * love : 0
     * id : 28866
     * browse : 1
     * json : {"attachs":[]}
     * is_loved : 0
     * attention : 0
     */

    private int id;
    private int browse;
    private int attention;
    /**
     * id : XMTY4NTYzNzg5Ng\u003d\u003d
     * link : http://v.youku.com/v_show/id_XMTY4NTYzNzg5Ng\u003d\u003d.html
     * thumb : http://r3.ykimg.com/0542040857B17B346A0A4E04578D1098
     */

    private JsonBean json;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<NewsImage> getImg() {
        return img;
    }

    public void setImg(List<NewsImage> img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrowse() {
        return browse;
    }

    public void setBrowse(int browse) {
        this.browse = browse;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    @Override
    public String toString() {
        return title;
    }


    public Preach() {
    }

    public JsonBean getJson() {
        return json;
    }

    public void setJson(JsonBean json) {
        this.json = json;
    }

    public static class JsonBean implements Parcelable {
        private String id;
        private String link;
        private String thumb;
        private String news_thumbnail;

        public String getNews_thumbnail() {
            return news_thumbnail;
        }

        public void setNews_thumbnail(String news_thumbnail) {
            this.news_thumbnail = news_thumbnail;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public JsonBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.link);
            dest.writeString(this.thumb);
            dest.writeString(this.news_thumbnail);
        }

        protected JsonBean(Parcel in) {
            this.id = in.readString();
            this.link = in.readString();
            this.thumb = in.readString();
            this.news_thumbnail = in.readString();
        }

        public static final Creator<JsonBean> CREATOR = new Creator<JsonBean>() {
            @Override
            public JsonBean createFromParcel(Parcel source) {
                return new JsonBean(source);
            }

            @Override
            public JsonBean[] newArray(int size) {
                return new JsonBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.news_id);
        dest.writeString(this.title);
        dest.writeString(this.time);
        dest.writeString(this.summary);
        dest.writeTypedList(this.img);
        dest.writeString(this.content);
        dest.writeString(this.author);
        dest.writeInt(this.id);
        dest.writeInt(this.browse);
        dest.writeInt(this.attention);
        dest.writeParcelable(this.json, flags);
    }

    protected Preach(Parcel in) {
        super(in);
        this.news_id = in.readInt();
        this.title = in.readString();
        this.time = in.readString();
        this.summary = in.readString();
        this.img = in.createTypedArrayList(NewsImage.CREATOR);
        this.content = in.readString();
        this.author = in.readString();
        this.id = in.readInt();
        this.browse = in.readInt();
        this.attention = in.readInt();
        this.json = in.readParcelable(JsonBean.class.getClassLoader());
    }

    public static final Creator<Preach> CREATOR = new Creator<Preach>() {
        @Override
        public Preach createFromParcel(Parcel source) {
            return new Preach(source);
        }

        @Override
        public Preach[] newArray(int size) {
            return new Preach[size];
        }
    };
}