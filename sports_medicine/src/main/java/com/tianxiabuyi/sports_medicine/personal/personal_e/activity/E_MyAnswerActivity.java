package com.tianxiabuyi.sports_medicine.personal.personal_e.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.eeesys.frame.utils.ViewFindUtils;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.adapter.CommonFmPagerAdapter;
import com.tianxiabuyi.sports_medicine.personal.personal_e.fragment.AnswerFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 我的回答
 */
public class E_MyAnswerActivity extends BaseActivity{
    private String[] mTitles = {"待解答", "已解答"};
    private List<Fragment> mFragments = new ArrayList<>();
    private SegmentTabLayout mTabLayout;
    private ViewPager vpPatient;

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_e__my_answer;
    }

    @Override
    protected void initContentView() {
        View mDecorView = getWindow().getDecorView();
        mTabLayout = ViewFindUtils.find(mDecorView, R.id.stl_answer);
        vpPatient = ViewFindUtils.find(mDecorView, R.id.viewPager);
        mFragments.add(AnswerFragment.newInstance(1));
        mFragments.add(AnswerFragment.newInstance(2));
        CommonFmPagerAdapter adapter = new CommonFmPagerAdapter(getSupportFragmentManager(), Arrays.asList(mTitles),mFragments);
        vpPatient.setAdapter(adapter);
        mTabLayout.setTabData(mTitles);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpPatient.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        vpPatient.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void back(View view){
        finish();
    }

}
