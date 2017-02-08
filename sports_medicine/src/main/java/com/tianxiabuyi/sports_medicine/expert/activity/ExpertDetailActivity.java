package com.tianxiabuyi.sports_medicine.expert.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.widget.HeaderScrollHelper;
import com.lzy.widget.HeaderViewPager;
import com.lzy.widget.tab.PagerSlidingTabStrip;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.adapter.CommonFmPagerAdapter;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.expert.fragment.MyAnswerFragment;
import com.tianxiabuyi.sports_medicine.expert.fragment.MyArticleFragment;
import com.tianxiabuyi.sports_medicine.expert.fragment.MyInfoFragment;
import com.tianxiabuyi.sports_medicine.model.Expert;
import com.tianxiabuyi.sports_medicine.question.activity.PublishActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExpertDetailActivity extends BaseActivity {
    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_profession)
    TextView tvProfession;
    @Bind(R.id.tv_love_number)
    TextView tvLoveNumber;
    @Bind(R.id.tv_answer_number)
    TextView tvAnswerNumber;
    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;
    @Bind(R.id.foot)
    LinearLayout foot;
    @Bind(R.id.ll_praise)
    LinearLayout llPraise;
    @Bind(R.id.fl_toolbar)
    FrameLayout flToolbar;
    @Bind(R.id.ll_top)
    LinearLayout llTop;
    private Expert expert;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_expert_detail;
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        expert = getIntent().getParcelableExtra(Constant.KEY_1);
        if (expert.getJson() != null) {
            if (expert.getJson().getMy_title()==null){
                tvProfession.setVisibility(View.GONE);
            }else {
                tvProfession.setText(expert.getJson().getMy_title());
            }
        }
        boolean isSuperExpert = getIntent().getBooleanExtra(Constant.KEY_2, false);
        if (isSuperExpert) {
            foot.setVisibility(View.GONE);
            llPraise.setVisibility(View.GONE);
        }
        GlideUtil.setCircleAvatar(this, ivAvatar, expert.getAvatar());
        tvName.setText(expert.getName());
        tvLoveNumber.setText(expert.getLove() + "");
        tvAnswerNumber.setText(expert.getAnswer() + "");

        fragments.add(MyAnswerFragment.newInstance(expert.getId(), expert.getUser_name()));
        fragments.add(MyArticleFragment.newInstance(expert.getUser_name()));
        fragments.add(MyInfoFragment.newInstance(expert.getJson().getIntroduce()));
        String[] titles = {"我的解答", "我的文章", "我的简介"};
        viewPager.setAdapter(new CommonFmPagerAdapter(getSupportFragmentManager(),
                Arrays.asList(titles), fragments));
        tabs.setViewPager(viewPager);
        scrollableLayout.setCurrentScrollableContainer((HeaderScrollHelper.ScrollableContainer) fragments.get(0));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                scrollableLayout.setCurrentScrollableContainer((HeaderScrollHelper.ScrollableContainer) fragments.get(position));
            }
        });
        scrollableLayout.setOnScrollListener(new HeaderViewPager.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                String alpha = Integer.toHexString((int) (255 * (1.0f * currentY / maxY)));
                if (alpha.length() == 1) {
                    alpha = "0" + alpha;
                }
                flToolbar.setBackgroundColor(Color.parseColor("#" + alpha + "1eb6e7"));
            }
        });
        viewPager.setOffscreenPageLimit(titles.length);
    }

    public void back(View view) {
        finish();
    }

    public void toAskQuestion(View view) {
        Intent intent = new Intent(this, PublishActivity.class);
        intent.putExtra(Constant.KEY_1, 1);
        intent.putExtra(Constant.KEY_2, expert);
        startActivity(intent);
    }
}
