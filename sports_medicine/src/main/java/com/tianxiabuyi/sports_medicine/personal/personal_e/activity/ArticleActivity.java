package com.tianxiabuyi.sports_medicine.personal.personal_e.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.model.Preach;
import com.tianxiabuyi.sports_medicine.personal.personal_e.event.PublishEvent;
import com.tianxiabuyi.sports_medicine.personal.personal_e.adapter.MyArticleAdapter;
import com.tianxiabuyi.sports_medicine.preach.activity.PreachDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class ArticleActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView lvArticle;
    private TextView tvTotalArticle;
    private TextView tvRanking;

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article;
    }

    @Override
    protected void initContentView() {
        EventBus.getDefault().register(this);
        lvArticle = (ListView) findViewById(R.id.lv_article);
        tvTotalArticle = (TextView) findViewById(R.id.tv_total_article);
        tvRanking = (TextView) findViewById(R.id.tv_ranking);
        lvArticle.setOnItemClickListener(this);
        loadData();
    }

    private void loadData() {
        Param param = new Param(Constant.ARTICLE_LIST);
        param.addRequstParams("author", UserSpUtil.getUsername(this));
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                List<Preach> articles = httpresult.getListResult("news", new TypeToken<List<Preach>>() {
                });
                tvTotalArticle.setText("文章总数 " + httpresult.getNumber("count"));
                tvRanking.setText("排名 " + httpresult.getNumber("order"));
                MyArticleAdapter adapter = new MyArticleAdapter(ArticleActivity.this, articles);
                lvArticle.setAdapter(adapter);
            }

            @Override
            public void err(HttpResult httpresult) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onPublishEvent(PublishEvent event) {
        loadData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PreachDetailActivity.class);
        Preach preach = (Preach) parent.getItemAtPosition(position);
        intent.putExtra(Constant.KEY_1, preach);
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }

    public void toPublish(View view) {
        startActivity(new Intent(this, PublishArticleActivity.class));
    }

}
