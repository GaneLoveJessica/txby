package com.tianxiabuyi.sports_medicine.personal.personal_e.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.model.Info;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class EPersonalMenuAdapter extends BaseListAdapter<Info> {
    public EPersonalMenuAdapter(Context context, List<Info> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, Info info, int position) {
        holder.textView_1.setText(info.getTitle());
        holder.textView_1.setCompoundDrawablesWithIntrinsicBounds(0, info.getImgId(), 0, 0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.grid_item_expert_personal_center;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_title);
    }
}
