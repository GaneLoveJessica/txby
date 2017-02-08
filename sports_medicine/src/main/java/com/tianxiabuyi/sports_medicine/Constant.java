package com.tianxiabuyi.sports_medicine;

import android.Manifest;

/**
 * Created by Administrator on 2016/8/11.
 */
public class Constant {
    public static final String HOSPITAL = "hospital";
    public static final String HOSPITAL_ID = "1068";

    public static final String TOKEN = "token";

    // 请求码、结果码
    public static final int TAKE_PHOTO = 0x11;
    public static final int CHOOSE_PICTURE = 0x12;
    public static final int CROP_PICTURE = 0x13;

    public static final String KEY_1 = "key1";
    public static final String KEY_2 = "key2";
    public static final String KEY_3 = "key3";
    public static final String KEY_4 = "key4";

    // 图片上传的最大张数
    public static final int MAX_IMG = 3;

    public static final String REGEX_NAME = "^[\\u4e00-\\u9fa5]{2,20}$";
    public static final String REGEX_PHONE = "^1\\d{10}$";
    public static final String REGEX_PASSWORD = "[^\\u4e00-\\u9fa5]{6,20}";
    public static final String REGEX_USER_NAME = "^[a-zA-Z][a-zA-Z0-9]{3,19}$";
    public static final String RAZOR_APP_KEY = "d03421e5b35b95408890e0e5f951790f";
    public static final String[] PER_AVATAR = new String[]{Manifest.permission.CAMERA};
    // 问答缩略图
    public static final String THUMB = "http://cloud.eeesys.com/pu/thumb.php";
    public static final String UPLOAD_IMAGE = "http://cloud.eeesys.com/pu/upload.php";
    // 二维码图片地址
    public static final String QRCODE_IMG_URL = "http://file.eeesys.com/attach/1477041403713031705962944871.png";
    // 分享图片地址
    public static final String SHARE_IMG_URL = "http://file.eeesys.com/attach/1477039932779473740517632119.png";

    public final static String IP = "http://demoapi.eeesys.com:18088/v2/";
    public final static String WECHAT = "http://demowechat.eeesys.com/module/";
    // 版本更新
    public static final String UPDATE = "http://api.eeesys.com:18088/v2/app/update";

    // 积分说明
    public static final String POINT_EXPLAIN = "http://demowechat.eeesys.com/section/1068/score_rule.jsp";
    // 新闻分享地址
    public static final String NEWS_URL = WECHAT + "news/article.jsp?nid=";
    // APP 分享地址
    public static final String SHARE_PP_URL = WECHAT + "app/share.jsp?uid=";

    public static final String REFRESH_TOKEN = IP + "token/refresh.jsp";
    // 注册
    public static final String REGISTER = IP + "user/create";
    // 登录
    public static final String LOGIN = IP + "user/login";

    public static final String MODIFY_PWD = IP + "user/password";
    // 问答
    public static final String QUES_CATEGORY = IP + "quest/group";
    public static final String QUES_LIST = IP + "quest/quests";
    public static final String QUESTION_DETAIL = IP + "quest/show";
    public static final String ASK_QUESTION = IP + "quest/create";
    public static final String REPLY_QUESTION = IP + "quest/reply";
    public static final String PRAISE = IP + "operate/create";
    public static final String CANCEL_PRAISE = IP + "operate/cancel";
    public static final String PRAISE_PERSON = IP + "quest/love";
    public static final String MY_QUESTION = IP + "quest/my";

    public static final String BIND_AVATAR = IP + "user/avatar";
    // 首页菜单
    public static final String HOME_MENU = IP + "news/category";
    // 新闻列表
    public static final String NEWS_LIST = IP + "news/list.jsp";
    public static final String PREACH_DETAIL = IP + "news/article.jsp";
    // 我的回答已答
    public static final String MY_ANSWERED = IP + "quest/answered";
    // 我的回答未答
    public static final String MY_UN_ANSWER = IP + "quest/to_answer";
    // 我的专家
    public static final String MY_EXPERT = IP + "doctor/my";
    // 专家
    public static final String EXPERT = IP + "doctor/doctors";
    // 我的患者
    public static final String MY_PATIENT = IP + "patient/list";
    // 解绑第三方账号
    public static final String UNBIND_THIRD_ACCOUNT = IP + "user/unbind";
    // 绑定第三方账号
    public static final String BIND_THIRD_ACCOUNT = IP + "user/bind";
    // 修改资料
    public static final String MODIFY = IP + "user/modify";
    // 查询总积分和答题数
    public static final String TOTAL_POINT = IP + "index/count";
    // 我的积分
    public static final String MY_POINT = IP + "score/show";
    // 签到
    public static final String SIGN_IN = IP + "sign/create";
    public static final String PUBLISH_ARTICLE = IP + "news/create";
    public static final String ARTICLE_LIST = IP + "news/my";
    public static final String REPLY_NEWS = IP + "news/reply";
    public static final String COMMUNITY_CATEGORY = IP + "quest/group";
    public static final String ADD_SCORE = IP + "score/create";
    // 上传
    public static final String STEP = IP + "steps/day_step.jsp";
    // 历史步数
    public static final String MONTH_STEP = IP + "steps/month_step.jsp";
    // 日步数排行榜
    public static final String DAY_RANKING = IP + "steps/day_ranking.jsp";
    public static final String MONTH_RANKING = IP + "steps/month_ranking.jsp";
    public static final String HEALTH_DATA = IP + "user/person";
    public static final String FEEDBACK = IP + "app/feedback";

}
