package com.eeesys.frame.http;

import com.eeesys.frame.utils.GsonTool;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by madc on 2016/4/13.
 */
public class HttpResult {
    private String ERR_CODE = "errcode";
    private String ERRMSG = "errmsg";
    private String result;
    private JSONObject jsonObject;

    public HttpResult(String result) {
        this.result = result;
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getResult() {
        return result;
    }

    /**
     * 获得errcode
     * @return
     */
    public int getErrCode() {
        int code = -999;
        try {
            code = jsonObject.getInt(ERR_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    public String getCodeString(String code) {
        String errcode = null;
        try {
            errcode = jsonObject.get(code).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errcode;
    }

    public int getCodeInt(String code) {
        int errcode = -1;
        try {
            errcode = jsonObject.getInt(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errcode;
    }

    /**
     * 获得errmsg
     * @return
     */
    public String getMsg() {
        String msg = null;
        try {
            msg = jsonObject.getString(ERRMSG);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 获得list
     * @param key
     * @param typeToken
     * @param <T>
     * @return
     */
    public <T> T getListResult(String key, TypeToken<T> typeToken) {
        try {
            JSONArray array = jsonObject.getJSONArray(key);
            return array == null ? null : GsonTool.fromJson(array.toString(), typeToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param key
     * @param c
     * @param <T>
     * @return
     */
    public <T> T getObjectResult(String key, Class<T> c) {
        try {
            JSONObject object = jsonObject.getJSONObject(key);
            return object == null ? null : GsonTool.fromJson(object.toString(), c);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getNumber(String s) {
        int nub = 0;
        try {
            nub = (int) jsonObject.get(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nub;
    }


    /**
     * 网络访问是否成功
     * @return
     */
    public boolean isSuccess() {
        if (getErrCode() != -999) {
            if (getErrCode() == 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
