package com.tianxiabuyi.sports_medicine.common.util;

import android.content.Context;

import com.eeesys.frame.utils.GsonTool;
import com.eeesys.frame.utils.SPUtils;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.model.Category;
import com.tianxiabuyi.sports_medicine.model.Expert;
import com.tianxiabuyi.sports_medicine.model.HomeMenu;
import com.tianxiabuyi.sports_medicine.model.Preach;
import com.tianxiabuyi.sports_medicine.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */

public class CacheUtil {

    public static final String KEY_CLOUD_EXPERT = "cloud_expert";
    public static final String KEY_CLOUD_BASE = "preach_20";
    public static final String KEY_CLOUD_VIDEO = "preach_6";
    public static final String KEY_CLOUD_DYNAMIC = "preach_2";
    public static final String KEY_CLOUD_MENU = "cloud_menu";
    public static final String KEY_CATE_QUESTION = "category_question";
    public static final String KEY_CATE_COMMUNITY = "category_community";
    public static final String KEY_EXPERT = "expert";
    public static final String KEY_PREACH = "preach_";
    public static final String KEY_QUESTION = "question_";
    public static final String KEY_COMMUNITY = "community_";

    public static <T> void saveCache(Context context, String key, List<T> list) {
        SPUtils.put(context, key, GsonTool.toJson(list));
    }

    public static List<Expert> getExpert(Context context,String key) {
        String json = (String) SPUtils.get(context, key, "");
        return GsonTool.fromJson(json, new TypeToken<List<Expert>>() {
        });
    }

    public static List<Preach> getPreach(Context context,String key) {
        String json = (String) SPUtils.get(context, key, "");
        return GsonTool.fromJson(json, new TypeToken<List<Preach>>() {
        });
    }

    public static ArrayList<HomeMenu> getCloudMenu(Context context) {
        String json = (String) SPUtils.get(context, KEY_CLOUD_MENU, "");
        return GsonTool.fromJson(json, new TypeToken<ArrayList<HomeMenu>>() {
        });
    }

    public static List<Category> getCategory(Context context,String key) {
        String json = (String) SPUtils.get(context, key, "");
        return GsonTool.fromJson(json, new TypeToken<List<Category>>() {
        });
    }

    public static List<Question> getQuestion(Context context, String categoryId) {
        String json = (String) SPUtils.get(context, categoryId, "");
        return GsonTool.fromJson(json, new TypeToken<List<Question>>() {
        });
    }

}
