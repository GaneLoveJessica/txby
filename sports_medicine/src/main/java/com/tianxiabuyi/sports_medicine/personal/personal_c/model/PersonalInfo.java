package com.tianxiabuyi.sports_medicine.personal.personal_c.model;

/**
 * Created by Administrator on 2016/10/11.
 */
public class PersonalInfo {
    private String name;
    private String birthday;
    private String gender;
    private String bloodType;

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
