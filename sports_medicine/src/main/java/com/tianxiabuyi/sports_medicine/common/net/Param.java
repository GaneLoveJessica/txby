package com.tianxiabuyi.sports_medicine.common.net;

import com.eeesys.frame.http.HttpBean;
import com.eeesys.frame.utils.Encrpt;
import com.eeesys.frame.utils.GsonTool;
import com.tianxiabuyi.sports_medicine.MyApp;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;

import java.util.HashMap;
import java.util.Map;

public class Param extends HttpBean {
    public Param(String url) {
        super(url);
    }

    public Param(String url, Boolean isParams) {
        super(url, isParams);
    }

    public Param(String url, Boolean isParams, Boolean isOnlyUrl) {
        super(url, isParams, isOnlyUrl);
    }

    /**
     * 设置默认的请求参数
     */
    @Override
    public void setDefaultParams() {
        map.put("app_type", "section");
        map.put(Constant.HOSPITAL, Constant.HOSPITAL_ID);
    }

    public void addToken() {
        map.put(Constant.TOKEN, UserSpUtil.getToken(MyApp.getInstance()));
    }

    /**
     * 设置刷新token的请求参数和url
     */
    @Override
    public void setRefTokenParam() {
        Map<String, Object> defTokenParam = new HashMap<>();
        defTokenParam.put(Constant.HOSPITAL, Constant.HOSPITAL_ID);
        defTokenParam.put(Constant.TOKEN, UserSpUtil.getToken(MyApp.getInstance()));
        setDefultRefTokenParmers(Encrpt.encryptStr(GsonTool.toJson(defTokenParam)));
        setDefultRefTokenUrl(Constant.REFRESH_TOKEN);
    }

}
