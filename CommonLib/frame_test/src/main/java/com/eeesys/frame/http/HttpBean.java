package com.eeesys.frame.http;

import android.text.TextUtils;

import com.eeesys.frame.utils.GsonTool;

import java.util.HashMap;
import java.util.Map;

/**
 * 对http请求传递的参数封装
 * Created by lenovos20-2 on 2015/12/21.
 */
public abstract class HttpBean implements HttpInterface.HttpToken{
    private Boolean isOnlyUrl;
    // url的地址
    private String url;
    // 传递的参数
    protected Map<String, Object> map = new HashMap<>();
    // 传递时是否加密；默认加密
    private Boolean postEncrpt = true;
    // 返回时是否解密 ；默认解密
    private Boolean resultEncrpt = true;
    // 是否显示加载界面；默认显示
    private Boolean isShowLoading = true;
    private String defultRefTokenUrl;
    private String defultRefTokenParmers;

    public HttpBean(String url) {
        this(url, true);
    }

    public HttpBean(String url, Boolean isParams) {
        this(url, isParams, false);
    }

    public HttpBean(String url, Boolean isParams, Boolean isOnlyUrl) {
        this.url = url;
        this.isOnlyUrl = isOnlyUrl;
        if (isParams) {
            setDefaultParams();
        }
    }


    public Map<String, Object> getMap() {
        return map;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getPostEncrpt() {
        return postEncrpt;
    }

    public void setPostEncrpt(Boolean postEncrpt) {
        this.postEncrpt = postEncrpt;
    }

    public Boolean getResultEncrpt() {
        return resultEncrpt;
    }

    public void setResultEncrpt(Boolean resultEncrpt) {
        this.resultEncrpt = resultEncrpt;
    }

    public Boolean getIsShowLoading() {
        return isShowLoading;
    }

    public void setIsShowLoading(Boolean isShowLoading) {
        this.isShowLoading = isShowLoading;
    }

    public Boolean containsToken() {
        return map.containsKey(SpuKey.TKOEN);
    }

    public Boolean getIsOnlyUrl() {
        return isOnlyUrl;
    }

    public void setIsOnlyUrl(Boolean isOnlyUrl) {
        this.isOnlyUrl = isOnlyUrl;
    }

    /**
     * 设置默认的参数
     */
    public abstract void setDefaultParams();

    /**
     * 参数不带token
     */
    public void removeToken() {
        map.remove(SpuKey.TKOEN);
    }

    /**
     * 获得token
     * @return
     */
    public String getToken() {
        String token = (String) map.get(SpuKey.TKOEN);
        return token;
    }
    /**
     * 设置请求的参数
     *
     * @param paramsKey
     * @param value
     */
    public void addRequstParams(String paramsKey, Object value) {
        map.put(paramsKey, value);
    }

    public String getDefultRefTokenParmers() {
        return defultRefTokenParmers;
    }

    public void setDefultRefTokenParmers(String defultRefTokenParmers) {
        this.defultRefTokenParmers = defultRefTokenParmers;
    }

    public String getDefultRefTokenUrl() {
        return defultRefTokenUrl;
    }

    public void setDefultRefTokenUrl(String defultRefTokenUrl) {
        this.defultRefTokenUrl = defultRefTokenUrl;
    }

    @Override
    public String toString() {
        if (map.size() > 0) {
            return TextUtils.concat(url, " ", GsonTool.toJson(map)).toString();
        }

        return url;
    }
}
