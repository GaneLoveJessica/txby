package com.tianxiabuyi.sports_medicine.preach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseRefreshFragment;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.CacheUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.activity.LoginActivity;
import com.tianxiabuyi.sports_medicine.model.Preach;
import com.tianxiabuyi.sports_medicine.preach.activity.PreachDetailActivity;
import com.tianxiabuyi.sports_medicine.preach.adapter.CloudEssenceAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CloudEssenceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CloudEssenceFragment extends BaseRefreshFragment<Preach> {
    private String id;

    public static CloudEssenceFragment newInstance(int id) {
        CloudEssenceFragment fragment = new CloudEssenceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_1, id + "");
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void init() {
        id = getArguments().getString(Constant.KEY_1);
        setOnItemClickListener(new OnItemClickListener() {
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
                        tvLoveNumber.setText(preach.getLove() - 1 + "");
                        cancelPraise(preach);
                    } else {
                        ivLove.setImageResource(R.mipmap.heart_red);
                        ivLove.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.love_anim));
                        tvLoveNumber.setText(preach.getLove() + 1 + "");
                        toPraise(preach);
                    }
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
    }

    @Override
    protected List<Preach> getCache() {
        return CacheUtil.getPreach(getActivity(), CacheUtil.KEY_PREACH + id);
    }

    @Override
    protected BaseQuickAdapter<Preach, BaseViewHolder> getAdapter(List<Preach> baseList) {
        return new CloudEssenceAdapter(baseList);
    }

    @Override
    protected Param getRequestParam(Preach preach) {
        Param param = new Param(Constant.NEWS_LIST);
        param.addRequstParams("category", id);
        param.addRequstParams("uid", UserSpUtil.getUid(getActivity()));
        if (preach != null) {
            param.addRequstParams("max_id", preach.getNews_id());
        }
        return param;
    }

    @Override
    protected List<Preach> getResult(HttpResult httpresult, boolean isRefresh) {
        List<Preach> preachList = httpresult.getListResult("news", new TypeToken<List<Preach>>() {
        });
        if (isRefresh) {
            CacheUtil.saveCache(getActivity(), CacheUtil.KEY_PREACH + id, preachList);
        }
        return preachList;
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

}
