package com.tianxiabuyi.sports_medicine.model;

public class AppInfo {

    /**
     * desc : new version . it is good
     * app_type : hospital
     * app_name : 建湖院内版
     * url : http://192.168.2.121/upload/hospital/1034/jhyy_hospital_1.1.apk
     * hospital : 1034
     * version_code : 1
     * version : 建湖院内版
     */

    private String desc;
    private String app_type;
    private String app_name;
    private String url;
    private int hospital;
    private int version_code;
    private String version;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getApp_type() {
        return app_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHospital() {
        return hospital;
    }

    public void setHospital(int hospital) {
        this.hospital = hospital;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
