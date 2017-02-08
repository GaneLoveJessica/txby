package com.tianxiabuyi.sports_medicine;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eeesys.frame.app.CusApp;
import com.ninegrid.NineGridView;
import com.orhanobut.logger.Logger;
import com.youku.player.YoukuPlayerConfig;

/**
 * Created by Administrator on 2016/8/1.
 */
public class MyApp extends CusApp {
    private static MyApp instance;
    public static final String CLIENT_ID_WITH_AD = "8711ca42bf58b526";
    public static final String CLIENT_SECRET_WITH_AD = "a0aa6c06756a907aa68693459b4fe721";

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initYouKu();
        NineGridView.setImageLoader(new GlideImageLoader());

        Logger.init(getResources().getString(R.string.app_name))                 // default PRETTYLOGGER or use just init()
                .hideThreadInfo(); //default AndroidLogAdapter
    }

    private void initYouKu() {
        YoukuPlayerConfig.setLog(false);
        //设置client_id和client_secret
        YoukuPlayerConfig.setClientIdAndSecret(CLIENT_ID_WITH_AD,CLIENT_SECRET_WITH_AD);
        //sdk初始化
        YoukuPlayerConfig.onInitial(getApplicationContext(), "");
    }

    /** Glide 加载 */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.loading)
                    .error(R.mipmap.loading)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}
