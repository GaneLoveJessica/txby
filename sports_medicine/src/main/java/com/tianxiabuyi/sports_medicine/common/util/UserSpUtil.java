package com.tianxiabuyi.sports_medicine.common.util;

import android.content.Context;

import com.eeesys.frame.utils.GsonTool;
import com.eeesys.frame.utils.SPUtils;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.model.User;

import static com.eeesys.frame.utils.SPUtils.get;

/**
 * 用户信息
 */
public class UserSpUtil {
    private static final String USERNAME = "username";
    private static final String PWD = "pwd";
    private static final String STATUS = "status";
    private static final String FIRST_SHOW_COMMUNITY = "first_show_community";
    private static final String FIRST_SHOW_EXPERT = "first_show_expert";
    private static final String USER = "user";

    public static boolean isFirstShowCommunity(Context context) {
        return (boolean) get(context, FIRST_SHOW_COMMUNITY, true);
    }

    public static void setFirstShowCommunity(Context context, boolean firstShow) {
        SPUtils.put(context, FIRST_SHOW_COMMUNITY, firstShow);
    }

    public static boolean isFirstShowExpert(Context context) {
        return (boolean) get(context, FIRST_SHOW_EXPERT, true);
    }

    public static void setFirstShowExpert(Context context, boolean firstShow) {
        SPUtils.put(context, FIRST_SHOW_EXPERT, firstShow);
    }

    public static String getToken(Context context) {
        return (String) get(context, Constant.TOKEN, "");
    }

    public static void setToken(Context context, String token) {
        SPUtils.put(context, Constant.TOKEN, token);
    }

    public static void setTime(Context context) {
        SPUtils.put(context, "time", System.currentTimeMillis());
    }

    public static Long getTime(Context context) {
        return (Long) get(context, "time", 0L);
    }

    public static void setPwd(Context context, String pwd) {
        SPUtils.put(context, PWD, pwd);
    }

    public static String getPwd(Context context) {
        return (String) get(context, PWD, "");
    }

    public static boolean isLogin(Context context) {
        return getPwd(context).length() > 0 || getUnionId(context).length() > 0;
    }

    /**
     * 保存用户登录信息
     *
     * @param context
     * @param user
     */
    public static void setUserInfo(Context context, User user) {
        SPUtils.put(context, "uid", user.getUid());
        SPUtils.put(context, USERNAME, user.getUser_name());
        SPUtils.put(context, STATUS, user.getType());
        SPUtils.put(context,USER, GsonTool.toJson(user));
    }

    /**
     * 删除用户登录信息
     *
     * @param context
     */
    public static void removeUserInfo(Context context) {
        SPUtils.remove(context, PWD);
        SPUtils.remove(context, "token");
        SPUtils.remove(context, "uid");
        SPUtils.remove(context, "union_id");
        SPUtils.remove(context, STATUS);
        SPUtils.remove(context,USER);
    }

    public static void setUserName(Context context, String phone) {
        SPUtils.put(context, USERNAME, phone);
    }

    public static String getUsername(Context context) {
        return (String) get(context, USERNAME, "");
    }

    public static int getUid(Context context) {
        return (int) get(context, "uid", 0);
    }

    /**
     * 获取用户身份
     * 100：普通用户
     * 200：专家
     *
     * @param context
     * @return
     */
    public static int getStatus(Context context) {
        return (int) get(context, STATUS, 0);
    }

    /**
     * 保存第三方用户id
     *
     * @param context
     * @param userId
     */
    public static void setUnionId(Context context, String userId) {
        SPUtils.put(context, "union_id", userId);
    }

    private static String getUnionId(Context context) {
        return (String) get(context, "union_id", "");
    }

    public static User getUser(Context context) {
        return GsonTool.fromJson((String) get(context,USER,""),User.class);
    }

    public static void setManager(Context context, String isManager) {
        SPUtils.put(context, "is_manager", isManager);
    }

    public static boolean isManager(Context context) {
        String isManager = (String) SPUtils.get(context, "is_manager","");
        return "1".equals(isManager);
    }
}
