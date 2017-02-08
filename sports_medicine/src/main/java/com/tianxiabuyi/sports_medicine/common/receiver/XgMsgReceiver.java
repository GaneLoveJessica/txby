package com.tianxiabuyi.sports_medicine.common.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.tianxiabuyi.sports_medicine.main.activity.MainActivity;
import com.tianxiabuyi.sports_medicine.personal.personal_c.event.StepUpStandardEvent;
import com.tianxiabuyi.sports_medicine.question.event.MsgEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/9/29.
 */

public class XgMsgReceiver extends XGPushBaseReceiver {

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {

    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        Log.d("XgMsgReceiver", "onNotifactionClickedResult" + xgPushClickedResult);
        if (context == null || xgPushClickedResult == null) {
            return;
        }
        if (xgPushClickedResult.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            if ("运动云医院".equals(xgPushClickedResult.getTitle())) {
                EventBus.getDefault().postSticky(new StepUpStandardEvent());
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(xgPushClickedResult.getCustomContent());
                    String quesId = jsonObject.optString("id");
                    if (quesId != null) {
                        EventBus.getDefault().postSticky(new MsgEvent(quesId));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        Log.d("XgMsgReceiver", "onNotifactionShowedResult");
    }
}
