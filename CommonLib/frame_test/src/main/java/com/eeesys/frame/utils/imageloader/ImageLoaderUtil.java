package com.eeesys.frame.utils.imageloader;

import android.content.Context;

/**
 * Created by Administrator on 2016/5/30.
 * 统一图片加载入口
 */
public class ImageLoaderUtil {
    public static final int PIC_LARGE = 0;
    public static final int PIC_MEDIUM = 1;
    public static final int PIC_SMALL = 2;

    public static final int LOAD_STRATEGY_NORMAL = 0;
    public static final int LOAD_STRATEGY_ONLY_WIFI = 1;

    private static ImageLoaderUtil mInstance;
    private BaseImageLoaderProvider mProvider;

    private ImageLoaderUtil() {
        mProvider = new UniversalImageLoaderProvider();
    }

    //single instance
    public static ImageLoaderUtil getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoaderUtil.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderUtil();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    public void loadImage(Context context, CommonImageLoader img) {
        mProvider.loadImage(context, img);
    }

    public BaseImageLoaderProvider getmProvider() {
        return mProvider;
    }

    public void setmProvider(BaseImageLoaderProvider mProvider) {
        this.mProvider = mProvider;
    }
}
