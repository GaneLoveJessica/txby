package com.tianxiabuyi.sports_medicine.expert.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
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
import com.tianxiabuyi.sports_medicine.expert.activity.ExpertDetailActivity;
import com.tianxiabuyi.sports_medicine.expert.activity.ExpertSearchActivity;
import com.tianxiabuyi.sports_medicine.expert.adapter.ExpertAdapter;
import com.tianxiabuyi.sports_medicine.login.activity.LoginActivity;
import com.tianxiabuyi.sports_medicine.model.Expert;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.OnClick;

import static com.youku.player.YoukuPlayerApplication.context;

public class ExpertFragment extends BaseRefreshFragment<Expert> {

    public static ExpertFragment newInstance() {
        return new ExpertFragment();
    }

    @Override
    protected void init() {
        setHasPage(true);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getActivity(), ExpertDetailActivity.class);
                Expert expert = (Expert) baseQuickAdapter.getItem(i);
                intent.putExtra(Constant.KEY_1, expert);
                startActivity(intent);
            }
        });
        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (UserSpUtil.isLogin(getActivity())) {
                    Expert expert = (Expert) baseQuickAdapter.getItem(i);
                    TextView tvLoveNumber = (TextView) view.findViewById(R.id.tv_love_number);
                    ImageView ivLove = (ImageView) view.findViewById(R.id.iv_love);
                    if (expert.getIs_loved() == 1) {
                        ivLove.setImageResource(R.mipmap.heart);
                        ivLove.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.love_anim));
                        tvLoveNumber.setText(expert.getLove() - 1+"");
                        cancelPraise(expert);
                    } else {
                        ivLove.setImageResource(R.mipmap.heart_red);
                        ivLove.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.love_anim));
                        tvLoveNumber.setText(expert.getLove() + 1+"");
                        toPraise(expert);
                    }
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
    }

    private void cancelPraise(final Expert expert) {
        Param param = new Param(Constant.CANCEL_PRAISE);
        param.addToken();
        param.addRequstParams("id", expert.getLoved_id());
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(context, param, new CommHttp.HttpBack() {
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
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_expert;
    }

    @Override
    protected List<Expert> getCache() {
        return CacheUtil.getExpert(getActivity(), CacheUtil.KEY_EXPERT);
    }

    @Override
    public void initData() {
        if (UserSpUtil.isFirstShowExpert(getActivity())) {
            showTip();
        }
        super.initData();
    }

    @Override
    protected BaseQuickAdapter<Expert, BaseViewHolder> getAdapter(List<Expert> baseList) {
        return new ExpertAdapter(baseList);
    }

    @Override
    protected Param getRequestParam(Expert expert) {
        Param param = new Param(Constant.EXPERT);
        param.addRequstParams("uid", UserSpUtil.getUid(getActivity()));
        return param;
    }

    @Override
    protected List<Expert> getResult(HttpResult httpresult, boolean isRefresh) {
        List<Expert> expertList = httpresult.getListResult("data", new TypeToken<List<Expert>>() {
        });
        if (isRefresh) {
            CacheUtil.saveCache(getActivity(), CacheUtil.KEY_EXPERT, expertList);
        }
        return expertList;
    }

    private void showTip() {
        final Dialog dialog = new Dialog(getActivity(), R.style.Dialog_Fullscreen);
        dialog.setContentView(R.layout.dialog_tip_expert);
        ImageView iv = (ImageView) dialog.findViewById(R.id.iv_yes);
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UserSpUtil.setFirstShowExpert(getActivity(), false);
            }
        });
        dialog.show();
    }

    @OnClick(R.id.iv_search)
    public void onClick() {
        startActivity(new Intent(getActivity(), ExpertSearchActivity.class));
    }
}
