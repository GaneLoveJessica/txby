package com.tianxiabuyi.sports_medicine.question.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.aihook.alertview.library.AlertView;
import com.aihook.alertview.library.OnItemClickListener;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.Encrpt;
import com.eeesys.frame.utils.GsonTool;
import com.eeesys.frame.utils.ToastTool;
import com.eeesys.frame.view.WheelView;
import com.google.gson.reflect.TypeToken;
import com.photo.model.TResult;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BasePhotoActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.view.MyGridView;
import com.tianxiabuyi.sports_medicine.model.Category;
import com.tianxiabuyi.sports_medicine.model.Expert;
import com.tianxiabuyi.sports_medicine.question.adapter.AskExpertAdapter;
import com.tianxiabuyi.sports_medicine.question.adapter.AskImageAdapter;
import com.tianxiabuyi.sports_medicine.question.event.AskExpertEvent;
import com.tianxiabuyi.sports_medicine.question.event.PublishCommunityEvent;
import com.tianxiabuyi.sports_medicine.question.event.PublishQuestionEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 问题发表界面
 */
public class PublishActivity extends BasePhotoActivity implements View.OnClickListener, OnItemClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.tv_category)
    TextView tvCategory;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.gv_picture)
    MyGridView gvPicture;
    @Bind(R.id.tv_tip)
    TextView tvTip;
    @Bind(R.id.gv_expert)
    MyGridView gvExpert;
    private ArrayList<String> imgPath = new ArrayList<>();
    private AskImageAdapter adapter;
    private List<Expert> askExperts = new ArrayList<>();
    private int selectedCategory;
    private ArrayList<Category> categories = new ArrayList<>();
    private Expert toAskExpert;
    private AskExpertAdapter askExpertAdapter;
    private Expert expert;
    private int type;
    private AlertView chooseCategoryAlert;
    private WheelView wheelView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_publish_question;
    }

    @Override
    public void initContentView() {
        ButterKnife.bind(this);
        title.setText(R.string.ask_question);
        tvRight.setText(R.string.publish);
        tvRight.setVisibility(View.VISIBLE);
        gvExpert.setOnItemClickListener(this);
        gvPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PublishActivity.this, BrowseImgActivity.class);
                intent.putStringArrayListExtra(Constant.KEY_1, imgPath);
                intent.putExtra(Constant.KEY_2, position);
                startActivity(intent);
            }
        });
        tvTip.setText("有需要可以直接提问专家哦");
        // 问答：1  社区：2
        type = getIntent().getIntExtra(Constant.KEY_1, 0);
        if (type == 2) {
            gvExpert.setVisibility(View.GONE);
            tvTip.setVisibility(View.GONE);
        } else {
            expert = getIntent().getParcelableExtra(Constant.KEY_2);
            if (expert != null) {
                expert.setFlag(true);
                askExperts.add(expert);
                tvTip.setEnabled(false);
                tvTip.setText("提问专家");
                tvTip.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                askExpertAdapter = new AskExpertAdapter(PublishActivity.this, askExperts);
                gvExpert.setAdapter(askExpertAdapter);
            } else {
                gvExpert.setVisibility(View.GONE);
            }
        }
        adapter = new AskImageAdapter(this, imgPath);
        gvPicture.setAdapter(adapter);
        ArrayList<Category> categoryArrayList = getIntent().getParcelableArrayListExtra(Constant.KEY_3);
        selectedCategory = getIntent().getIntExtra(Constant.KEY_4, 0);
        if (categoryArrayList != null && categoryArrayList.size() > 0) {
            categories.addAll(categoryArrayList);
            tvCategory.setText(categories.get(selectedCategory).getName());
            createCategoryDialog();
        } else {
            loadCategory();
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
            hideSoftKeyboard();
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

    private void loadCategory() {
        Param param = new Param(Constant.QUES_CATEGORY);
        param.addRequstParams("type", type);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                List<Category> list = httpresult.getListResult("categorys", new TypeToken<ArrayList<Category>>() {
                });
                categories.clear();
                categories.addAll(list);
                tvCategory.setText(categories.get(selectedCategory).getName());
                createCategoryDialog();
            }

            @Override
            public void err(HttpResult httpresult) {
            }
        });
    }

    private void createCategoryDialog() {
        chooseCategoryAlert = new AlertView("选择分类", null, null, new String[]{"确定"}, new String[]{"取消"},
                this, AlertView.Style.AlertDialog, this);
        ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alert_wheelview, null);
        wheelView = (WheelView) extView.findViewById(R.id.wheelView);
        List<String> categoryNames = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            categoryNames.add(categories.get(i).getName());
        }
        wheelView.setItems(categoryNames);
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

            @Override
            public void onSelected(int selectedIndex, String item) {
                PublishActivity.this.selectedCategory = selectedIndex - 1;
            }
        });
        chooseCategoryAlert.addExtView(extView);
    }

    @Override
    protected void rightClick() {
        String content = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastTool.show(this, "请输入内容");
            return;
        }
        if (imgPath.size() > 0) {
            uploadPhotos(imgPath, new PhotosUploadBack() {
                @Override
                public void onSuccess(List<String> imgUrls) {
                    submitQuestion(imgUrls);
                }

                @Override
                public void onError(String s) {

                }
            });
        } else {
            submitQuestion(null);
        }
    }

    private void submitQuestion(List<String> imgUrls) {
        tvRight.setEnabled(false);
        hideSoftKeyboard();
        Param param = new Param(Constant.ASK_QUESTION);
        param.addToken();
        param.addRequstParams("title", "默认标题");
        param.addRequstParams("content", getText(etContent));
        param.addRequstParams("group", categories.get(selectedCategory).getId());
        if (toAskExpert != null) {
            param.addRequstParams("aid", toAskExpert.getId());
        } else if (expert != null) {
            param.addRequstParams("aid", expert.getId());
        }
        if (imgUrls != null && imgUrls.size() > 0) {
            param.addRequstParams("imgs", Encrpt.encryptStr(GsonTool.toJson(imgUrls)));
        }
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                ToastTool.show(PublishActivity.this, "发表成功");
                if (type == 1) {
                    EventBus.getDefault().post(new PublishQuestionEvent(selectedCategory, categories.get(selectedCategory).getName()));
                } else {
                    EventBus.getDefault().post(new PublishCommunityEvent(selectedCategory, categories.get(selectedCategory).getName()));
                }
                finish();
            }

            @Override
            public void err(HttpResult httpresult) {
                tvRight.setEnabled(true);
                ToastTool.show(PublishActivity.this, httpresult.getMsg());
            }
        });
    }

    @Override
    public void onItemClick(Object o, int position) {
        if (position == 0) {
            tvCategory.setText(categories.get(selectedCategory).getName());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Expert askExpert = (Expert) adapterView.getItemAtPosition(i);
        if (askExpert.getId() == askExpertAdapter.getChooseId()) {
            askExpertAdapter.setChooseExpertId(0);
            this.toAskExpert = null;
        } else {
            askExpertAdapter.setChooseExpertId(askExpert.getId());
            this.toAskExpert = askExpert;
        }
    }

    @OnClick({R.id.ll_choose_category, R.id.iv_add_pic, R.id.tv_tip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_choose_category:
                if (categories.size() == 0) {
                    loadCategory();
                } else {
                    wheelView.setSeletion(selectedCategory);
                    chooseCategoryAlert.show();
                }
                break;
            case R.id.iv_add_pic:
                if (imgPath.size() >= Constant.MAX_IMG) {
                    ToastTool.show(this, "最多只能上传3张图片");
                    return;
                }
                hideSoftKeyboard();
                new AlertView("添加图片", null, "取消", null,
                        new String[]{"拍照", "从相册中选择"},
                        this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            takePhoto(false);
                        } else if (position == 1) {
                            selectPhoto(false);
                        }
                    }
                }).show();
                break;
            case R.id.tv_tip:
                EventBus.getDefault().post(new AskExpertEvent());
                finish();
                break;
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        imgPath.add(result.getImage().getCompressPath());
        adapter.notifyDataSetChanged();
    }
}
