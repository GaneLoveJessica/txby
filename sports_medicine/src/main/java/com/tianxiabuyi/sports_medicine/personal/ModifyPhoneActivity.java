package com.tianxiabuyi.sports_medicine.personal;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.model.User;
import com.tianxiabuyi.sports_medicine.personal.personal_c.event.ModifyPhoneEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 换绑手机
 */
public class ModifyPhoneActivity extends BaseActivity {
    @Bind(R.id.et_old_phone)
    EditText etOldPhone;
    @Bind(R.id.et_confirm_phone)
    EditText etConfirmPhone;
    @Bind(R.id.et_new_phone)
    EditText etNewPhone;
    @Bind(R.id.ll_new_phone)
    LinearLayout llNewPhone;
    @Bind(R.id.btn_change)
    Button btnChange;
    @Bind(R.id.tv_confirm_phone)
    TextView tvConfirmPhone;
    private boolean hasPhone;
    //    private CountDownTimer timer = new MyCountdownTimer(60000, 1000);
//    private EditText etVerification;
//    private TextView btnGetVerification;
//    private EventHandler eventHandler;
//    private Handler handler;
//    private Dialog pd;
//    private int currentPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_phone;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        title.setText("换绑手机");
        String phone = UserSpUtil.getUser(this).getPhone();
        if (phone!=null && phone.length() == 11) {
            hasPhone = true;
            etOldPhone.setText(new StringBuilder(phone).replace(3, 8, "*****"));
            etOldPhone.setEnabled(false);
        } else {
            hasPhone = false;
            tvConfirmPhone.setText("确认手机号");
            llNewPhone.setVisibility(View.GONE);
        }
//        initSms();
    }

//    private void initSms() {
//        handler = new Handler(this);
//        SMSSDK.initSDK(this, Constant.SMS_KEY, Constant.SMS_VALUE);
//        eventHandler = new EventHandler() {
//            @Override
//            public void afterEvent(int event, int result, Object data) {
//                Message msg = new Message();
//                msg.arg1 = event;
//                msg.arg2 = result;
//                msg.obj = data;
//                handler.sendMessage(msg);
//            }
//        };
//    }

    /**
     * 点击获取验证码按钮
     *
     * @param view
     *//*
    public void getVerification(View view) {
        if (checkPhoneNumber()) {
            etVerification.requestFocus();
            btnGetVerification.setEnabled(false);
            timer.start();
            if (currentPage == 1) {
                SMSSDK.getVerificationCode("86", UserSpUtil.getPhone(this));
            }else {
                SMSSDK.getVerificationCode("86", getText(etPhone));
            }
        }
    }*/

    /**
     * 点击修改手机号码按钮
     *
     * @param view
     */
    public void changePhone(View view) {
        if (hasPhone) {
            if (TextUtils.isEmpty(getText(etConfirmPhone))) {
                Toast.makeText(this, "请确认原手机号码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(getText(etNewPhone))) {
                Toast.makeText(this, "请输入新手机号码", Toast.LENGTH_SHORT).show();
                return;
            }
            String phone = UserSpUtil.getUser(this).getPhone();
            if (!getText(etConfirmPhone).equals(phone)) {
                Toast.makeText(this, "原手机号码不正确", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Pattern.matches(Constant.REGEX_PHONE, getText(etNewPhone))) {
                Toast.makeText(this, "新号码格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            if (TextUtils.isEmpty(getText(etOldPhone))) {
                Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(getText(etConfirmPhone))) {
                Toast.makeText(this, "请确认手机号码", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        uploadPhone();
//        if (!checkPhoneNumber()) return;
//        String code = getText(etVerification);
//        if (TextUtils.isEmpty(code)) {
//            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        pd = ProgressDialog.show(this, null, "加载中...", false, false);
//        if (currentPage == 1) {
//            SMSSDK.submitVerificationCode("86", UserSpUtil.getPhone(this), code);
//        }else{
//            SMSSDK.submitVerificationCode("86", getText(etPhone), code);
//        }
    }

    /**
     * 检查手机号码
     *
     * @return
     *//*
    private boolean checkPhoneNumber() {
        String phone;
        if (currentPage == 1){
            phone = UserSpUtil.getPhone(this);
        }else{
            phone = getText(etPhone);
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Pattern.matches(Constant.REGEX_PHONE, phone)) {
            Toast.makeText(this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }*/

   /* @Override
    public boolean handleMessage(Message message) {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        int event = message.arg1;
        int result = message.arg2;
        Object data = message.obj;
        if (result == SMSSDK.RESULT_COMPLETE) {
            //回调完成
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                //提交验证码成功
                if (currentPage == 1) {
                    changePage();
                } else {
                    uploadPhone();
                }
            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                boolean smart = (Boolean) data;
                if (smart) {
                    if (currentPage == 1) {
                        changePage();
                    } else {
                        uploadPhone();
                    }
                } else {
                    //获取验证码成功
                    Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            ((Throwable) data).printStackTrace();
            // 验证码不正确
            String msg = ((Throwable) data).getMessage();
            try {
                JSONObject json = new JSONObject(msg);
                Toast.makeText(this, json.optString("detail"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    *//**
     * 页面转换
     *//*
    private void changePage() {
        if (currentPage == 1) {
            currentPage = 2;
            etPhone.setEnabled(true);
            etPhone.setText("");
            timer.cancel();
            btnGetVerification.setEnabled(true);
            btnGetVerification.setText("发送验证码");
            etVerification.setText("");
            btnChangePhone.setText("确认绑定");
        }else{
            currentPage = 1;
            etPhone.setEnabled(false);
            etPhone.setText(new StringBuilder(UserSpUtil.getPhone(this)).replace(3, 7, "****"));
            timer.cancel();
            btnGetVerification.setEnabled(true);
            btnGetVerification.setText("发送验证码");
            etVerification.setText("");
            btnChangePhone.setText("验证后绑定手机号");
        }
    }

    @Override
    protected void leftClick() {
        if (currentPage == 1){
            finish();
        }else {
            changePage();
        }
    }*/

    /**
     * 请求修改手机号码
     */
    private void uploadPhone() {
        Param param = new Param(Constant.MODIFY);
        param.addToken();
        final String newPhone;
        if (hasPhone) {
            newPhone = getText(etNewPhone);
        } else {
            newPhone = getText(etOldPhone);
        }
        param.addRequstParams("phone", newPhone);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                ToastTool.show(ModifyPhoneActivity.this, "手机号码修改成功");
                User user = UserSpUtil.getUser(ModifyPhoneActivity.this);
                user.setPhone(newPhone);
                UserSpUtil.setUserInfo(ModifyPhoneActivity.this,user);
                EventBus.getDefault().post(new ModifyPhoneEvent());
                finish();
            }

            @Override
            public void err(HttpResult httpresult) {

            }
        });
    }

   /* private class MyCountdownTimer extends CountDownTimer {

        public MyCountdownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnGetVerification.setText(millisUntilFinished / 1000 + " 秒");
        }

        @Override
        public void onFinish() {
            btnGetVerification.setText("获取验证码");
            btnGetVerification.setEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
*/

}
