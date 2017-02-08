package com.tianxiabuyi.sports_medicine.personal.personal_c.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BaseAdapter;
import com.tianxiabuyi.sports_medicine.model.Step;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */

public class StepAdapter extends BaseAdapter<Step> {

    public StepAdapter(List<Step> data) {
        super(R.layout.list_item_step,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Step step) {
        holder.setText(R.id.tv_date,step.getStep_time().replace("-",".").replace("-","."))
                .setText(R.id.tv_step,TextUtils.concat(step.getStep()+"步"));
    }
}
