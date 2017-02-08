package com.tianxiabuyi.sports_medicine.common.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tianxiabuyi.sports_medicine.R;

/**
 * Created by Administrator on 2016/9/27.
 */

public class GlideUtil {
    public static void setCircleAvatar(Context context, ImageView ivAvatar, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .bitmapTransform(new GlideCircleTransform(context))
                .placeholder(R.mipmap.avatar)
                .error(R.mipmap.avatar)
                .into(ivAvatar);
    }

    public static void setAvatar(Context context, ImageView ivAvatar, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .dontAnimate()
                .placeholder(R.mipmap.avatar)
                .error(R.mipmap.avatar)
                .into(ivAvatar);
    }

    public static void setImage(Context context, ImageView imageView, String imgUrl){
        Glide.with(context)
                .load(imgUrl)
                .dontAnimate()
                .placeholder(R.mipmap.loading)
                .error(R.mipmap.loading)
                .into(imageView);
    }
}
