package com.tianxiabuyi.sports_medicine.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HomeMenuAdapter extends BaseListAdapter<String> {

    public HomeMenuAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, String item, int position) {
        if (position == 0) {
            holder.imageView_1.setImageResource(R.mipmap.icon_cloud_colligate);
        }else if(position == 1){
            holder.imageView_1.setImageResource(R.mipmap.icon_cloud_knowledge);
        }else if (position == 2){
            holder.imageView_1.setImageResource(R.mipmap.icon_cloud_recovery);
        }else{
            holder.imageView_1.setImageResource(R.mipmap.icon_cloud_medical);
        }
        holder.textView_1.setText(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.grid_item_home_menu;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_icon);
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_title);
    }
}
