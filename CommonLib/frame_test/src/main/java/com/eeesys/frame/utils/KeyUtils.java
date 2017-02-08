package com.eeesys.frame.utils;

import android.text.TextUtils;

import com.eeesys.frame.BuildConfig;

/**
 * Created by xjh1994 on 2016/7/27.
 */
public class KeyUtils {
    public static String getKey() {
        String key = TextUtils.concat(BuildConfig.appKey,
                EncryptUtils.something(),
                EncryptUtils.getKey(),
                BuildConfig.appKey,
                EncryptUtils.getString()).toString();
        return key;
    }
}
