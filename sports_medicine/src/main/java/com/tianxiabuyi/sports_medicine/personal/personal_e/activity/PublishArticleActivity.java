package com.tianxiabuyi.sports_medicine.personal.personal_e.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aihook.alertview.library.AlertView;
import com.aihook.alertview.library.OnItemClickListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.CacheUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.model.HomeMenu;
import com.tianxiabuyi.sports_medicine.personal.personal_e.event.PublishEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.richeditor.RichEditor;

public class PublishArticleActivity extends BaseActivity implements OptionsPickerView.OnOptionsSelectListener {

    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.tv_category)
    TextView tvCategory;
    @Bind(R.id.ll_choose_category)
    LinearLayout llChooseCategory;
    @Bind(R.id.re_content)
    RichEditor mEditor;
    private OptionsPickerView optionsPickerView;
    private ArrayList<HomeMenu> categories;
    private HomeMenu.SubBean selectedCategory;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_article;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        title.setText(R.string.publish_article);
        tvRight.setText(R.string.complete);
        tvRight.setVisibility(View.VISIBLE);
        mEditor.setPlaceholder("输入内容");
        mEditor.setPadding(16, 16, 16, 16);
        mEditor.setEditorHeight(200);
        optionsPickerView = new OptionsPickerView(PublishArticleActivity.this);
        optionsPickerView.setOnoptionsSelectListener(this);
        ArrayList<HomeMenu> menuList = CacheUtil.getCloudMenu(this);
        if (menuList != null && menuList.size() > 0) {
            categories = menuList;
            setCategory();
            loadData(false);
        } else {
            loadData(true);
        }
    }

    private void setCategory() {
        ArrayList<List<HomeMenu.SubBean>> childList = new ArrayList<>();
        categories.remove(0);
        categories.remove(categories.size() - 1);
        categories.remove(categories.size() - 1);
        for (int i = 0; i < categories.size(); i++) {
            childList.add(categories.get(i).getSub());
        }
        for (int i = 0; i < childList.get(0).size(); i++) {
            if (childList.get(0).get(i).getName().equals("视频")) {
                childList.get(0).remove(i);
                break;
            }
        }
        optionsPickerView.setPicker(categories, childList, true);
        optionsPickerView.setSelectOptions(0);
        optionsPickerView.setCyclic(false);
        selectedCategory = childList.get(0).get(0);
        tvCategory.setText(childList.get(0).get(0).getName());
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
        if (!TextUtils.isEmpty(mEditor.getHtml()) || !TextUtils.isEmpty(getText(etTitle))) {
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
        if (getText(etTitle).length() == 0) {
            ToastTool.show(this, "请输入标题");
        } else if (mEditor.getHtml().length() == 0 || mEditor.getHtml().equals("<br>")) {
            ToastTool.show(this, "请输入内容");
        } else if (getText(tvCategory).length() == 0) {
            ToastTool.show(this, "请选择文章分类");
        } else {
            hideSoftKeyboard();
            submitArticle();
        }
    }

    private void submitArticle() {
        tvRight.setEnabled(false);
        Param param = new Param(Constant.PUBLISH_ARTICLE);
        param.addRequstParams("title", getText(etTitle));
        param.addRequstParams("content", mEditor.getHtml());
        param.addRequstParams("author", UserSpUtil.getUsername(this));
        param.addRequstParams("category", selectedCategory.getId());
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                EventBus.getDefault().post(new PublishEvent());
                finish();
            }

            @Override
            public void err(HttpResult httpresult) {
                tvRight.setEnabled(true);
                ToastTool.show(PublishArticleActivity.this, httpresult.getMsg());
            }
        });
    }

    private void loadData(final boolean isShowLoading) {
        Param param = new Param(Constant.HOME_MENU);
        param.setIsShowLoading(isShowLoading);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                ArrayList<HomeMenu> caMenus = httpresult.getListResult("category", new TypeToken<ArrayList<HomeMenu>>() {
                });
                CacheUtil.saveCache(PublishArticleActivity.this, CacheUtil.KEY_CLOUD_MENU, caMenus);
                if (isShowLoading) {
                    categories = caMenus;
                    setCategory();
                }
            }

            @Override
            public void err(HttpResult httpresult) {

            }
        });
    }

    /**
     * 点击分类
     *
     * @param view
     */
    public void chooseCategory(View view) {
        if (categories == null || categories.size() == 0) {
            loadData(true);
        } else {
            hideSoftKeyboard();
            optionsPickerView.show();
        }
    }

    @Override
    public void onOptionsSelect(int options1, int option2, int options3) {
        this.selectedCategory = categories.get(options1).getSub().get(option2);
        tvCategory.setText(selectedCategory.getName());
    }
}
