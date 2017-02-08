package com.tianxiabuyi.sports_medicine.personal;

import android.view.View;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.eeesys.frame.utils.Tools;
import com.eeesys.frame.view.CleanableEditText;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.Constant;

import java.util.regex.Pattern;

/**
 * 修改密码
 */
public class ModifyPwdActivity extends BaseActivity {
    private CleanableEditText oldPwd;
    private CleanableEditText newPwd;
    private CleanableEditText confirmPwd;
    private String oldStr;
    private String newStr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_pwd;
    }

    @Override
    protected void initContentView() {
        title.setText(R.string.modify_password);
        oldPwd = (CleanableEditText) findViewById(R.id.old_pwd);
        newPwd = (CleanableEditText) findViewById(R.id.new_pwd);
        confirmPwd = (CleanableEditText) findViewById(R.id.confirm_new_pwd);
    }

    public void modifyPwd(View view) {
        if (!checkPwd()) return;
        hideSoftKeyboard();
        Param param = new Param(Constant.MODIFY_PWD);
        param.addToken();
        param.addRequstParams("old_password", Tools.encryptMD5(oldStr));
        param.addRequstParams("repassword", Tools.encryptMD5(newStr));
        param.addRequstParams("password", Tools.encryptMD5(newStr));
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                UserSpUtil.setPwd(ModifyPwdActivity.this, newStr);
                ToastTool.show(ModifyPwdActivity.this, "密码修改成功");
                finish();
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(ModifyPwdActivity.this,httpresult.getMsg());
            }
        });
    }

    /**
     * 验证输入的密码信息
     */
    private boolean checkPwd() {
        oldStr = getText(oldPwd);
        if (oldStr.equals("")) {
            ToastTool.show(this, "请输入原密码");
            return false;
        }
        newStr = getText(newPwd);
        if (newStr.equals("")) {
            ToastTool.show(this, "请输入新密码");
            return false;
        }
        if (!Pattern.matches(Constant.REGEX_PASSWORD, newStr)) {
            ToastTool.show(this, "密码格式不正确");
            return false;
        }
        String confirmStr = getText(confirmPwd);
        if (confirmStr.equals("")) {
            ToastTool.show(this, "请确认新密码");
            return false;
        }
//        if (!oldStr.equals(UserSpUtil.getPwd(this))) {
//            ToastTool.show(this, "原密码不正确，请重新输入！");
//            return false;
//        }
        if (!newStr.equals(confirmStr)) {
            ToastTool.show(this, "两次输入的密码不一致");
            return false;
        }
        return true;
    }
}
