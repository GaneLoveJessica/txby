package com.tianxiabuyi.sports_medicine.preach.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.MyLinearLayoutManager;
import com.tianxiabuyi.sports_medicine.common.fragment.LazyFragment;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.CacheUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.activity.LoginActivity;
import com.tianxiabuyi.sports_medicine.model.NewsImage;
import com.tianxiabuyi.sports_medicine.model.Preach;
import com.tianxiabuyi.sports_medicine.preach.activity.PreachDetailActivity;
import com.tianxiabuyi.sports_medicine.preach.adapter.PreachAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreachTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreachTabFragment extends LazyFragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.rv_preach)
    RecyclerView mRecyclerView;
    private ConvenientBanner mBanner;
    private List<Preach> bannerList = new ArrayList<>();
    private PreachAdapter adapter;
    private int id;
    private View emptyView;
    private View headView;

    public static PreachTabFragment newInstance(int id) {
        PreachTabFragment fragment = new PreachTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_1, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(Constant.KEY_1);
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity()));
        initAdapter();
        addHeadView();
        initCache();
        return view;
    }

    private void addHeadView() {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.list_head_preach, (ViewGroup) mRecyclerView.getParent(), false);
        TextView blank = (TextView) headView.findViewById(R.id.blank);
        blank.setVisibility(View.VISIBLE);
        headView.setVisibility(View.INVISIBLE);
        initBanner(headView);
        adapter.addHeaderView(headView);
    }

    private void initAdapter() {
        adapter = new PreachAdapter(new ArrayList<Preach>());
        mRecyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getActivity(), PreachDetailActivity.class);
                intent.putExtra(Constant.KEY_1, (Preach) baseQuickAdapter.getItem(i));
                startActivity(intent);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (UserSpUtil.isLogin(getActivity())) {
                    Preach preach = (Preach) baseQuickAdapter.getItem(position);
                    TextView tvLoveNumber = (TextView) view.findViewById(R.id.tv_love_number);
                    ImageView ivLove = (ImageView) view.findViewById(R.id.iv_love);
                    if (preach.getIs_loved() == 1) {
                        ivLove.setImageResource(R.mipmap.heart);
                        ivLove.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.love_anim));
                        tvLoveNumber.setText(preach.getLove() - 1 + "次");
                        cancelPraise(preach);
                    } else {
                        ivLove.setImageResource(R.mipmap.heart_red);
                        ivLove.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.love_anim));
                        tvLoveNumber.setText(preach.getLove() + 1 + "次");
                        toPraise(preach);
                    }
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
        emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.base_empty, (ViewGroup) mRecyclerView.getParent(), false);
    }

    private void initCache() {
        List<Preach> preaches = CacheUtil.getPreach(getActivity(), CacheUtil.KEY_PREACH + id);
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

    private void initBanner(View headView) {
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
        mBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, bannerList)
                .setPageIndicator(new int[]{R.mipmap.icon_point_gray, R.mipmap.icon_point_gray_selected})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        mBanner.setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), PreachDetailActivity.class);
                intent.putExtra(Constant.KEY_1, bannerList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        refreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        adapter.setEnableLoadMore(false);
        loadData(true);
    }

    @Override
    public void onLoadMoreRequested() {
        loadData(false);
    }

    private void loadData(final boolean isRefresh) {
        Param param = new Param(Constant.NEWS_LIST);
        param.addRequstParams("category", id);
        param.addRequstParams("uid", UserSpUtil.getUid(getActivity()));
        if (!isRefresh) {
            param.addRequstParams("max_id", adapter.getItem(adapter.getData().size() - 1).getNews_id());
        }
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                adapter.setEnableLoadMore(true);
                List<Preach> preachList = httpresult.getListResult("news", new TypeToken<List<Preach>>() {
                });
                headView.setVisibility(View.VISIBLE);
                if (isRefresh) {
                    CacheUtil.saveCache(getActivity(), CacheUtil.KEY_PREACH + id, preachList);
                    refreshLayout.setRefreshing(false);
                    bannerList.clear();
                    if (preachList.size() > 3) {
                        bannerList.addAll(preachList.subList(0, 3));
                        mBanner.notifyDataSetChanged();
                        adapter.setNewData(preachList.subList(3, preachList.size()));
                    } else if (preachList.size() > 0) {
                        bannerList.addAll(preachList);
                        mBanner.notifyDataSetChanged();
                    } else {
                        adapter.setEmptyView(emptyView);
                    }
                } else {
                    adapter.addData(preachList);
                    adapter.loadMoreComplete();
                }
                if (preachList.size() < 20) {
                    if (adapter.getData().size() < 17) {
                        adapter.loadMoreEnd(true);
                    } else {
                        adapter.loadMoreEnd();
                    }
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                if (getActivity() == null) return;
                ToastTool.show(getContext(), httpresult.getMsg());
                if (isRefresh) {
                    adapter.setEnableLoadMore(true);
                    refreshLayout.setRefreshing(false);
                    adapter.setEmptyView(emptyView);
                } else {
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.loadMoreFail();
                        }
                    }, 300);
                }
            }
        });
    }

    private void toPraise(final Preach preach) {
        Param param = new Param(Constant.PRAISE);
        param.addToken();
        param.addRequstParams("oid", preach.getId());
        param.addRequstParams("category", 1);
        param.addRequstParams("operate", 3);
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                try {
                    JSONObject jsonObject = new JSONObject(httpresult.getResult());
                    preach.setLoved_id(jsonObject.getLong("id"));
                    preach.setIs_loved(1);
                    preach.setLove(preach.getLove() + 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void err(HttpResult httpresult) {

            }
        });
    }

    private void cancelPraise(final Preach preach) {
        Param param = new Param(Constant.CANCEL_PRAISE);
        param.addToken();
        param.addRequstParams("id", preach.getLoved_id());
        param.setIsShowLoading(false);
        param.addRequstParams("category", 1);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(httpresult.getResult());
                    preach.setLoved_id(jsonObject.getLong("id"));
                    preach.setIs_loved(0);
                    preach.setLove(preach.getLove() - 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void err(HttpResult httpresult) {

            }
        });
    }

    public class LocalImageHolderView implements Holder<Preach> {
        private LocalImageHolderView.ViewHolder holder;

        @Override
        public View createView(Context context) {
            holder = new LocalImageHolderView.ViewHolder();
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.banner_preach, null);
            holder.ivImage = (ImageView) view.findViewById(R.id.iv_image);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            return view;
        }

        @Override
        public void UpdateUI(Context context, final int position, Preach preach) {
            List<NewsImage> images = preach.getImg();
            if (images != null && images.size() > 0) {
                Glide.with(context)
                        .load(images.get(0).getSrc())
                        .placeholder(R.mipmap.loading)
                        .error(R.mipmap.banner1)
                        .into(holder.ivImage);
            } else {
                holder.ivImage.setImageResource(R.mipmap.banner1);
            }
            holder.tvTitle.setText(preach.getTitle());
        }

        class ViewHolder {
            ImageView ivImage;
            TextView tvTitle;
        }
    }
}
