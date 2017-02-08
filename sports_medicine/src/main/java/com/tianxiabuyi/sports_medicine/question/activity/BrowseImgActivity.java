package com.tianxiabuyi.sports_medicine.question.activity;

import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.question.adapter.BigImagePagerAdapter;

import java.util.ArrayList;

/**
 * 浏览大图页面
 */
public class BrowseImgActivity extends BaseActivity {

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_browse_img;
    }

    @Override
    protected void initContentView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_image);
        final TextView tvPage = (TextView) findViewById(R.id.tv_page);
        final ArrayList<String> images = getIntent().getStringArrayListExtra(Constant.KEY_1);
        int position = getIntent().getIntExtra(Constant.KEY_2, 0);
        BigImagePagerAdapter adapter = new BigImagePagerAdapter(this,images);
        viewPager.setAdapter(adapter);
        tvPage.setText(position + 1 + "/" + images.size());
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvPage.setText(position + 1 + "/" + images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
