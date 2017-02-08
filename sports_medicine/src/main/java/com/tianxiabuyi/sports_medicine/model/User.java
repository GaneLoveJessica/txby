package com.tianxiabuyi.sports_medicine.model;

/**
 * Created by Administrator on 2016/8/8.
 */
public class User {

    /**
     * uid : 9073
     * birthday :
     * phone : 18501546991
     * medical_number :
     * card_number :
     * self : 0
     * avatar : http://image.eeesys.com/default/user_m.png
     * city : 510
     * user_name : qwer
     * card_type :
     * json : {}
     * age :
     * gender :
     * medical_type :
     * patient_id :
     */

    private int uid;
    private String birthday;
    private String phone;
    private String medical_number;
    private String card_number;
    private String self;
    private String avatar;
    private String city;
    private String user_name;
    private String card_type;
    private String age;
    private String gender;
    private String medical_type;
    private String patient_id;
    private String name;

    /**
     * type : 100
     */

    private int type;
    /**
     * mail :
     * hospital_name :
     * state :
     * certification : http://image.eeesys.com/small/2016/20160922/244bd804_11d7_4ec5_8dc4_3bc0a5ded577.jpg
     * title : 医生
     * skill :
     * dept :
     * specialty :
     */

    private String mail;
    private String hospital_name;
    private String state;
    private String certification;
    private String title;
    private String skill;
    private String dept;
    private String specialty;
    /**
     * introduce : 看看
     * major : 北京
     */

    private String introduce;
    private String major;
    /**
     * company : 苏州市立医院
     */

    private String company;
    /**
     * my_title :
     */

    private String my_title;
    /**
     * json : {"user":{"birthday":"2016年12月13日","sex":"女","weight":"","name":"吖吖","waist":"","high":"","blood":"A+"}}
     */

    private JsonBean json;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMedical_number() {
        return medical_number;
    }

    public void setMedical_number(String medical_number) {
        this.medical_number = medical_number;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMedical_type() {
        return medical_type;
    }

    public void setMedical_type(String medical_type) {
        this.medical_type = medical_type;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMy_title() {
        return my_title;
    }

    public void setMy_title(String my_title) {
        this.my_title = my_title;
    }

    public JsonBean getJson() {
        return json;
    }

    public void setJson(JsonBean json) {
        this.json = json;
    }

    public static class JsonBean {
        /**
         * user : {"birthday":"2016年12月13日","sex":"女","weight":"","name":"吖吖","waist":"","high":"","blood":"A+"}
         */

        private UserBean user;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * birthday : 2016年12月13日
             * sex : 女
             * weight :
             * name : 吖吖
             * waist :
             * high :
             * blood : A+
             */

            private String weight;
            private String name;
            private String waist;
            private String high;
            private String blood;

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getWaist() {
                return waist;
            }

            public void setWaist(String waist) {
                this.waist = waist;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getBlood() {
                return blood;
            }

            public void setBlood(String blood) {
                this.blood = blood;
            }
        }
    }
}
