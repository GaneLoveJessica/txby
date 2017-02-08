package com.tianxiabuyi.sports_medicine.model;

/**
 * Created by Administrator on 2016/11/21.
 */

public class Step {
    private String time;
    private String step;
    private String step_time;// 月步数

    public Step(String time, String step) {
        this.time = time;
        this.step = step;
    }

    public String getStep_time() {
        return step_time;
    }

    public void setStep_time(String step_time) {
        this.step_time = step_time;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "{" +
                "step='" + step + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
