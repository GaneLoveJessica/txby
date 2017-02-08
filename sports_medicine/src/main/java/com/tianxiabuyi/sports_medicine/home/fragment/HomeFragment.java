package com.tianxiabuyi.sports_medicine.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.eeesys.frame.view.ScrollViewListView;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.fragment.LazyFragment;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.CacheUtil;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.common.view.HorizontalListView;
import com.tianxiabuyi.sports_medicine.common.view.MyGridView;
import com.tianxiabuyi.sports_medicine.expert.activity.ExpertDetailActivity;
import com.tianxiabuyi.sports_medicine.home.adapter.HomeDoctorAdapter;
import com.tianxiabuyi.sports_medicine.home.adapter.HomeLineBaseAdapter;
import com.tianxiabuyi.sports_medicine.home.adapter.HomeMenuAdapter;
import com.tianxiabuyi.sports_medicine.home.adapter.HomeNewsAdapter;
import com.tianxiabuyi.sports_medicine.home.adapter.HomeVideoAdapter;
import com.tianxiabuyi.sports_medicine.model.Expert;
import com.tianxiabuyi.sports_medicine.model.Preach;
import com.tianxiabuyi.sports_medicine.preach.activity.CloudEssenceActivity;
import com.tianxiabuyi.sports_medicine.preach.activity.PreachActivity;
import com.tianxiabuyi.sports_medicine.preach.activity.PreachDetailActivity;
import com.tianxiabuyi.sports_medicine.preach.activity.YoukuPlayerActivity;
import com.tianxiabuyi.sports_medicine.preach.event.VideoPraiseEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tianxiabuyi.sports_medicine.common.util.CacheUtil.KEY_CLOUD_EXPERT;

/**
 * 首页
 */
public class HomeFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.gv_home_menu)
    MyGridView gvHomeMenu;
    @Bind(R.id.gv_line_base)
    MyGridView gvLineBase;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.gv_video)
    MyGridView gvVideo;
    @Bind(R.id.lv_news)
    ScrollViewListView lvNews;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.hlv_expert)
    HorizontalListView hlvExpert;
    private String[] items = {"云荟萃", "云知识", "云康复", "云医疗"};
    private List<Preach> cloudBaseList = new ArrayList<>();
    private List<Preach> cloudVideoList = new ArrayList<>();
    private HomeLineBaseAdapter cloudBaseAdapter;
    private HomeVideoAdapter cloudVideoAdapter;
    private List<Expert> experts;
    private HomeDoctorAdapter expertAdapter;
    private long delayMillis = 2500;// 专家滚动时间间隔
    private int videoPos;
    private boolean isTouch;
    private boolean finishCloudLine = false;
    private boolean finishCloudVideo = false;
    private boolean finishCloudExpert = false;
    private boolean finishCloudDynamic = false;
    private int screenWidth;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        screenWidth = wm.getDefaultDisplay().getWidth();
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(this);
        gvHomeMenu.setAdapter(new HomeMenuAdapter(getActivity(), Arrays.asList(items)));
        gvHomeMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (position == 0) {
                    intent.setClass(getActivity(), CloudEssenceActivity.class);
                    intent.putExtra(Constant.KEY_1, "云荟萃");
                    intent.putExtra(Constant.KEY_2, 25);
                } else {
                    intent.setClass(getActivity(), PreachActivity.class);
                    intent.putExtra(Constant.KEY_1, position);
                    intent.putExtra(Constant.KEY_2, items[position]);
                }
                startActivity(intent);
            }
        });
        hlvExpert.setOnScrollStateChangedListener(new HorizontalListView.OnScrollStateChangedListener() {
            @Override
            public void onScrollStateChanged(ScrollState scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE://当屏幕停止滚动时
                        if (isTouch) {
                            isTouch = false;
                            handler.postDelayed(runnable, delayMillis);
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL://当用户在以触屏方式滚动屏幕并且手指仍然还在屏幕上时
                        break;
                    case SCROLL_STATE_FLING://当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                        isTouch = true;
                        handler.removeCallbacks(runnable);
                        break;
                }
            }
        });
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PreachDetailActivity.class);
                intent.putExtra(Constant.KEY_1, (Preach) parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });
        gvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), YoukuPlayerActivity.class);
                Preach preach = (Preach) parent.getItemAtPosition(position);
                intent.putExtra(Constant.KEY_1, preach);
                videoPos = position;
                startActivity(intent);
            }
        });
        gvLineBase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PreachDetailActivity.class);
                intent.putExtra(Constant.KEY_1, (Preach) parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });
        hlvExpert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ExpertDetailActivity.class);
                Expert expert = (Expert) parent.getItemAtPosition(position);
                intent.putExtra(Constant.KEY_1, expert);
                intent.putExtra(Constant.KEY_2, true);
                startActivity(intent);
            }
        });
        initBanner();
        List<Preach> preaches = CacheUtil.getPreach(getActivity(), CacheUtil.KEY_CLOUD_BASE);
        if (preaches != null && preaches.size() > 0) {
            setCloudBaseData(preaches);
        }
        List<Preach> videos = CacheUtil.getPreach(getActivity(), CacheUtil.KEY_CLOUD_VIDEO);
        if (videos != null && videos.size() > 0) {
            setCloudVideoData(videos);
        }
        experts = CacheUtil.getExpert(getActivity(), CacheUtil.KEY_CLOUD_EXPERT);
        if (experts != null && experts.size() > 0) {
            hlvExpert.setAdapter(new HomeDoctorAdapter(getActivity(), experts));
        }
        List<Preach> dynamics = CacheUtil.getPreach(getActivity(), CacheUtil.KEY_CLOUD_DYNAMIC);
        if (dynamics != null && dynamics.size() > 0) {
            if (dynamics.size() < 5) {
                lvNews.setAdapter(new HomeNewsAdapter(getActivity(), dynamics));
            } else {
                lvNews.setAdapter(new HomeNewsAdapter(getActivity(), dynamics.subList(0, 5)));
            }
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onVideoPraiseEvent(VideoPraiseEvent event) {
        if (videoPos >= 0) {
            cloudVideoList.set(videoPos, event.getPreach());
            cloudVideoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initData() {
        loadData();
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delayMillis);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (experts == null) {// 可能还在请求专家数据，继续发送消息
                handler.postDelayed(this, delayMillis);
                return;
            } else if (experts.size() <= 4) {// 专家小等于4个不滚动
                return;
            }
            hlvExpert.scrollTo(screenWidth / 4 * (hlvExpert.getFirstVisiblePosition() + 1));
            handler.postDelayed(this, delayMillis);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(5000);
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delayMillis);
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
        handler.removeCallbacks(runnable);
    }

    public void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        loadLineBase(20);
        loadCloudVideos(6);
        loadDoctors(1);
        loadNews(2);
    }

    private void loadCloudVideos(final int id) {
        Param param = new Param(Constant.NEWS_LIST);
        param.addRequstParams("category", id);
        param.addRequstParams("uid", UserSpUtil.getUid(getActivity()));
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                stopRefresh(2);
                List<Preach> videos = httpresult.getListResult("news", new TypeToken<List<Preach>>() {
                });
                CacheUtil.saveCache(getActivity(), CacheUtil.KEY_CLOUD_VIDEO, videos);
                setCloudVideoData(videos);
            }

            @Override
            public void err(HttpResult httpresult) {
                stopRefresh(2);
            }
        });
    }

    private void stopRefresh(int i) {
        if (i == 1) {
            finishCloudLine = true;
        } else if (i == 2) {
            finishCloudVideo = true;
        } else if (i == 3) {
            finishCloudExpert = true;
        } else if (i == 4) {
            finishCloudDynamic = true;
        }
        if (finishCloudDynamic && finishCloudExpert && finishCloudLine && finishCloudVideo) {
            swipeRefreshLayout.setRefreshing(false);
            finishCloudVideo = false;
            finishCloudLine = false;
            finishCloudExpert = false;
            finishCloudDynamic = false;
        }
    }

    private void setCloudVideoData(List<Preach> videos) {
        GlideUtil.setImage(getActivity(), ivImage, videos.get(0).getJson().getThumb());
        tvTitle.setText(videos.get(0).getTitle());
        int end = 0;
        if (videos.size() <= 5) {
            end = videos.size();
        } else {
            end = 5;
        }
        cloudVideoList.clear();
        cloudVideoList.addAll(videos.subList(1, end));
        if (cloudVideoAdapter == null) {
            cloudVideoAdapter = new HomeVideoAdapter(getActivity(), cloudVideoList);
            gvVideo.setAdapter(cloudVideoAdapter);
        } else {
            cloudVideoAdapter.notifyDataSetChanged();
        }
    }

    public void loadLineBase(final int id) {
        Param param = new Param(Constant.NEWS_LIST);
        param.addRequstParams("category", id);
        param.addRequstParams("uid", UserSpUtil.getUid(getActivity()));
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                stopRefresh(1);
                List<Preach> preach = httpresult.getListResult("news", new TypeToken<List<Preach>>() {
                });
                CacheUtil.saveCache(getActivity(), CacheUtil.KEY_CLOUD_BASE, preach);
                setCloudBaseData(preach);
            }

            @Override
            public void err(HttpResult httpresult) {
                stopRefresh(1);
                ToastTool.show(getActivity(), httpresult.getMsg());
            }
        });
    }

    private void setCloudBaseData(List<Preach> preach) {
        int end = 3;
        if (preach.size() < 3) {
            end = preach.size();
        }
        cloudBaseList.clear();
        cloudBaseList.addAll(preach.subList(0, end));
        if (cloudBaseAdapter == null) {
            cloudBaseAdapter = new HomeLineBaseAdapter(getActivity(), cloudBaseList);
            gvLineBase.setAdapter(cloudBaseAdapter);
        } else {
            cloudBaseAdapter.notifyDataSetChanged();
        }
    }

    private void loadDoctors(int id) {
        Param param = new Param(Constant.EXPERT);
        param.addRequstParams("category", id);
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                stopRefresh(3);
                experts = httpresult.getListResult("data", new TypeToken<List<Expert>>() {
                });
                CacheUtil.saveCache(getActivity(), KEY_CLOUD_EXPERT, experts);
                expertAdapter = new HomeDoctorAdapter(getActivity(), experts);
                hlvExpert.setAdapter(expertAdapter);
            }

            @Override
            public void err(HttpResult httpresult) {
                stopRefresh(3);
            }
        });
    }

    static Handler handler = new Handler();

    /**
     * 请求新闻列表
     *
     * @param id
     */
    public void loadNews(final int id) {
        Param param = new Param(Constant.NEWS_LIST);
        param.addRequstParams("category", id);
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                stopRefresh(4);
                List<Preach> news = httpresult.getListResult("news", new TypeToken<List<Preach>>() {
                });
                CacheUtil.saveCache(getActivity(), CacheUtil.KEY_CLOUD_DYNAMIC, news);
                if (news.size() < 5) {
                    lvNews.setAdapter(new HomeNewsAdapter(getActivity(), news));
                } else {
                    lvNews.setAdapter(new HomeNewsAdapter(getActivity(), news.subList(0, 5)));
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                stopRefresh(4);
            }
        });
    }

    private void initBanner() {
        convenientBanner.getViewPager().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        swipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        swipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
        List<Integer> localImages = new ArrayList<>();
        localImages.add(R.mipmap.banner1);
        localImages.add(R.mipmap.banner2);
        localImages.add(R.mipmap.banner3);
        localImages.add(R.mipmap.banner4);
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.icon_point_yellow, R.mipmap.icon_point_yellow_selected})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_more_base, R.id.tv_more_video, R.id.fl_hot_video, R.id.tv_more_news})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_more_base:
                intent.setClass(getActivity(), CloudEssenceActivity.class);
                intent.putExtra(Constant.KEY_1, "云基地");
                intent.putExtra(Constant.KEY_2, 20);
                break;
            case R.id.tv_more_video:
                intent.setClass(getActivity(), PreachActivity.class);
                intent.putExtra(Constant.KEY_1, 1);
                intent.putExtra(Constant.KEY_2, items[1]);
                intent.putExtra(Constant.KEY_3, true);
                break;
            case R.id.fl_hot_video:
                intent.setClass(getActivity(), YoukuPlayerActivity.class);
                intent.putExtra(Constant.KEY_1, cloudVideoList.get(0));
                videoPos = 0;
                break;
            case R.id.tv_more_news:
                intent.setClass(getActivity(), CloudEssenceActivity.class);
                intent.putExtra(Constant.KEY_1, "云动态");
                intent.putExtra(Constant.KEY_2, 2);
                break;
        }
        startActivity(intent);

    }


    public class LocalImageHolderView implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {
            imageView.setImageResource(data);
        }
    }
}
