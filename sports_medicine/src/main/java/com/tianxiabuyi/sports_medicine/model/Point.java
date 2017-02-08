package com.tianxiabuyi.sports_medicine.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */

public class Point {

    /**
     * data : [{"operator_name":"Chen","uid":46681,"id":7462,"category":4,"activity_id":132,"week_day":"周三","create_time":"2016-09-28 15:53:42","score":-5,"type":4,"hospital":1068,"operator":"46681"},{"operator_name":"Chen","uid":46681,"id":7461,"category":4,"activity_id":132,"week_day":"周三","create_time":"2016-09-28 15:53:40","score":-5,"type":4,"hospital":1068,"operator":"46681"},{"operator_name":"Chen","uid":46681,"id":7460,"category":4,"activity_id":132,"week_day":"周三","create_time":"2016-09-28 15:53:38","score":-5,"type":4,"hospital":1068,"operator":"46681"},{"operator_name":"Chen","uid":46681,"id":7459,"category":4,"activity_id":132,"week_day":"周三","create_time":"2016-09-28 15:53:33","score":-5,"type":4,"hospital":1068,"operator":"46681"},{"operator_name":"Chen","uid":46681,"id":7458,"category":4,"activity_id":132,"week_day":"周三","create_time":"2016-09-28 15:52:27","score":5,"type":3,"hospital":1068,"operator":"46681"},{"operator_name":"Chen","uid":46681,"id":7457,"category":4,"activity_id":132,"week_day":"周三","create_time":"2016-09-28 15:38:54","score":-5,"type":4,"hospital":1068,"operator":"46681"},{"operator_name":"yhx666","uid":46681,"id":7379,"operator_avatar":"http://image.eeesys.com/small/2016/20160927/20160927123801.png","category":4,"activity_id":132,"week_day":"周三","create_time":"2016-09-28 13:16:05","score":5,"type":3,"hospital":1068,"operator":"46802"},{"operator_name":"Chen","uid":46681,"id":7377,"category":4,"activity_id":132,"week_day":"周三","create_time":"2016-09-28 13:13:30","score":5,"type":3,"hospital":1068,"operator":"46681"},{"operator_name":"系统","uid":46681,"id":7349,"activity_id":0,"week_day":"周二","create_time":"2016-09-27 09:33:45","score":100,"hospital":1068,"operator":"系统"}]
     * month : 2016-09
     */

    private String month;
    /**
     * operator_name : Chen
     * uid : 46681
     * id : 7462
     * category : 4
     * activity_id : 132
     * week_day : 周三
     * create_time : 2016-09-28 15:53:42
     * score : -5
     * type : 4
     * hospital : 1068
     * operator : 46681
     */

    private List<DataBean> data;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean{
        private String operator_name;
        private int uid;
        private int id;
        private int category;
        private int activity_id;
        private String week_day;
        private String create_time;
        private int score;
        private int type;
        private int hospital;
        private String operator;
        private String month;
        /**
         * operator_avatar : http://image.eeesys.com/small/2016/20160928/20160928184315.png
         */

        private String operator_avatar;
        /**
         * json : {}
         */

        private JsonBean json;

        public String getOperator_name() {
            return operator_name;
        }

        public void setOperator_name(String operator_name) {
            this.operator_name = operator_name;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public int getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(int activity_id) {
            this.activity_id = activity_id;
        }

        public String getWeek_day() {
            return week_day;
        }

        public void setWeek_day(String week_day) {
            this.week_day = week_day;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getHospital() {
            return hospital;
        }

        public void setHospital(int hospital) {
            this.hospital = hospital;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getMonth() {
            return month;
        }

        public String getOperator_avatar() {
            return operator_avatar;
        }

        public void setOperator_avatar(String operator_avatar) {
            this.operator_avatar = operator_avatar;
        }

        public JsonBean getJson() {
            return json;
        }

        public void setJson(JsonBean json) {
            this.json = json;
        }

        public static class JsonBean {
            private String types;

            public String getTypes() {
                return types;
            }

            public void setTypes(String types) {
                this.types = types;
            }
        }
    }



}
