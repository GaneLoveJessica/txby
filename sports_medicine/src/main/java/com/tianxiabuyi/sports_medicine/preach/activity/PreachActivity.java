package com.tianxiabuyi.sports_medicine.preach.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.activity.LoginActivity;
import com.tianxiabuyi.sports_medicine.model.HomeMenu;
import com.tianxiabuyi.sports_medicine.model.User;
import com.tianxiabuyi.sports_medicine.preach.event.FindExpertEvent;
import com.tianxiabuyi.sports_medicine.preach.fragment.PreachTabFragment;
import com.tianxiabuyi.sports_medicine.preach.fragment.VideoFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreachActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_drawer_menu)
    ImageView ivDrawerMenu;
    @Bind(R.id.tv_group)
    TextView tvGroup;
    @Bind(R.id.tl_preach)
    TabLayout tlPreach;
    @Bind(R.id.vp_preach)
    ViewPager vpPreach;
    @Bind(R.id.left_drawer)
    ListView leftDrawer;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.load_error_view)
    LinearLayout loadErrorView;
    private List<HomeMenu> menus = new ArrayList<>();
    private int position;
    List<Fragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    private ImageView ivAvatar;
    private TextView tvName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preach;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        leftDrawer.setOnItemClickListener(this);
        View headView = getLayoutInflater().inflate(R.layout.list_head_main_drawer, leftDrawer, false);
        leftDrawer.addHeaderView(headView,null,false);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        position = getIntent().getIntExtra(Constant.KEY_1, 0);
        if (position != 1) {
            ivDrawerMenu.setVisibility(View.GONE);
            tvGroup.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            if (position == 3) {
                tvGroup.setText(R.string.find_expert);
                tvGroup.setEnabled(true);
                tvGroup.setVisibility(View.VISIBLE);
            }
        }
        tvTitle.setText(getIntent().getStringExtra(Constant.KEY_2));
        List<HomeMenu> menuList = CacheUtil.getCloudMenu(this);
        if (menuList != null && menuList.size() > 0) {
            setData(menuList);
            loadMenu(false);
        } else {
            loadMenu(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserSpUtil.isLogin(this)) {
            User user = UserSpUtil.getUser(this);
            tvName.setText(user.getUser_name());
            GlideUtil.setCircleAvatar(this, ivAvatar, user.getAvatar());
            tvName.setEnabled(false);
        } else {
            tvName.setText("点击登录");
            tvName.setEnabled(true);
        }
    }

    private void loadMenu(final boolean isFirst) {
        Param param = new Param(Constant.HOME_MENU);
        param.setIsShowLoading(isFirst);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                List<HomeMenu> menuList = httpresult.getListResult("category", new TypeToken<List<HomeMenu>>() {
                });
                CacheUtil.saveCache(PreachActivity.this, CacheUtil.KEY_CLOUD_MENU, menuList);
                if (isFirst) {
                    setData(menuList);
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                if (isFirst) {
                    ToastTool.show(PreachActivity.this, httpresult.getMsg());
                    loadErrorView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setData(List<HomeMenu> menuList) {
        menus.addAll(menuList);
        boolean isVideo = getIntent().getBooleanExtra(Constant.KEY_3, false);
        if (position == 0) {// 云荟萃
            setCurrentGroup(menus.get(menus.size() - 1), isVideo);
        } else if (position == 1) { // 云知识
            menus.remove(0);
            menus.remove(menus.size() - 1);
            menus.remove(menus.size() - 1);
            ArrayAdapter<HomeMenu> menuAdapter = new ArrayAdapter<HomeMenu>(PreachActivity.this, R.layout.list_item_drawer_menu, menus);
            leftDrawer.setAdapter(menuAdapter);
            // 默认选中第一项
            leftDrawer.setItemChecked(1, true);
            setCurrentGroup(menus.get(0), isVideo);
        } else if (position == 2) { // 云康复
            setCurrentGroup(menus.get(4), isVideo);
        } else if (position == 3) { // 云医疗
            setCurrentGroup(menus.get(3), isVideo);
        } else if (position == 4) { // 云基地
            setCurrentGroup(menus.get(5), isVideo);
        } else if (position == 5) { // 云动态
            setCurrentGroup(menus.get(0), isVideo);
        }
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    /**
     * @param homeMenu
     * @param isVideo
     */
    public void setCurrentGroup(HomeMenu homeMenu, boolean isVideo) {
        if (position != 3) {
            tvGroup.setText(homeMenu.getName());
        }
        fragments.clear();
        titles.clear();
        List<HomeMenu.SubBean> list = homeMenu.getSub();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals("视频")) {
                fragments.add(VideoFragment.newInstance(list.get(i).getId()));
            } else {
                fragments.add(PreachTabFragment.newInstance(list.get(i).getId()));
            }
            titles.add(list.get(i).getName());
        }
        if (titles.size() <= 1) {
            tlPreach.setVisibility(View.GONE);
        } else {
            tlPreach.setVisibility(View.VISIBLE);
        }
        vpPreach.removeAllViews();
        vpPreach.removeAllViewsInLayout();
        vpPreach.setAdapter(new CommunicatePagerAdapter(getSupportFragmentManager(), titles, fragments));
        vpPreach.setOffscreenPageLimit(titles.size());
        tlPreach.setupWithViewPager(vpPreach);
        if (isVideo) {
            vpPreach.setCurrentItem(fragments.size() - 1);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            drawerLayout.closeDrawer(leftDrawer);
            setCurrentGroup(menus.get(position - 1), false);
        }
    }

    @OnClick(R.id.load_error_view)
    public void onClick() {
        loadErrorView.setVisibility(View.GONE);
        loadMenu(true);
    }

    private class CommunicatePagerAdapter extends FragmentStatePagerAdapter {
        private final List<String> titles;
        private final List<Fragment> list;

        private CommunicatePagerAdapter(FragmentManager fm, List<String> titles, List<Fragment> list) {
            super(fm);
            this.titles = titles;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (titles == null) return null;
            return titles.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    /**
     * 打开侧滑菜单
     *
     * @param view
     */
    public void showDrawer(View view) {
        drawerLayout.openDrawer(leftDrawer);
    }

    public void back(View view) {
        finish();
    }

    public void toLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void findExpert(View view) {
        EventBus.getDefault().post(new FindExpertEvent());
        finish();
    }
}
