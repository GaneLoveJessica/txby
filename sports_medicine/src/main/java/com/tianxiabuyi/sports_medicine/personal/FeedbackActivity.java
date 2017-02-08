package com.tianxiabuyi.sports_medicine.personal;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.AppUtils;
import com.eeesys.frame.utils.ToastTool;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity implements RatingBar.OnRatingBarChangeListener {
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.btn_commit)
    Button btnCommit;
    private float rating = 5f;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initContentView() {
        title.setText(R.string.feedback);
        ButterKnife.bind(this);
        ratingBar.setOnRatingBarChangeListener(this);
    }

    @OnClick(R.id.btn_commit)
    public void onClick() {
        String content = etContent.getText().toString().trim();
        if (rating <= 0) {
            ToastTool.show(this, R.string.toast_rating);
        } else if (content.equals("")) {
            ToastTool.show(this, R.string.toast_feedback);
        } else {
            hideSoftKeyboard();
            btnCommit.setEnabled(false);
            toCommit(content);
        }
    }

    private void toCommit(String content) {
        Param param = new Param(Constant.FEEDBACK);
        param.addRequstParams("version", AppUtils.getVersionName(this));
        param.addRequstParams("version_code", AppUtils.getVersionCode(this));
        param.addRequstParams("device", 1);
        param.addRequstParams("suggestion", content);
        param.addRequstParams("grade", rating);
        param.addRequstParams("uid", UserSpUtil.getUid(this));
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                ToastTool.show(FeedbackActivity.this, R.string.toast_feedback_success);
                finish();
            }

            @Override
            public void err(HttpResult httpresult) {
                btnCommit.setEnabled(true);
                ToastTool.show(FeedbackActivity.this, httpresult.getMsg());
            }
        });
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        this.rating = rating;
    }
}
