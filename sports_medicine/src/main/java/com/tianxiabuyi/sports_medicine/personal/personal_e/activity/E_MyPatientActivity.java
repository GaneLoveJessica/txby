package com.tianxiabuyi.sports_medicine.personal.personal_e.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.view.MySwipeRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.model.Patient;
import com.tianxiabuyi.sports_medicine.personal.personal_e.adapter.PatientAdapter;
import com.tianxiabuyi.sports_medicine.personal.personal_e.adapter.PatientTopAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的患者
 */
public class E_MyPatientActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, MySwipeRefreshLayout.OnLoadListener {
    private ListView lvMyPatient;
    private GridView gvTop;
    private MySwipeRefreshLayout refreshLayout;
    private List<Patient> list = new ArrayList<>();
    private PatientAdapter adapter;
    private int currentPage = 1;
    private View headView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_e__my_patient;
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected void initContentView() {
        refreshLayout = (MySwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        lvMyPatient = (ListView) findViewById(R.id.lv_my_patient);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        headView = getLayoutInflater().inflate(R.layout.list_head_patient, lvMyPatient, false);
        headView.setVisibility(View.GONE);
        gvTop = (GridView) headView.findViewById(R.id.gv_top);
        lvMyPatient.addHeaderView(headView);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        adapter = new PatientAdapter(E_MyPatientActivity.this, list);
        lvMyPatient.setAdapter(adapter);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    private void loadMyPatient(final boolean isFresh) {
        final Param param = new Param(Constant.MY_PATIENT);
        param.setIsShowLoading(false);
        if (!isFresh) {
            param.addRequstParams("page", currentPage + 1);
        }
        param.addToken();
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                List<Patient> patients = httpresult.getListResult("data", new TypeToken<List<Patient>>() {
                });
                currentPage = httpresult.getNumber("page");
                if (isFresh) {
                    refreshLayout.setRefreshing(false);
                    if (patients.size() == 0){
                        lvMyPatient.setEmptyView(findViewById(R.id.tv_empty));
                    }else{
                        lvMyPatient.setEmptyView(null);
                        findViewById(R.id.tv_empty).setVisibility(View.GONE);
                    }
                 headView.setVisibility(View.VISIBLE);
                    if (patients.size() <= 3) {
                        PatientTopAdapter topAdapter = new PatientTopAdapter(E_MyPatientActivity.this, patients);
                        gvTop.setAdapter(topAdapter);
                    } else {
                        PatientTopAdapter topAdapter = new PatientTopAdapter(E_MyPatientActivity.this, patients.subList(0, 3));
                        gvTop.setAdapter(topAdapter);
                        list.clear();
                        list.addAll(patients.subList(3, patients.size()));
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    refreshLayout.setLoading(false);
                    list.addAll(patients);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                lvMyPatient.setEmptyView(findViewById(R.id.tv_empty));
                if (isFresh){
                    refreshLayout.setRefreshing(false);
                }else{
                    refreshLayout.setLoading(false);
                }
            }
        });
    }

    /**
     * 返回按钮点击事件
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    @Override
    public void onRefresh() {
        loadMyPatient(true);
    }

    @Override
    public void onLoad() {
        if (list.size() < 20) {
            refreshLayout.setLoading(false);
        } else {
            loadMyPatient(false);
        }
    }
}
