package com.tianxiabuyi.sports_medicine.personal.personal_e.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.util.GlideCircleTransform;
import com.tianxiabuyi.sports_medicine.model.Patient;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class PatientAdapter extends BaseListAdapter<Patient> {
    public PatientAdapter(Context context, List<Patient> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, Patient patient, int position) {
        Glide.with(context)
                .load(patient.getAvatar())
                .bitmapTransform(new GlideCircleTransform(context))
                .placeholder(R.mipmap.avatar)
                .error(R.mipmap.avatar)
                .into(holder.imageView_1);
        holder.textView_1.setText(position + 4+"");
        holder.textView_2.setText(patient.getUser_name());
        holder.textView_3.setText(patient.getCount()+"");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_item_patient;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_avatar);
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_ranking);
        holder.textView_2 = (TextView) view.findViewById(R.id.tv_name);
        holder.textView_3 = (TextView) view.findViewById(R.id.tv_ask_count);
    }
}
