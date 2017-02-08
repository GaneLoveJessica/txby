package com.eeesys.frame.utils.imageloader;

import android.content.Context;

import com.eeesys.frame.R;
import com.eeesys.frame.utils.ImageLoaderTool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


/**
 * Created by Administrator on 2016/5/30.
 */
public class UniversalImageLoaderProvider extends BaseImageLoaderProvider {

    @Override
    public void loadImage(Context ctx, CommonImageLoader img) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(img.getPlaceHolder() == 0 ? R.drawable.default_loading : img.getPlaceHolder())
                .showImageForEmptyUri(img.getPlaceHolder() == 0 ? R.drawable.default_loading : img.getPlaceHolder())
                .showImageOnFail(img.getPlaceHolder() == 0 ? R.drawable.default_loading : img.getPlaceHolder())
                .cacheInMemory(true).cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(0)).build();
        ImageLoader.getInstance().displayImage(img.getUrl(), img.getImgView(),
                options,
                ImageLoaderTool.getAnimateFirstDisplayListener());
    }
}
