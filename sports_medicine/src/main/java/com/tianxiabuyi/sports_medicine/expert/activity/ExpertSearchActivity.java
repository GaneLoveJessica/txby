package com.tianxiabuyi.sports_medicine.expert.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.eeesys.frame.view.CleanableEditText;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.MyLinearLayoutManager;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.expert.adapter.EHistoryAdapter;
import com.tianxiabuyi.sports_medicine.expert.adapter.ExpertAdapter;
import com.tianxiabuyi.sports_medicine.expert.util.HistoryDbUtil;
import com.tianxiabuyi.sports_medicine.login.activity.LoginActivity;
import com.tianxiabuyi.sports_medicine.model.Expert;
import com.tianxiabuyi.sports_medicine.model.ExpertHistory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExpertSearchActivity extends BaseActivity implements TextView.OnEditorActionListener, TextWatcher, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @Bind(R.id.et_search)
    CleanableEditText etSearch;
    @Bind(R.id.rv_result)
    RecyclerView rvResult;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.rv_history)
    RecyclerView rvHistory;
    private int currentPage;
    private int totalPage;
    private ExpertAdapter adapter;
    private EHistoryAdapter historyAdapter;
    private View footView;
    private View emptyView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_expert;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(this);
        etSearch.setOnEditorActionListener(this);
        etSearch.addTextChangedListener(this);
        initHistory();
        initResult();
        freshHistory();
    }

    private void initHistory() {
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(new MyLinearLayoutManager(this));
        rvHistory.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvHistory.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                rvResult.setVisibility(View.VISIBLE);
                swipeRefresh.setVisibility(View.VISIBLE);
                rvHistory.setVisibility(View.GONE);
                ExpertHistory history = (ExpertHistory) baseQuickAdapter.getItem(i);
                etSearch.setText(history.getContent());
                swipeRefresh.setRefreshing(true);
                onRefresh();
            }
        });
        rvHistory.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                ExpertHistory expertHistory = (ExpertHistory) baseQuickAdapter.getItem(i);
                HistoryDbUtil.getInstance().deleteById(expertHistory.getId());
                freshHistory();
            }
        });
        footView = getLayoutInflater().inflate(R.layout.list_foot_history, (ViewGroup) rvHistory.getParent(), false);
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyAdapter.removeFooterView(footView);
                HistoryDbUtil.getInstance().clear();
                freshHistory();
            }
        });
        historyAdapter = new EHistoryAdapter(new ArrayList<ExpertHistory>());
        rvHistory.setAdapter(historyAdapter);
    }

    private void initResult() {
        rvResult.setHasFixedSize(true);
        rvResult.setLayoutManager(new MyLinearLayoutManager(this));
        rvResult.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(ExpertSearchActivity.this, ExpertDetailActivity.class);
                Expert expert = (Expert) baseQuickAdapter.getItem(i);
                intent.putExtra(Constant.KEY_1, expert);
                startActivity(intent);
            }
        });
        rvResult.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (UserSpUtil.isLogin(ExpertSearchActivity.this)) {
                    Expert expert = (Expert) baseQuickAdapter.getItem(i);
                    TextView tvLoveNumber = (TextView) view.findViewById(R.id.tv_love_number);
                    ImageView ivLove = (ImageView) view.findViewById(R.id.iv_love);
                    if (expert.getIs_loved() == 1) {
                        ivLove.setImageResource(R.mipmap.heart);
                        ivLove.startAnimation(AnimationUtils.loadAnimation(ExpertSearchActivity.this, R.anim.love_anim));
                        tvLoveNumber.setText(TextUtils.concat(expert.getLove() - 1+""));
                        cancelPraise(expert);
                    } else {
                        ivLove.setImageResource(R.mipmap.heart_red);
                        ivLove.startAnimation(AnimationUtils.loadAnimation(ExpertSearchActivity.this, R.anim.love_anim));
                        tvLoveNumber.setText(expert.getLove() + 1+"");
                        toPraise(expert);
                    }
                } else {
                    startActivity(new Intent(ExpertSearchActivity.this, LoginActivity.class));
                }
            }
        });
        emptyView = getLayoutInflater().inflate(R.layout.base_empty, (ViewGroup) rvResult.getParent(), false);
        adapter = new ExpertAdapter(new ArrayList<Expert>());
        rvResult.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);
    }

    @OnClick(R.id.tv_back)
    public void onClick() {
        finish();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (getText(etSearch).length() == 0) {
                ToastTool.show(this, "请输入搜索内容");
            } else {
                rvResult.setVisibility(View.VISIBLE);
                swipeRefresh.setVisibility(View.VISIBLE);
                rvHistory.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(true);
                onRefresh();
                HistoryDbUtil.getInstance().save(getText(etSearch));
            }
            return true;
        }
        return false;
    }

    private void loadResult(final boolean isFresh, String text) {
        hideSoftKeyboard();
        Param param = new Param(Constant.EXPERT);
        param.addRequstParams("uid", UserSpUtil.getUid(this));
        if (!isFresh) {
            param.addRequstParams("page", currentPage + 1);
        }
        param.addRequstParams("content", text);
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                adapter.setEnableLoadMore(true);
                swipeRefresh.setRefreshing(false);
                List<Expert> experts = httpresult.getListResult("data", new TypeToken<List<Expert>>() {
                });
                currentPage = httpresult.getNumber("page");
                totalPage = httpresult.getNumber("page_count");
                if (currentPage == 1) {
                    adapter.setNewData(experts);
                } else if (currentPage > 1) {
                    adapter.addData(experts);
                } else {
                    adapter.setEmptyView(emptyView);
                }
                if (totalPage == 1){
                    adapter.loadMoreEnd(true);
                }else if (currentPage == totalPage){
                    adapter.loadMoreEnd();
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(ExpertSearchActivity.this, httpresult.getMsg());
                swipeRefresh.setRefreshing(false);
                adapter.setEnableLoadMore(true);
                adapter.setNewData(null);
                adapter.setEmptyView(emptyView);
                rvResult.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.loadMoreFail();
                    }
                }, 300);
            }
        });
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            rvHistory.setVisibility(View.VISIBLE);
            freshHistory();
        }
    }

    public void freshHistory() {
        List<ExpertHistory> list = HistoryDbUtil.getInstance().queryAll();
        if (list != null) {
            if (historyAdapter.getFooterLayoutCount() == 0) {
                historyAdapter.addFooterView(footView);
            }
            historyAdapter.setNewData(list);
        }
        if (list == null || list.size() == 0) {
            historyAdapter.removeFooterView(footView);
        }
    }

    @Override
    public void onRefresh() {
        adapter.setEnableLoadMore(false);
        loadResult(true, getText(etSearch));
    }

    @Override
    public void onLoadMoreRequested() {
        loadResult(false, getText(etSearch));
    }

    private void cancelPraise(final Expert expert) {
        Param param = new Param(Constant.CANCEL_PRAISE);
        param.addToken();
        param.addRequstParams("id", expert.getLoved_id());
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                try {
                    JSONObject jsonObject = new JSONObject(httpresult.getResult());
                    expert.setLoved_id(jsonObject.getLong("id"));
                    expert.setLove(expert.getLove() - 1);
                    expert.setIs_loved(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void err(HttpResult httpresult) {
            }
        });
    }

    private void toPraise(final Expert expert) {
        Param param = new Param(Constant.PRAISE);
        param.addToken();
        param.addRequstParams("oid", expert.getId());
        param.addRequstParams("category", 3);
        param.addRequstParams("operate", 3);
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                try {
                    JSONObject jsonObject = new JSONObject(httpresult.getResult());
                    expert.setLoved_id(jsonObject.getLong("id"));
                    expert.setLove(expert.getLove() + 1);
                    expert.setIs_loved(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void err(HttpResult httpresult) {
            }
        });
    }
}
