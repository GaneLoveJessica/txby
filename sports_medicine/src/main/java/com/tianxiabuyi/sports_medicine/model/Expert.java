package com.tianxiabuyi.sports_medicine.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/22.
 */

public class Expert extends BaseLove implements Parcelable {
    private int id;
    private String user_name;
    private String avatar;
    private String name;
    private String title;
    private int browse;
    private int attention;
    private JsonBean json;
    private int score;
    private int answer;
    private int count;

    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonBean getJson() {
        return json;
    }

    public void setJson(JsonBean json) {
        this.json = json;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }


    public static class JsonBean implements Parcelable {
        private String sports_medical;
        private String company_address_zip;
        private String certifiction_name_no;
        private String company;
        private String graduate_school;
        private String my_title;
        private String education;
        private String graduate_time;
        private String sports_other;
        private String education_medical;
        private String major;
        /**
         * work_time : 16
         * introduce : 男、高级教练员\n1990年至2001年中国人民解放军八一体育工作大队田径队运动员\n2001年至今苏州市专业运动队管理中心、苏州市体育运动学校中长跑教练员\n执教生涯：带队队员多次取得全国冠军\n周春秀2008年北京奥运会女子马拉松铜牌\n原国家现代五项队教练员\n陈倩获得2009年世界现代五项锦标赛冠军
         */

        private String work_time;
        private String introduce;

        public String getSports_medical() {
            return sports_medical;
        }

        public void setSports_medical(String sports_medical) {
            this.sports_medical = sports_medical;
        }

        public String getCompany_address_zip() {
            return company_address_zip;
        }

        public void setCompany_address_zip(String company_address_zip) {
            this.company_address_zip = company_address_zip;
        }

        public String getCertifiction_name_no() {
            return certifiction_name_no;
        }

        public void setCertifiction_name_no(String certifiction_name_no) {
            this.certifiction_name_no = certifiction_name_no;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getGraduate_school() {
            return graduate_school;
        }

        public void setGraduate_school(String graduate_school) {
            this.graduate_school = graduate_school;
        }

        public String getMy_title() {
            return my_title;
        }

        public void setMy_title(String my_title) {
            this.my_title = my_title;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getGraduate_time() {
            return graduate_time;
        }

        public void setGraduate_time(String graduate_time) {
            this.graduate_time = graduate_time;
        }

        public String getSports_other() {
            return sports_other;
        }

        public void setSports_other(String sports_other) {
            this.sports_other = sports_other;
        }

        public String getEducation_medical() {
            return education_medical;
        }

        public void setEducation_medical(String education_medical) {
            this.education_medical = education_medical;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public JsonBean() {
        }

        public String getWork_time() {
            return work_time;
        }

        public void setWork_time(String work_time) {
            this.work_time = work_time;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.sports_medical);
            dest.writeString(this.company_address_zip);
            dest.writeString(this.certifiction_name_no);
            dest.writeString(this.company);
            dest.writeString(this.graduate_school);
            dest.writeString(this.my_title);
            dest.writeString(this.education);
            dest.writeString(this.graduate_time);
            dest.writeString(this.sports_other);
            dest.writeString(this.education_medical);
            dest.writeString(this.major);
            dest.writeString(this.work_time);
            dest.writeString(this.introduce);
        }

        protected JsonBean(Parcel in) {
            this.sports_medical = in.readString();
            this.company_address_zip = in.readString();
            this.certifiction_name_no = in.readString();
            this.company = in.readString();
            this.graduate_school = in.readString();
            this.my_title = in.readString();
            this.education = in.readString();
            this.graduate_time = in.readString();
            this.sports_other = in.readString();
            this.education_medical = in.readString();
            this.major = in.readString();
            this.work_time = in.readString();
            this.introduce = in.readString();
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


    public Expert() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
        dest.writeString(this.user_name);
        dest.writeString(this.avatar);
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeInt(this.browse);
        dest.writeInt(this.attention);
        dest.writeParcelable(this.json, flags);
        dest.writeInt(this.score);
        dest.writeInt(this.answer);
        dest.writeInt(this.count);
        dest.writeByte(this.flag ? (byte) 1 : (byte) 0);
    }

    protected Expert(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.user_name = in.readString();
        this.avatar = in.readString();
        this.name = in.readString();
        this.title = in.readString();
        this.browse = in.readInt();
        this.attention = in.readInt();
        this.json = in.readParcelable(JsonBean.class.getClassLoader());
        this.score = in.readInt();
        this.answer = in.readInt();
        this.count = in.readInt();
        this.flag = in.readByte() != 0;
    }

    public static final Creator<Expert> CREATOR = new Creator<Expert>() {
        @Override
        public Expert createFromParcel(Parcel source) {
            return new Expert(source);
        }

        @Override
        public Expert[] newArray(int size) {
            return new Expert[size];
        }
    };
}
