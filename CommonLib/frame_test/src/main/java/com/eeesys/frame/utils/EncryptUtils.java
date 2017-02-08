package com.eeesys.frame.utils;

/**
 * Created by xjh1994 on 2016/7/26.
 */
public class EncryptUtils {

    static {
        System.loadLibrary("EncryptUtils");
    }

    public static native String something();

    public static String getKey() {
        return "&&";
    }

    public static String getString() {
        String string = "么久乁乌乔么";
        char[] array = string.toCharArray();
        for (int i = 0; i < array.length; i++) {
            array[i] = (char) (array[i] ^ 20000);
        }
        return new String(array);
    }
}
