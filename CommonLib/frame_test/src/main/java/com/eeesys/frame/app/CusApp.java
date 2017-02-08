package com.eeesys.frame.app;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;

import com.eeesys.frame.utils.ImageLoaderTool;
import com.eeesys.frame.utils.ImageTool;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.x;

import java.util.LinkedList;
import java.util.List;


public class CusApp extends MultiDexApplication {

    private List<Activity> mActivities = new LinkedList<Activity>();

    public List<Activity> getActivities() {
        return mActivities;
    }

    public void onDestroy() {
        ImageTool.clearImageCache();
        finishActivities();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void finishActivities() {
        for (Activity activity : mActivities) {
            activity.finish();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //CrashHandler.getInstance().init(this);
        x.Ext.init(this);
        ImageLoaderTool.initImageLoader(getApplicationContext());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageTool.clearImageCache();
        ImageLoader.getInstance().clearMemoryCache();
    }

}
