package com.tianxiabuyi.sports_medicine.question.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.adapter.CommonFmPagerAdapter;
import com.tianxiabuyi.sports_medicine.common.fragment.LazyFragment;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.CacheUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.activity.LoginActivity;
import com.tianxiabuyi.sports_medicine.model.Category;
import com.tianxiabuyi.sports_medicine.question.activity.PublishActivity;
import com.tianxiabuyi.sports_medicine.question.activity.SearchActivity;
import com.tianxiabuyi.sports_medicine.question.event.PublishQuestionEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 问答
 */
public class QuestionFragment extends LazyFragment implements View.OnClickListener {
    private static final String TAG = "QuestionFragment";
    @Bind(R.id.iv_ask)
    ImageView ivAsk;
    @Bind(R.id.tl_communicate)
    TabLayout tlCommunicate;
    @Bind(R.id.vp_communicate)
    ViewPager vpCommunicate;
    @Bind(R.id.load_error_view)
    LinearLayout loadErrorView;
    private List<Fragment> list = new ArrayList<>();
    private CommonFmPagerAdapter adapter;
    private ArrayList<Category> titles = new ArrayList<>();

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance() {
        return new QuestionFragment();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    protected void initData() {
        List<Category> categories = CacheUtil.getCategory(getActivity(), CacheUtil.KEY_CATE_QUESTION);
        if (categories != null && categories.size() > 0) {
            setData(categories);
            requestCategory(false);
        } else {
            requestCategory(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onPublishQuestionEvent(PublishQuestionEvent event) {
        int selectedCategory = event.getSelectedCategory();
        vpCommunicate.setCurrentItem(selectedCategory);
        QuestionTab communicateTab = (QuestionTab) adapter.getItem(selectedCategory);
        communicateTab.initData();
    }

    private void requestCategory(final boolean isShowLoading) {
        Param param = new Param(Constant.QUES_CATEGORY);
        param.addRequstParams("type", 1);
        param.setIsShowLoading(isShowLoading);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                loadErrorView.setVisibility(View.GONE);
                List<Category> categories = httpresult.getListResult("categorys", new TypeToken<ArrayList<Category>>() {
                });
                CacheUtil.saveCache(getActivity(), CacheUtil.KEY_CATE_QUESTION, categories);
                if (isShowLoading) {
                    setData(categories);
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                if (isShowLoading) {
                    ToastTool.show(getContext(),httpresult.getMsg());
                    loadErrorView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setData(List<Category> categories) {
        titles.clear();
        titles.addAll(categories);
        list.clear();
        List<String> titleList = new ArrayList<String>();
        for (int i = 0; i < titles.size(); i++) {
            list.add(QuestionTab.newInstance(titles.get(i).getId(), false));
            titleList.add(titles.get(i).getName());
        }
        adapter = new CommonFmPagerAdapter(getChildFragmentManager(), titleList, list);
        vpCommunicate.setAdapter(adapter);
        vpCommunicate.setOffscreenPageLimit(list.size());
        tlCommunicate.setupWithViewPager(vpCommunicate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_search, R.id.load_error_view, R.id.iv_ask})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.load_error_view:
                requestCategory(true);
                break;
            case R.id.iv_ask:
                if (titles == null) return;
                if (!UserSpUtil.isLogin(getActivity())) {
                    startActivity(intent.setClass(getActivity(), LoginActivity.class));
                    return;
                }
                intent.setClass(getActivity(), PublishActivity.class);
                intent.putExtra(Constant.KEY_1, 1);
                intent.putParcelableArrayListExtra(Constant.KEY_3, titles);
                intent.putExtra(Constant.KEY_4, vpCommunicate.getCurrentItem());
                startActivity(intent);
                break;
            case R.id.iv_search:
                if (titles == null) return;
                intent.setClass(getActivity(), SearchActivity.class);
                intent.putParcelableArrayListExtra(Constant.KEY_1, titles);
                intent.putExtra(Constant.KEY_2, 1);
                startActivity(intent);
                break;
        }
    }
}
