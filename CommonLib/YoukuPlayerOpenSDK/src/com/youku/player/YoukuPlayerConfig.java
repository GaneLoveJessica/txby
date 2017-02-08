package com.youku.player;

import android.content.Context;

import com.thx.imageloader.cache.disc.naming.Md5FileNameGenerator;
import com.thx.imageloader.core.ImageLoader;
import com.thx.imageloader.core.ImageLoaderConfiguration;
import com.thx.imageloader.core.assist.QueueProcessingType;
import com.youku.analytics.AnalyticsAgent;
import com.youku.analytics.utils.Tools;
import com.youku.player.base.Platform;
import com.youku.player.goplay.Profile;
import com.youku.player.util.AnalyticsManager;
import com.youku.player.util.ValidateUtil;

/**
 * 播放器初始化类，在应用程序启动时使用
 */
public class YoukuPlayerConfig {

	/**
	 * 初始化播放器相关参数接口，在应用启动时调用
	 */
	private static void onInitial(final Context cxt) {
		Tools.setContext(cxt);
		YoukuPlayerApplication.onInitial(cxt);

	}

	/**
	 * 初始化播放器相关参数接口，在应用启动时调用
	 * 
	 * @param pid
	 *            应用的pid参数,目前云作为保留参数，直接传空值即可
	 */
	public static void onInitial(final Context ctx, String pid) {
		YoukuPlayerProfile.PLATFORM = Platform.YOUKU;
		YoukuPlayerProfile.CLOUD_USER_AGENT = YoukuPlayerProfile
				.getCloudUserAgent(ctx);
		setPid("528a34396e9040f3");
		onInitial(ctx);
		initStatistics();

		String nativeLibPath = "/data/data/"
				+ ctx.getApplicationContext().getPackageName() + "/lib/";
		setNativeLibraryPath(nativeLibPath);

		ImageLoader.getInstance().init(buildDefaultILC(ctx));
	}

	private static ImageLoaderConfiguration buildDefaultILC(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this); method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
				context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		return config.build();
	}

	public static void setClientIdAndSecret(String clientId, String clientSecret) {
		if (ValidateUtil.isValid(clientId)) {
			clientId.trim();
		}
		if (ValidateUtil.isValid(clientSecret)) {
			clientSecret.trim();
		}
		YoukuPlayerProfile.CLIENT_ID = clientId;
		YoukuPlayerProfile.CLIENT_SECRET = clientSecret;
		if (YoukuPlayerApplication.context != null) {
			com.youku.analytics.utils.Config.appname = clientId;
		}
	}

	/**
	 * 优酷播放器统计数据初始化接口
	 */
	private static void initStatistics() {

		YoukuPlayerApplication.initStatistics();
	}

	/**
	 * 应用退出时调用
	 */
	public static void exit() {
		YoukuPlayerApplication.exit();
	}

	/**
	 * 设置pid参数
	 * 
	 * @param pid
	 */
	private static void setPid(String pid) {
		YoukuPlayerApplication.setPid(pid);
	}

	/**
	 * 
	 * 设置so库的路径</br> 例如：“/data/data/ + getApplicationContext().getPackageName()
	 * + /lib/”
	 * 
	 * @param nativeLibraryPath
	 * 
	 */
	private static void setNativeLibraryPath(String nativeLibraryPath) {
		YoukuPlayerApplication.setNativeLibraryPath(nativeLibraryPath);
	}

	/**
	 * 日志开关
	 * 
	 * @param isOpen
	 */
	public static void setLog(boolean isOpen) {
		com.baseproject.utils.Profile.LOG = isOpen; // 关闭log
		com.baseproject.utils.Logger.DEBUG = isOpen; // 关闭log
		com.baseproject.utils.Logger.ERROR = isOpen; // 关闭log
	}
}
