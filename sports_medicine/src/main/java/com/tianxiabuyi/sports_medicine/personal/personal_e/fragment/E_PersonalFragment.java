package com.tianxiabuyi.sports_medicine.personal.personal_e.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.fragment.LazyFragment;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.model.Info;
import com.tianxiabuyi.sports_medicine.personal.DataActivity;
import com.tianxiabuyi.sports_medicine.personal.MyPointActivity;
import com.tianxiabuyi.sports_medicine.personal.personal_e.activity.ArticleActivity;
import com.tianxiabuyi.sports_medicine.personal.personal_e.activity.E_MyAnswerActivity;
import com.tianxiabuyi.sports_medicine.personal.personal_e.activity.E_MyPatientActivity;
import com.tianxiabuyi.sports_medicine.personal.personal_e.adapter.EPersonalMenuAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 专家个人中心
 */
public class E_PersonalFragment extends LazyFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.tv_total_points)
    TextView tvTotalPoints;
    @Bind(R.id.civ_avatar)
    CircleImageView civAvatar;
    @Bind(R.id.tv_total_answer)
    TextView tvTotalAnswer;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.gv_e_personal_center)
    GridView gvEPersonalCenter;
    private List<Info> list = new ArrayList<>();
    private EPersonalMenuAdapter adapter;
    private int isSign;
    private int score;

    public E_PersonalFragment() {
        // Required empty public constructor
    }

    public static E_PersonalFragment newInstance() {
        return new E_PersonalFragment();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_e__personal, container, false);
        ButterKnife.bind(this, view);
        list.add(new Info("我的解答", R.mipmap.icon_e_person_answer));
        list.add(new Info("文章发布", R.mipmap.icon_e_person_artical));
        list.add(new Info("个人资料", R.mipmap.icon_e_person_info));
        list.add(new Info("每日签到", R.mipmap.icon_e_person_unsign_in));
        list.add(new Info("我的用户", R.mipmap.icon_e_person_my_patient));
        list.add(new Info("我的积分", R.mipmap.icon_e_person_my_point));
        adapter = new EPersonalMenuAdapter(getActivity(), list);
        gvEPersonalCenter.setAdapter(adapter);
        gvEPersonalCenter.setOnItemClickListener(this);
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        setData();
    }

    private void setData() {
        if (UserSpUtil.isLogin(getActivity()) && UserSpUtil.getStatus(getActivity()) == 200){
            loadPoints();
            String avatar = UserSpUtil.getUser(getActivity()).getAvatar();
            if (avatar == null || avatar.equals("http://image.eeesys.com/default/doctor_m.png")){
                civAvatar.setImageResource(R.mipmap.avatar);
            }else {
                GlideUtil.setAvatar(getActivity(), civAvatar, UserSpUtil.getUser(getActivity()).getAvatar());
            }
            tvUsername.setText(UserSpUtil.getUsername(getActivity()));
        }
    }

    /**
     * 查询总积分和答题数
     */
    private void loadPoints() {
        Param param = new Param(Constant.TOTAL_POINT);
        param.addToken();
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                try {
                    JSONObject jsonObject = new JSONObject(httpresult.getResult());
                    tvTotalAnswer.setText(TextUtils.concat("答题数\n", jsonObject.optInt("answer") + ""));
                    score = jsonObject.optInt("score");
                    tvTotalPoints.setText(TextUtils.concat("积分数\n", score + ""));
                    isSign = jsonObject.optInt("sign");
                    if (isSign == 1) {
                        list.get(3).setImgId(R.mipmap.icon_e_person_sign_in);
                        adapter.notifyDataSetChanged();
                    } else {
                        list.get(3).setImgId(R.mipmap.icon_e_person_unsign_in);
                        adapter.notifyDataSetChanged();
                    }
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            // 我的回答
            case 0:
                startActivity(new Intent(getActivity(), E_MyAnswerActivity.class));
                break;
            // 文章发布
            case 1:
                startActivity(new Intent(getActivity(), ArticleActivity.class));
                break;
            // 个人资料
            case 2:
                startActivity(new Intent(getActivity(), DataActivity.class));
                break;
            // 问卷调查
            case 3:
                if (isSign == 1) {
                    ToastTool.show(getActivity(), "今日已签到");
                } else {
                    toSignIn();
                }
                break;
            // 我的患者
            case 4:
                startActivity(new Intent(getActivity(), E_MyPatientActivity.class));
                break;
            // 我的积分
            case 5:
                startActivity(new Intent(getActivity(), MyPointActivity.class));
                break;
            default:
                ToastTool.show(getActivity(), "正在建设中...");
                break;
        }
    }

    /**
     * 签到
     */
    private void toSignIn() {
        Param param = new Param(Constant.SIGN_IN);
        param.addToken();
        new HttpUtil().HttpRequest(getActivity(), param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                tvTotalPoints.setText(TextUtils.concat("积分数\n", (score + 1) + ""));
                list.get(3).setImgId(R.mipmap.icon_e_person_sign_in);
                adapter.notifyDataSetChanged();
                isSign =1;
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(getActivity(), httpresult.getMsg());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.civ_avatar)
    public void onClick() {
        startActivity(new Intent(getActivity(), DataActivity.class));
    }
}
