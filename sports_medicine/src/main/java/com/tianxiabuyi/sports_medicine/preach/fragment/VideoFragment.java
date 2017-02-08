package com.tianxiabuyi.sports_medicine.preach.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.MyGridLayoutManager;
import com.tianxiabuyi.sports_medicine.common.fragment.LazyFragment;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.CacheUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.model.Preach;
import com.tianxiabuyi.sports_medicine.preach.activity.YoukuPlayerActivity;
import com.tianxiabuyi.sports_medicine.preach.adapter.VideoAdapter;
import com.tianxiabuyi.sports_medicine.preach.event.VideoPraiseEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.rv_preach)
    RecyclerView rvPreach;
    private ConvenientBanner mBanner;
    private List<Preach> bannerList = new ArrayList<>();
    private VideoAdapter adapter;
    private int videoPos;
    private int bannerPos;
    private View emptyView;
    private View headView;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(int id) {
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_1, id);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), YoukuPlayerActivity.class);
        intent.putExtra(Constant.KEY_1, bannerList.get(position));
        this.bannerPos = position;
        videoPos = -1;
        startActivity(intent);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, view);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        rvPreach.setLayoutManager(new MyGridLayoutManager(getActivity(), 2));
        adapter = new VideoAdapter(new ArrayList<Preach>());
        rvPreach.setAdapter(adapter);
        headView = inflater.inflate(R.layout.list_head_preach, null);
        TextView blank = (TextView) headView.findViewById(R.id.blank);
        blank.setVisibility(View.VISIBLE);
        headView.setVisibility(View.INVISIBLE);
        adapter.addHeaderView(headView);
        adapter.setOnLoadMoreListener(this);
        rvPreach.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getActivity(), YoukuPlayerActivity.class);
                intent.putExtra(Constant.KEY_1, (Preach) baseQuickAdapter.getItem(i));
                videoPos = i;
                bannerPos = -1;
                startActivity(intent);
            }
        });
        mBanner = (ConvenientBanner) headView.findViewById(R.id.convenientBanner);
//         解决SwipRefreshLayout和ViewPager的滑动冲突
        mBanner.getViewPager().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        refreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        refreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
        mBanner.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        emptyView = inflater.inflate(R.layout.base_empty, (ViewGroup) rvPreach.getParent(), false);
        mBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, bannerList)
                .setPageIndicator(new int[]{R.mipmap.icon_point_gray, R.mipmap.icon_point_gray_selected})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        initCache();
        return view;
    }

    private void initCache() {
        List<Preach> preaches = CacheUtil.getPreach(getActivity(), CacheUtil.KEY_CLOUD_VIDEO);
        if (preaches != null && preaches.size() > 0) {
            headView.setVisibility(View.VISIBLE);
            if (preaches.size() > 3) {
                bannerList.addAll(preaches.subList(0, 3));
                mBanner.notifyDataSetChanged();
                adapter.setNewData(preaches.subList(3, preaches.size()));
                if (preaches.size() < 20) {
                    adapter.loadMoreEnd(true);
                }
            } else if (preaches.size() > 0) {
                bannerList.addAll(preaches);
                mBanner.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onVideoPraiseEvent(VideoPraiseEvent event) {
        if (bannerPos >= 0) {
            bannerList.set(bannerPos, event.getPreach());
            mBanner.notifyDataSetChanged();
        } else if (videoPos >= 0) {
            adapter.addData(videoPos, event.getPreach());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initData() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        loadData(false);
    }

    public class LocalImageHolderView implements Holder<Preach> {
        private ViewHolder holder;

        @Override
        public View createView(Context context) {
            holder = new ViewHolder();
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.banner_preach, null);
            holder.ivImage = (ImageView) view.findViewById(R.id.iv_image);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            holder.ivPlay = (ImageView) view.findViewById(R.id.iv_play);
            return view;
        }

        @Override
        public void UpdateUI(Context context, final int position, Preach preach) {
            Glide.with(context)
                    .load(preach.getJson().getThumb())
                    .placeholder(R.mipmap.loading)
                    .error(R.mipmap.banner1)
                    .into(holder.ivImage);
            holder.tvTitle.setText(preach.getTitle());
            holder.ivPlay.setVisibility(View.VISIBLE);
        }

        class ViewHolder {
            ImageView ivImage;
            TextView tvTitle;
            ImageView ivPlay;
        }
    }

    @Override
    public void onRefresh() {
        adapter.setEnableLoadMore(false);
        loadData(true);
    }

    private void loadData(final boolean isFresh) {
        Param param = new Param(Constant.NEWS_LIST);
        param.addRequstParams("category", getArguments().getInt(Constant.KEY_1));
        param.addRequstParams("uid", UserSpUtil.getUid(getActivity()));
        if (!isFresh) {
            param.addRequstParams("max_id", adapter.getItem(adapter.getData().size() - 1).getNews_id());
        }
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                adapter.setEnableLoadMore(true);
                List<Preach> news = httpresult.getListResult("news", new TypeToken<List<Preach>>() {
                });
                headView.setVisibility(View.VISIBLE);
                if (isFresh) {
                    CacheUtil.saveCache(getActivity(), CacheUtil.KEY_CLOUD_VIDEO, news);
                    refreshLayout.setRefreshing(false);
                    bannerList.clear();
                    if (news.size() > 3) {
                        bannerList.addAll(news.subList(0, 3));
                        mBanner.notifyDataSetChanged();
                        adapter.setNewData(news.subList(3, news.size()));
                    } else if (news.size() > 0) {
                        bannerList.addAll(news);
                        mBanner.notifyDataSetChanged();
                    } else {
                        adapter.setEmptyView(emptyView);
                    }
                } else {
                    adapter.addData(news);
                    adapter.loadMoreComplete();
                    if (news.size() < 20) {
                        if (adapter.getData().size() < 17) {
                            adapter.loadMoreEnd(true);
                        } else {
                            adapter.loadMoreEnd();
                        }
                    }
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                if (getActivity() == null) return;
                ToastTool.show(getContext(), httpresult.getMsg());
                adapter.setEnableLoadMore(true);
                refreshLayout.setRefreshing(false);
                adapter.setEmptyView(emptyView);
                rvPreach.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.loadMoreFail();
                    }
                }, 300);
            }
        });
    }
}
