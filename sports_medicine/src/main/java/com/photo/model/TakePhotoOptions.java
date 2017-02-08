package com.photo.model;

import java.io.Serializable;

/**
 * Author: crazycodeboy
 * Date: 2016/11/5 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.devio.org/
 * GitHub:https://github.com/crazycodeboy
 * Eamil:crazycodeboy@gmail.com
 */
public class TakePhotoOptions implements Serializable {
    /**
     * 是对拍的照片进行旋转角度纠正
     */
    private boolean correctImage;

    private TakePhotoOptions() {
    }

    public boolean isCorrectImage() {
        return correctImage;
    }

    public void setCorrectImage(boolean correctImage) {
        this.correctImage = correctImage;
    }

    public static class Builder {
        private TakePhotoOptions options;

        public Builder() {
            this.options = new TakePhotoOptions();
        }

        public Builder setCorrectImage(boolean isCorrectImage) {
            options.setCorrectImage(isCorrectImage);
            return this;
        }
        public TakePhotoOptions create(){
            return options;
        }
    }
}
