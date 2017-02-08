package com.tianxiabuyi.sports_medicine.personal.personal_c.activity;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.eeesys.frame.http.HttpResult;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseRefreshActivity;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.expert.activity.ExpertDetailActivity;
import com.tianxiabuyi.sports_medicine.model.Expert;
import com.tianxiabuyi.sports_medicine.personal.personal_c.adapter.MyExpertAdapter;

import java.util.List;

/**
 * 我的专家
 */
public class C_MyExpertActivity extends BaseRefreshActivity<Expert> {

    @Override
    protected void init() {
        title.setText(R.string.my_expert);
        setHasPage(true);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(C_MyExpertActivity.this, ExpertDetailActivity.class);
                intent.putExtra(Constant.KEY_1, (Expert)baseQuickAdapter.getItem(i));
                startActivity(intent);
            }
        });
    }

    @Override
    protected BaseQuickAdapter<Expert, BaseViewHolder> getAdapter(List<Expert> baseList) {
        return new MyExpertAdapter(baseList);
    }

    @Override
    protected Param getRequestParam(Expert expert) {
        Param param = new Param(Constant.MY_EXPERT);
        param.addToken();
        return param;
    }

    @Override
    protected List<Expert> getResult(boolean isRefresh, HttpResult httpresult) {
        return httpresult.getListResult("data", new TypeToken<List<Expert>>() {
        });
    }
}
