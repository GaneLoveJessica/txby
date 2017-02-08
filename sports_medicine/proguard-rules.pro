# Add project specific ProGuard rules here.
# By default, the flags in this icon_browse are appended to flags specified
# in E:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class com.tianxiabuyi.sports_medicine.model.** {
        *;
}

-keep class com.tianxiabuyi.sports_medicine.pedometer.pojo.StepData {
        *;
} 
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,Annotation,EnclosingMethod,MethodParameters

-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法
-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆
#保护注解
-keepattributes *Annotation*
#如果引用了v4或者v7包
-dontwarn android.support.**
#忽略警告
-ignorewarning
####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####
-keep public class * extends android.view.View {
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
     public void set*(...);
}

-keep public class * {
	public protected *;
}

-keepclassmembers enum * {
	public static **[] values();
	public static ** valueOf(java.lang.String);
}

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keep class **.R$* {
    *;
}

###----- xUtils3 -----
-keepattributes Signature,*Annotation*
-keep public class org.xutils.** {
    public protected *;
}
-keep public interface org.xutils.** {
    public protected *;
}
-keepclassmembers class * extends org.xutils.** {
    public protected *;
}
-keepclassmembers @org.xutils.db.annotation.* class * {*;}
-keepclassmembers @org.xutils.http.annotation.* class * {*;}
-keepclassmembers class * {
    @org.xutils.view.annotation.Event <methods>;
}

##----- Gson -----
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

#----- 信鸽推送 -----
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}

#----- Glide图片加载 -----
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#-------- ShareSDK 相关的混淆配置---------
-keep class cn.sharesdk.**{*;}
	-keep class com.sina.**{*;}
	-keep class **.R$* {*;}
	-keep class **.R{*;}
	-keep class com.mob.**{*;}
	-dontwarn com.mob.**
	-dontwarn cn.sharesdk.**
	-dontwarn **.R$*

#----- ButterKnife -----
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

###-----------MPAndroidChart图库相关的混淆配置------------
-keep class com.github.mikephil.charting.** { *; }

#----- EventBus -----
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(Java.lang.Throwable);
}

#################### 优酷 ######################
-ignorewarnings #忽略警告
-keep class com.alibaba.** {*; }
-keep class cn.com.iresearch.** {*; }
-keep class com.youku.analytics.** {*; }
-keep class com.luajava.**{*;}

-keep public class com.youku.player.base.YoukuPlayerView
-keep interface  com.youku.player.base.YoukuPlayerView{
        public <fields>;
        public <methods>;
}

################ TakePhoto #########################
-keep class com.photo.** { *; }
-dontwarn com.photo.**

############### Bugly ################
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

############# BaseRecyclerViewAdapterHelper##############
-keep class com.chad.library.adapter.** {
   *;
}
