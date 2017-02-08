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
 * 我的患者提问榜
 */
public class PatientTopAdapter extends BaseListAdapter<Patient> {
    public PatientTopAdapter(Context context, List<Patient> list) {
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
        holder.textView_2.setText(patient.getUser_name());
        holder.textView_3.setText(patient.getCount()+"");
        if (position == 0){
            holder.imageView_2.setImageResource(R.mipmap.hat1);
        }else if (position == 1){
            holder.imageView_2.setImageResource(R.mipmap.hat2);
        }else if (position == 2){
            holder.imageView_2.setImageResource(R.mipmap.hat3);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.grid_item_patient_top;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_avatar);
        holder.imageView_2 = (ImageView) view.findViewById(R.id.iv_crown);
        holder.textView_2 = (TextView) view.findViewById(R.id.tv_name);
        holder.textView_3 = (TextView) view.findViewById(R.id.tv_ask_count);
    }
}
