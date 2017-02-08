package com.tianxiabuyi.sports_medicine.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HomeMenu implements Parcelable {


    /**
     * id : 3
     * parent : -1
     * sub : [{"id":4,"parent":3,"sub":[],"json":{},"name":"室内"},{"id":5,"parent":3,"sub":[],"json":{},"name":"室外"},{"id":6,"parent":3,"sub":[],"json":{},"name":"视频"}]
     * json : {"icon":"http://file.eeesys.com/attach/1474600212protect.png"}
     * name : 防护
     */

    private int id;
    private int parent;
    private String icon;
    /**
     * icon : http://file.eeesys.com/attach/1474600212protect.png
     */

    private JsonBean json;
    private String name;


    public HomeMenu(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * id : 4
     * parent : 3
     * sub : []
     * json : {}
     * name : 室内
     */

    private List<SubBean> sub;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HomeMenu homeMenu = (HomeMenu) o;

        return name != null ? name.equals(homeMenu.name) : homeMenu.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public JsonBean getJson() {
        return json;
    }

    public void setJson(JsonBean json) {
        this.json = json;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubBean> getSub() {
        return sub;
    }

    public void setSub(List<SubBean> sub) {
        this.sub = sub;
    }

    public static class JsonBean implements Parcelable {
        private String icon;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.icon);
        }

        public JsonBean() {
        }

        protected JsonBean(Parcel in) {
            this.icon = in.readString();
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

    public static class SubBean implements Parcelable {
        private int id;
        private int parent;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParent() {
            return parent;
        }

        public void setParent(int parent) {
            this.parent = parent;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.parent);
            dest.writeString(this.name);
        }

        public SubBean() {
        }

        protected SubBean(Parcel in) {
            this.id = in.readInt();
            this.parent = in.readInt();
            this.name = in.readString();
        }

        public static final Creator<SubBean> CREATOR = new Creator<SubBean>() {
            @Override
            public SubBean createFromParcel(Parcel source) {
                return new SubBean(source);
            }

            @Override
            public SubBean[] newArray(int size) {
                return new SubBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.parent);
        dest.writeString(this.icon);
        dest.writeParcelable(this.json, flags);
        dest.writeString(this.name);
        dest.writeTypedList(this.sub);
    }

    protected HomeMenu(Parcel in) {
        this.id = in.readInt();
        this.parent = in.readInt();
        this.icon = in.readString();
        this.json = in.readParcelable(JsonBean.class.getClassLoader());
        this.name = in.readString();
        this.sub = in.createTypedArrayList(SubBean.CREATOR);
    }

    public static final Creator<HomeMenu> CREATOR = new Creator<HomeMenu>() {
        @Override
        public HomeMenu createFromParcel(Parcel source) {
            return new HomeMenu(source);
        }

        @Override
        public HomeMenu[] newArray(int size) {
            return new HomeMenu[size];
        }
    };
}
