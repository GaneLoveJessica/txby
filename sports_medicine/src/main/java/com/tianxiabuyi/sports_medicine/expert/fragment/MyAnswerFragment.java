package com.tianxiabuyi.sports_medicine.expert.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.eeesys.frame.view.ScrollViewListView;
import com.google.gson.reflect.TypeToken;
import com.lzy.widget.HeaderScrollHelper;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.fragment.LazyFragment;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.expert.adapter.ArticleAdapter;
import com.tianxiabuyi.sports_medicine.expert.adapter.LatestAnswerAdapter;
import com.tianxiabuyi.sports_medicine.model.Preach;
import com.tianxiabuyi.sports_medicine.model.Question;
import com.tianxiabuyi.sports_medicine.preach.activity.PreachDetailActivity;
import com.tianxiabuyi.sports_medicine.question.activity.QuesDetActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/17.
 */

public class MyAnswerFragment extends LazyFragment implements HeaderScrollHelper.ScrollableContainer {
    @Bind(R.id.ll_latest_answer)
    ScrollViewListView lvAnswer;
    @Bind(R.id.ll_article)
    ScrollViewListView lvArticle;
    private int expertId;
    private String expertName;
    private View view;

    public static MyAnswerFragment newInstance(int id, String user_name) {
        MyAnswerFragment fragment = new MyAnswerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_1, id);
        bundle.putString(Constant.KEY_2, user_name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            expertId = getArguments().getInt(Constant.KEY_1);
            expertName = getArguments().getString(Constant.KEY_2);
        }
    }

    private void loadArticle() {
        final Param param = new Param(Constant.ARTICLE_LIST);
        param.addRequstParams("author", expertName);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                List<Preach> list = httpresult.getListResult("news", new TypeToken<List<Preach>>() {
                });
                if (list.size() == 0) {
                    Preach preach = new Preach();
                    preach.setTitle("没有文章信息");
                    list.add(preach);
                    lvArticle.setEnabled(false);
                }
                ArticleAdapter adapter = new ArticleAdapter(getActivity(), list);
                lvArticle.setAdapter(adapter);
            }

            @Override
            public void err(HttpResult httpresult) {
            }
        });
    }

    private void loadAnswer() {
        Param param = new Param(Constant.MY_ANSWERED);
        param.addRequstParams("uid", expertId);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                List<Question> list = httpresult.getListResult("quests", new TypeToken<List<Question>>() {
                });
                if (list.size() == 0) {
                    Question question = new Question();
                    question.setContent("没有解答信息");
                    list.add(question);
                    lvAnswer.setEnabled(false);
                }
                LatestAnswerAdapter latestAnswerAdapter = new LatestAnswerAdapter(getActivity(), list);
                lvAnswer.setAdapter(latestAnswerAdapter);
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(getContext(), httpresult.getMsg());
            }
        });
    }

    @Override
    public View getScrollableView() {
        return view;
    }

    @Override
    protected View initViews(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_answer, container, false);
        ButterKnife.bind(this, view);
        lvAnswer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Question question = (Question) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), QuesDetActivity.class);
                intent.putExtra(Constant.KEY_1, question.getId() + "");
                intent.putExtra(Constant.KEY_2, false);
                startActivity(intent);
            }
        });
        lvArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Preach preach = (Preach) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), PreachDetailActivity.class);
                intent.putExtra(Constant.KEY_1, preach);
                intent.putExtra(Constant.KEY_2, 4);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    protected void initData() {
        loadAnswer();
        loadArticle();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
