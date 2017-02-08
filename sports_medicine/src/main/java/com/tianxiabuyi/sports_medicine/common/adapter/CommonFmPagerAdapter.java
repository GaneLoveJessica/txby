package com.tianxiabuyi.sports_medicine.common.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
public class CommonFmPagerAdapter extends FragmentPagerAdapter {
    private final List<String> titles;
    private final List<? extends Fragment> list;
    private final FragmentManager fm;

    public CommonFmPagerAdapter(FragmentManager fm, List<String> titles, List<? extends Fragment> list) {
        super(fm);
        this.titles = titles;
        this.list = list;
        this.fm = fm;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null || titles.size() == 0) return null;
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        fm.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);  
        Fragment fragment = list.get(position);
        fm.beginTransaction().hide(fragment).commit();
    }
}
