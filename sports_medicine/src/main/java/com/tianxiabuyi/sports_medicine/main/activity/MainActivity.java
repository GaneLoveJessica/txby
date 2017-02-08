package com.tianxiabuyi.sports_medicine.main.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.model.TabEntity;
import com.eeesys.frame.utils.Encrpt;
import com.eeesys.frame.utils.GsonTool;
import com.eeesys.frame.utils.ToastTool;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.tencent.android.tpush.XGPushManager;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.DateUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.common.view.UnScrollViewPager;
import com.tianxiabuyi.sports_medicine.expert.fragment.ExpertFragment;
import com.tianxiabuyi.sports_medicine.home.fragment.HomeFragment;
import com.tianxiabuyi.sports_medicine.login.activity.LoginActivity;
import com.tianxiabuyi.sports_medicine.login.event.LoginEvent;
import com.tianxiabuyi.sports_medicine.model.Category;
import com.tianxiabuyi.sports_medicine.model.Step;
import com.tianxiabuyi.sports_medicine.pedometer.pojo.StepData;
import com.tianxiabuyi.sports_medicine.pedometer.service.StepService;
import com.tianxiabuyi.sports_medicine.pedometer.utils.DbUtils;
import com.tianxiabuyi.sports_medicine.personal.LogoutEvent;
import com.tianxiabuyi.sports_medicine.personal.personal_c.event.StepEvent;
import com.tianxiabuyi.sports_medicine.personal.personal_c.event.StepUpStandardEvent;
import com.tianxiabuyi.sports_medicine.personal.personal_c.fragment.C_PersonalFragment;
import com.tianxiabuyi.sports_medicine.personal.personal_e.fragment.E_PersonalFragment;
import com.tianxiabuyi.sports_medicine.preach.event.FindExpertEvent;
import com.tianxiabuyi.sports_medicine.question.activity.QuesDetActivity;
import com.tianxiabuyi.sports_medicine.question.event.AskExpertEvent;
import com.tianxiabuyi.sports_medicine.question.event.CommCateLoadedEvent;
import com.tianxiabuyi.sports_medicine.question.event.MsgEvent;
import com.tianxiabuyi.sports_medicine.question.fragment.CommunityFragment;
import com.tianxiabuyi.sports_medicine.question.fragment.QuestionFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements Handler.Callback, AdapterView.OnItemClickListener,
        OnTabSelectListener {
    private static final String TAG = "MainActivity";
    @Bind(R.id.activity_new_main)
    LinearLayout activityNewMain;
    @Bind(R.id.ctl_main)
    CommonTabLayout ctlMain;
    @Bind(R.id.left_drawer)
    ListView leftDrawer;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.fl_main)
    UnScrollViewPager mViewPager;
    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Handler delayHandler;
    private long mExitTime;
    private static final long TIME_INTERVAL = 500;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"首页", "问答", "社区", "专家", "个人"};
    private int[] mIconUnselectIds = {
            R.mipmap.icon_main_home, R.mipmap.icon_main_question,
            R.mipmap.icon_main_com_up, R.mipmap.icon_main_expert, R.mipmap.icon_main_personal};
    private int[] mIconSelectIds = {
            R.mipmap.icon_main_home_checked, R.mipmap.icon_main_question_checked,
            R.mipmap.icon_main_com_down, R.mipmap.icon_main_expert_checked, R.mipmap.icon_main_personal_checked};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private MyPagerAdapter pagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_main;
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected void initContentView() {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        if (UserSpUtil.isLogin(this)) {
            Context context = getApplicationContext();
            XGPushManager.registerPush(context, UserSpUtil.getUid(this) + "");
            if (UserSpUtil.getStatus(this) == 100) {
                uploadStep();
            }
        }
        leftDrawer.setOnItemClickListener(this);
        ctlMain.setOnTabSelectListener(this);
        mFragments.add(new HomeFragment());
        mFragments.add(new QuestionFragment());
        mFragments.add(new CommunityFragment());
        mFragments.add(new ExpertFragment());
        mFragments.add(new C_PersonalFragment());
        mFragments.add(new E_PersonalFragment());
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(mTitles.length);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        ctlMain.setTabData(mTabEntities);
        delayHandler = new Handler(this);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void uploadStep() {
        DbUtils.createDb(this);
        List<StepData> stepDatas = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{DateUtil.getTodayDate()});
        if (stepDatas == null || stepDatas.size() == 0) return;
        List<Step> steps = new ArrayList<>();
        for (int i = stepDatas.size() - 1; i >= 0; i--) {
            StepData stepData = stepDatas.get(i);
            if (!stepData.getStep().equals("0")) {
                steps.add(new Step(stepData.getToday(), stepData.getStep()));
                break;
            }
        }
        if (steps.size() == 0) return;
        Log.d(TAG, steps.toString());
        Param param = new Param(Constant.STEP);
        param.addRequstParams("uid", UserSpUtil.getUid(this));
        param.addRequstParams("steps", Encrpt.encryptStr(GsonTool.toJson(steps)));
        param.addToken();
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {

            }

            @Override
            public void err(HttpResult httpresult) {

            }
        });
    }

    public void showDrawer(View view) {
        drawerLayout.openDrawer(leftDrawer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, StepService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastTool.show(this, "再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            moveTaskToBack(true);
            super.onBackPressed();
        }
    }

    @Subscribe
    public void onLogoutEvent(LogoutEvent event) {
        showPage(0);
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Subscribe
    public void onLoginEvent(LoginEvent event) {
        Log.d(TAG, "isLogin: " + event.isLogin());
        if (event.isLogin()) {
            mFragments.set(1, QuestionFragment.newInstance());
            mFragments.set(2, CommunityFragment.newInstance());
            mFragments.set(3, ExpertFragment.newInstance());
            pagerAdapter.notifyDataSetChanged();
            ctlMain.setCurrentTab(4);
            int role = UserSpUtil.getStatus(MainActivity.this);
            if (role == 100) {
                mViewPager.setCurrentItem(4, false);
            } else {
                mViewPager.setCurrentItem(5, false);
            }
        } else if (ctlMain.getCurrentTab() == 4) {
            showPage(0);
        }
    }

    @Subscribe
    public void onAskExpertEvent(AskExpertEvent expertEvent) {
        showPage(3);
    }

    private void showPage(int page) {
        ctlMain.setCurrentTab(page);
        mViewPager.setCurrentItem(page, false);
    }

    /**
     * 问答推送
     *
     * @param event
     */
    @Subscribe(sticky = true)
    public void onMsgEvent(MsgEvent event) {
        Intent intent = new Intent(MainActivity.this, QuesDetActivity.class);
        intent.putExtra(Constant.KEY_1, event.getId());
        intent.putExtra(Constant.KEY_2, false);
        startActivity(intent);
    }

    @Subscribe(sticky = true)
    public void onStepUpStandardEvent(StepUpStandardEvent event) {
        showPage(4);
    }

    /**
     * 云医疗找专家
     *
     * @param event
     */
    @Subscribe
    public void onFindExpertEvent(FindExpertEvent event) {
        showPage(3);
    }

    @Subscribe
    public void onCommCateLoadedEvent(CommCateLoadedEvent event) {
        ArrayAdapter<Category> commCate = new ArrayAdapter<>(MainActivity.this, R.layout.list_item_drawer_menu, event.getCategories());
        leftDrawer.setAdapter(commCate);
        leftDrawer.setItemChecked(0, true);
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                messenger = new Messenger(service);
                Message msg = Message.obtain(null, com.tianxiabuyi.sports_medicine.pedometer.config.Constant.MSG_FROM_CLIENT);
                msg.replyTo = mGetReplyMessenger;
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case com.tianxiabuyi.sports_medicine.pedometer.config.Constant.MSG_FROM_SERVER:
                if (mFragments.get(4) != null && mFragments.get(4).isAdded()) {
                    // 更新界面上的步数
                    EventBus.getDefault().post(new StepEvent(message.getData().getInt("step")));
                }
                delayHandler.sendEmptyMessageDelayed(com.tianxiabuyi.sports_medicine.pedometer.config.Constant.REQUEST_SERVER, TIME_INTERVAL);
                break;
            case com.tianxiabuyi.sports_medicine.pedometer.config.Constant.REQUEST_SERVER:
                try {
                    Message msg1 = Message.obtain(null, com.tianxiabuyi.sports_medicine.pedometer.config.Constant.MSG_FROM_CLIENT);
                    msg1.replyTo = mGetReplyMessenger;
                    messenger.send(msg1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Category category = (Category) parent.getItemAtPosition(position);
        ((CommunityFragment) mFragments.get(2)).setItem(position, category.getName());
        drawerLayout.closeDrawer(leftDrawer);
    }

    @Override
    public void onTabSelect(int position) {
        if (position == 2) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        if (position == 4) {
            if (UserSpUtil.isLogin(MainActivity.this)) {
                int role = UserSpUtil.getStatus(MainActivity.this);
                if (role == 100) {
                    mViewPager.setCurrentItem(position, false);
                } else {
                    mViewPager.setCurrentItem(position + 1, false);
                }
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        } else {

            mViewPager.setCurrentItem(position, false);
        }
    }

    @Override
    public void onTabReselect(int position) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拍照、选择相册、裁剪返回
        if (requestCode == Constant.TAKE_PHOTO || requestCode == Constant.CHOOSE_PICTURE
                || requestCode == Constant.CROP_PICTURE && resultCode == RESULT_OK) {
            mFragments.get(4).onActivityResult(requestCode, resultCode, data);
        }
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof CommunityFragment || object instanceof QuestionFragment || object instanceof ExpertFragment) {
                return PagerAdapter.POSITION_NONE;
            }
            return super.getItemPosition(object);
        }
    }
}
