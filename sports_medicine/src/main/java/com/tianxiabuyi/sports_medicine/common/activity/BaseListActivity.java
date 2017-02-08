package com.tianxiabuyi.sports_medicine.common.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public abstract class BaseListActivity<T> extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "BaseListActivity";
    private ListView listView;
    private ImageView empty;
    protected List<T> list = new ArrayList<>();
    protected BaseAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list;
    }

    @Override
    protected void initContentView() {
        listView = (ListView) findViewById(R.id.listView);
        empty = (ImageView) findViewById(R.id.tv_empty);
        init();
        adapter = getAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        loadData();
    }

    protected void loadData() {
        Param param = getParam();
        if (param == null) {
            Log.e(TAG, "请设置请求参数");
            return;
        }
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                 onSuccess(httpresult);
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(BaseListActivity.this,httpresult.getMsg());
                listView.setEmptyView(empty);
            }
        });
    }

    protected void onSuccess(HttpResult httpresult) {
    }

    protected abstract BaseAdapter getAdapter();

    protected abstract Param getParam();

    protected abstract void init();

    protected void refreshData(List<T> data){
        list.clear();
        if (data!=null && data.size()>0){
            list.addAll(data);
        }
        adapter.notifyDataSetChanged();
        listView.setEmptyView(empty);
    }

    protected void showDivider(boolean isShowDivider) {
        if (!isShowDivider) {
            listView.setDividerHeight(0);
        }
    }

    protected void setDividerColor(String color) {
        listView.setDivider(new ColorDrawable(Color.parseColor(color)));
        listView.setDividerHeight(1);
    }

    protected View setHeadView(int layoutId){
        View view = getLayoutInflater().inflate(layoutId,null);
        listView.addHeaderView(view);
        return view;
    }

    protected void setDividerHeight(int height) {
        listView.setDividerHeight(dp2px(height));
    }

    protected void setPadding(int left, int top, int right, int bottom) {
        listView.setPadding(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom));
    }

    public int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        itemClick((T)adapterView.getItemAtPosition(i),i);
    }

    protected void itemClick(T t, int position){

    }
}
