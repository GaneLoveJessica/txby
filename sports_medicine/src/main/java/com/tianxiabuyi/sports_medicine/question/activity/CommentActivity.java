package com.tianxiabuyi.sports_medicine.question.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aihook.alertview.library.AlertView;
import com.aihook.alertview.library.OnItemClickListener;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.Encrpt;
import com.eeesys.frame.utils.GsonTool;
import com.eeesys.frame.utils.ToastTool;
import com.google.gson.reflect.TypeToken;
import com.photo.model.TResult;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BasePhotoActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.DateUtil;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.common.view.MyGridView;
import com.tianxiabuyi.sports_medicine.model.Question;
import com.tianxiabuyi.sports_medicine.model.Reply;
import com.tianxiabuyi.sports_medicine.question.adapter.AskImageAdapter;
import com.tianxiabuyi.sports_medicine.question.adapter.PictureAdapter;
import com.tianxiabuyi.sports_medicine.question.event.CommentEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发表评论
 */
public class CommentActivity extends BasePhotoActivity {
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.gv_images)
    MyGridView gvPicture;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    private ArrayList<String> imgPath = new ArrayList<>();
    private AskImageAdapter adapter;
    private Question question;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        title.setText(R.string.publish_comment);
        tvRight.setText(R.string.send);
        tvRight.setVisibility(View.VISIBLE);
        adapter = new AskImageAdapter(this, imgPath);
        gvPicture.setAdapter(adapter);
        gvPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CommentActivity.this, BrowseImgActivity.class);
                intent.putStringArrayListExtra(Constant.KEY_1, imgPath);
                intent.putExtra(Constant.KEY_2, position);
                startActivity(intent);
            }
        });
        question = getIntent().getParcelableExtra(Constant.KEY_1);
        if (question != null) {
            GlideUtil.setCircleAvatar(this, ivImage, question.getAvatar());
            tvName.setText(question.getUser_name());
            tvTime.setText(DateUtil.getPrefix(question.getCreate_time()));
            tvContent.setText(question.getContent());
            List<String> imgList = question.getImgs();
            if (imgList != null && imgList.size() > 0) {
                MyGridView gridView = (MyGridView) findViewById(R.id.gv_picture);
                gridView.setVisibility(View.VISIBLE);
                PictureAdapter replyAdapter = new PictureAdapter(this, question.getImgs());
                gridView.setAdapter(replyAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(CommentActivity.this, BrowseImgActivity.class);
                        intent.putStringArrayListExtra(Constant.KEY_1, question.getImgs());
                        intent.putExtra(Constant.KEY_2, position);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    protected void leftClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!showTip()) {
            super.onBackPressed();
        }
    }

    private boolean showTip() {
        if (!TextUtils.isEmpty(getText(etContent)) || imgPath.size() > 0) {
            new AlertView("提示", "是否离开编辑界面", null, new String[]{"离开"}, new String[]{"取消"},
                    this, AlertView.Style.AlertDialog, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position == 0) {
                        finish();
                    }
                }
            }).show();
            return true;
        }
        return false;
    }

    @Override
    protected void rightClick() {
        String content = etContent.getText().toString().trim();
        if (content.equals("")) {
            ToastTool.show(this, "请输入内容！");
            return;
        }
        hideSoftKeyboard();
        if (imgPath.size() > 0) {
            uploadPhotos(imgPath, new PhotosUploadBack() {
                @Override
                public void onSuccess(List<String> imgUrls) {
                    submitReply(imgUrls);
                }

                @Override
                public void onError(String s) {

                }
            });
        } else {
            submitReply(null);
        }
    }

    private void submitReply(List<String> imgUrls) {
        String content = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastTool.show(this, "请输入内容");
            return;
        }
        Param param = new Param(Constant.REPLY_QUESTION);
        param.addToken();
        param.addRequstParams("quest_id", question.getId());
        param.addRequstParams("content", content);
        if (imgUrls != null && imgUrls.size() > 0) {
            param.addRequstParams("imgs", Encrpt.encryptStr(GsonTool.toJson(imgUrls)));
        }
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                ToastTool.show(CommentActivity.this, "评论成功");
                ArrayList<Reply> replies = httpresult.getListResult("replies", new TypeToken<ArrayList<Reply>>() {
                });
                EventBus.getDefault().post(new CommentEvent(replies));
                finish();
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(CommentActivity.this, httpresult.getMsg());
            }
        });
    }

    @Override
    public void takeSuccess(TResult result) {
        imgPath.add(result.getImage().getCompressPath());
        adapter.notifyDataSetChanged();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @OnClick({R.id.iv_camera, R.id.iv_album})
    public void onClick(View view) {
        if (imgPath.size() >= Constant.MAX_IMG) {
            ToastTool.show(this, "最多可上传三张图片");
            return;
        }
        switch (view.getId()) {
            case R.id.iv_camera:
                takePhoto(false);
                break;
            case R.id.iv_album:
                selectPhoto(false);
                break;
        }
    }
}
