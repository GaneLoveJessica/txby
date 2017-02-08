package com.tianxiabuyi.sports_medicine.expert.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.google.gson.reflect.TypeToken;
import com.lzy.widget.HeaderScrollHelper;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseListFragment;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.expert.adapter.ArticleAdapter2;
import com.tianxiabuyi.sports_medicine.model.Preach;
import com.tianxiabuyi.sports_medicine.preach.activity.PreachDetailActivity;

import java.util.List;

/**
 * Created by Admirator on 2016/10/17.
 */

public class MyArticleFragment extends BaseListFragment<Preach> implements HeaderScrollHelper.ScrollableContainer {
    private String expertName;

    public static MyArticleFragment newInstance(String user_name) {
        MyArticleFragment fragment = new MyArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_1, user_name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View getScrollableView() {
        return baseRecyclerView;
    }

    @Override
    protected void init() {
        expertName = getArguments().getString(Constant.KEY_1);
        addDivider(R.drawable.divider_my_article);
    }

    @Override
    protected BaseQuickAdapter<Preach, BaseViewHolder> getAdapter(List<Preach> baseList) {
        return new ArticleAdapter2(baseList);
    }

    @Override
    protected Param getRequestParam() {
        final Param param = new Param(Constant.ARTICLE_LIST);
        param.addRequstParams("author", expertName);
        return param;
    }

    @Override
    protected List<Preach> getResult(HttpResult httpresult) {
        List<Preach> preachList = httpresult.getListResult("news", new TypeToken<List<Preach>>() {
        });
        if (preachList.size() == 0) {
            Preach preach = new Preach();
            preach.setTitle("没有文章信息");
            preachList.add(preach);
        }else{
            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    Intent intent = new Intent(getActivity(), PreachDetailActivity.class);
                    intent.putExtra(Constant.KEY_1, (Preach) baseQuickAdapter.getItem(i));
                    startActivity(intent);
                }
            });
        }
        return preachList;
    }

    @Override
    protected void onError(HttpResult httpresult) {
        ToastTool.show(getContext(), httpresult.getMsg());
    }
}
