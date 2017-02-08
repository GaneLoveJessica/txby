package com.tianxiabuyi.sports_medicine.personal.personal_c.activity;

import android.content.Intent;
import android.widget.BaseAdapter;

import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseListActivity;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.model.Info;
import com.tianxiabuyi.sports_medicine.personal.personal_c.adapter.HealthFileAdapter;

/**
 * 健康档案
 */
public class HealthFileActivity extends BaseListActivity<Info> {

    @Override
    protected BaseAdapter getAdapter() {
        return new HealthFileAdapter(this, list);
    }

    @Override
    protected Param getParam() {
        return null;
    }

    @Override
    protected void init() {
        title.setText(R.string.health_file);
        list.add(new Info("本人信息", R.mipmap.icon_personal_info));
        list.add(new Info("健身数据", R.mipmap.icon_sport_data));
        list.add(new Info("身体测量", R.mipmap.icon_body_measure));
        setDividerHeight(0);
    }

    @Override
    protected void itemClick(Info info, int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, PersonalInfoActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, SportDataActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, BodyMeasureActivity.class));
                break;
        }

    }
}
