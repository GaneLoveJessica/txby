package com.tianxiabuyi.sports_medicine.question.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.tianxiabuyi.sports_medicine.common.view.UnScrollViewPager;
import com.tianxiabuyi.sports_medicine.login.activity.LoginActivity;
import com.tianxiabuyi.sports_medicine.model.Category;
import com.tianxiabuyi.sports_medicine.question.activity.PublishActivity;
import com.tianxiabuyi.sports_medicine.question.activity.SearchActivity;
import com.tianxiabuyi.sports_medicine.question.event.CommCateLoadedEvent;
import com.tianxiabuyi.sports_medicine.question.event.PublishCommunityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 社区
 */

public class CommunityFragment extends LazyFragment implements View.OnClickListener {
    private static final String TAG = "QuestionFragment";
    @Bind(R.id.iv_menu)
    ImageView ivMenu;
    @Bind(R.id.tv_category)
    TextView tvCategory;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.iv_ask)
    ImageView ivAsk;
    @Bind(R.id.vp_communicate)
    UnScrollViewPager vpCommunicate;
    @Bind(R.id.load_error_view)
    LinearLayout loadErrorView;
    private List<Fragment> list = new ArrayList<>();
    private CommonFmPagerAdapter adapter;
    private ArrayList<Category> titles = new ArrayList<>();

    public CommunityFragment() {
        // Required empty public constructor
    }

    public static CommunityFragment newInstance() {
        return new CommunityFragment();
    }

    private void loadCommCategory(final boolean isShowLoading) {
        Param param = new Param(Constant.COMMUNITY_CATEGORY);
        param.addRequstParams("type", 2);
        param.setIsShowLoading(isShowLoading);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                loadErrorView.setVisibility(View.GONE);
                List<Category> categories = httpresult.getListResult("categorys", new TypeToken<ArrayList<Category>>() {
                });
                CacheUtil.saveCache(getActivity(), CacheUtil.KEY_CATE_COMMUNITY, categories);
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
        titles.addAll(categories);
        setAdapter();
        EventBus.getDefault().post(new CommCateLoadedEvent(categories));
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        tvTitle.setText(R.string.community);
        return view;
    }

    @Override
    protected void initData() {
        if (UserSpUtil.isFirstShowCommunity(getActivity())) {
            showTip();
        }
        List<Category> categories = CacheUtil.getCategory(getActivity(), CacheUtil.KEY_CATE_COMMUNITY);
        if (categories != null && categories.size() > 0) {
            setData(categories);
            loadCommCategory(false);
        } else {
            loadCommCategory(true);
        }
    }

    private void showTip() {
        final Dialog dialog = new Dialog(getActivity(), R.style.Dialog_Fullscreen);
        dialog.setContentView(R.layout.dialog_tip_community);
        ImageView iv = (ImageView) dialog.findViewById(R.id.iv_yes);
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UserSpUtil.setFirstShowCommunity(getActivity(), false);
            }
        });
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onPublishCommunityEvent(PublishCommunityEvent event) {
        vpCommunicate.setCurrentItem(event.getSelectedCategory());
        CommunityTab communicateTab = (CommunityTab) adapter.getItem(event.getSelectedCategory());
        tvCategory.setText(event.getName());
        communicateTab.initData();
    }

    public void setItem(int position, String name) {
        vpCommunicate.setCurrentItem(position, false);
        tvCategory.setText(name);
    }

    public void setAdapter() {
        loadErrorView.setVisibility(View.GONE);
        for (int i = 0; i < titles.size(); i++) {
            list.add(CommunityTab.newInstance(titles.get(i).getId(), false));
        }
        adapter = new CommonFmPagerAdapter(getChildFragmentManager(), null, list);
        vpCommunicate.setAdapter(adapter);
        vpCommunicate.setOffscreenPageLimit(list.size());
        tvCategory.setText(titles.get(0).getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_search, R.id.iv_ask, R.id.load_error_view})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.iv_ask:
                if (titles == null) return;
                if (!UserSpUtil.isLogin(getActivity())) {
                    startActivity(intent.setClass(getActivity(), LoginActivity.class));
                    return;
                }
                intent.setClass(getActivity(), PublishActivity.class);
                intent.putExtra(Constant.KEY_1, 2);
                intent.putParcelableArrayListExtra(Constant.KEY_3, titles);
                intent.putExtra(Constant.KEY_4, vpCommunicate.getCurrentItem());
                startActivity(intent);
                break;
            case R.id.iv_search:
                if (titles == null) return;
                intent.setClass(getActivity(), SearchActivity.class);
                intent.putParcelableArrayListExtra(Constant.KEY_1, titles);
                intent.putExtra(Constant.KEY_2, 2);
                startActivity(intent);
                break;
            case R.id.load_error_view:
                loadCommCategory(true);
                break;
        }
    }

}
