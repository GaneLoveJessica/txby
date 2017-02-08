package com.tianxiabuyi.sports_medicine.question.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.eeesys.frame.utils.ToastTool;
import com.eeesys.frame.view.CleanableEditText;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseAdapter;
import com.tianxiabuyi.sports_medicine.base.MyLinearLayoutManager;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.adapter.CommonFmPagerAdapter;
import com.tianxiabuyi.sports_medicine.model.Category;
import com.tianxiabuyi.sports_medicine.model.CommunityHistory;
import com.tianxiabuyi.sports_medicine.model.QuesHistory;
import com.tianxiabuyi.sports_medicine.question.adapter.CHistoryAdapter;
import com.tianxiabuyi.sports_medicine.question.adapter.QHistoryAdapter;
import com.tianxiabuyi.sports_medicine.question.fragment.CommunityTab;
import com.tianxiabuyi.sports_medicine.question.fragment.QuestionTab;
import com.tianxiabuyi.sports_medicine.question.util.CHistoryDbUtil;
import com.tianxiabuyi.sports_medicine.question.util.QHistoryDbUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 问答,社区搜索界面
 */
public class SearchActivity extends BaseActivity implements TextView.OnEditorActionListener,
        ViewPager.OnPageChangeListener, TextWatcher {
    @Bind(R.id.et_search)
    CleanableEditText etSearch;
    @Bind(R.id.tl_communicate)
    TabLayout tlCommunicate;
    @Bind(R.id.vp_search_result)
    ViewPager vpSearchResult;
    @Bind(R.id.rv_history)
    RecyclerView rvHistory;
    private List<Fragment> fragmentList = new ArrayList<>();
    private View footView;
    private BaseAdapter historyAdapter;
    private int from;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_ques;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        etSearch.setOnEditorActionListener(this);
        etSearch.addTextChangedListener(this);
        from = getIntent().getIntExtra(Constant.KEY_2, 0);
        initHistory();
        initViewPager();
        freshHistory();
    }

    private void initHistory() {
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(new MyLinearLayoutManager(this));
        rvHistory.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvHistory.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                hideSoftKeyboard();
                vpSearchResult.setVisibility(View.VISIBLE);
                rvHistory.setVisibility(View.GONE);
                if (from == 1) {
                    QuesHistory history = (QuesHistory) baseQuickAdapter.getItem(i);
                    etSearch.setText(history.getContent());
                    ((QuestionTab) fragmentList.get(0)).search(getText(etSearch));
                } else if (from == 2) {
                    CommunityHistory history = (CommunityHistory) baseQuickAdapter.getItem(i);
                    etSearch.setText(history.getContent());
                    ((CommunityTab) fragmentList.get(0)).search(getText(etSearch));
                }
            }
        });
        rvHistory.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (from == 1) {
                    QuesHistory quesHistory = (QuesHistory) baseQuickAdapter.getItem(i);
                    QHistoryDbUtil.getInstance().deleteById(quesHistory.getId());
                } else if (from == 2) {
                    CommunityHistory communityHistory = (CommunityHistory) baseQuickAdapter.getItem(i);
                    CHistoryDbUtil.getInstance().deleteById(communityHistory.getId());
                }
                freshHistory();
            }
        });
        footView = getLayoutInflater().inflate(R.layout.list_foot_history, (ViewGroup) rvHistory.getParent(), false);
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyAdapter.removeFooterView(footView);
                if (from == 1) {
                    QHistoryDbUtil.getInstance().clear();
                } else if (from == 2) {
                    CHistoryDbUtil.getInstance().clear();
                }
                freshHistory();
            }
        });
        if (from == 1) {
            historyAdapter = new QHistoryAdapter(new ArrayList<QuesHistory>());
        } else if (from == 2) {
            historyAdapter = new CHistoryAdapter(new ArrayList<CommunityHistory>());
        }
        rvHistory.setAdapter(historyAdapter);
    }

    private void initViewPager() {
        ArrayList<Category> categories = getIntent().getParcelableArrayListExtra(Constant.KEY_1);
        categories.add(0, new Category(0, "综合"));
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            if (from == 1) {
                fragmentList.add(QuestionTab.newInstance(categories.get(i).getId(),true));
            } else if (from == 2) {
                fragmentList.add(CommunityTab.newInstance(categories.get(i).getId(),true));
            }
            titles.add(categories.get(i).getName());
        }
        vpSearchResult.setOffscreenPageLimit(categories.size() - 1);
        CommonFmPagerAdapter adapter = new CommonFmPagerAdapter(getSupportFragmentManager(), titles, fragmentList);
        vpSearchResult.setAdapter(adapter);
        tlCommunicate.setupWithViewPager(vpSearchResult);
        vpSearchResult.addOnPageChangeListener(this);
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (getText(etSearch).length() == 0) {
                ToastTool.show(SearchActivity.this, "请输入搜索内容");
            } else {
                hideSoftKeyboard();
                vpSearchResult.setVisibility(View.VISIBLE);
                rvHistory.setVisibility(View.GONE);
                if (from == 1){
                    ((QuestionTab) fragmentList.get(vpSearchResult.getCurrentItem())).search(getText(etSearch));
                    QHistoryDbUtil.getInstance().save(getText(etSearch));
                }else if (from == 2){
                    ((CommunityTab) fragmentList.get(vpSearchResult.getCurrentItem())).search(getText(etSearch));
                    CHistoryDbUtil.getInstance().save(getText(etSearch));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (from == 1) {
            ((QuestionTab) fragmentList.get(position)).search(getText(etSearch));
        }else if (from == 2){
            ((CommunityTab) fragmentList.get(position)).search(getText(etSearch));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == 0) {
            rvHistory.setVisibility(View.VISIBLE);
            freshHistory();
        }
    }

    public void freshHistory() {
        if (from == 1) {
            List<QuesHistory> list = QHistoryDbUtil.getInstance().queryAll();
            if (list != null) {
                if (historyAdapter.getFooterLayoutCount() == 0) {
                    historyAdapter.addFooterView(footView);
                }
                historyAdapter.setNewData(list);
            }
            if (list == null || list.size() == 0) {
                historyAdapter.removeFooterView(footView);
            }
        }else if (from ==2){
            List<CommunityHistory> list = CHistoryDbUtil.getInstance().queryAll();
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
    }

    @OnClick(R.id.tv_back)
    public void onClick() {
        finish();
    }
}
