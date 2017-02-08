package com.tianxiabuyi.sports_medicine.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.model.Expert;

import java.util.List;

/**
 * Created by Administrator on 2016/10/15.
 */

public class HomeDoctorAdapter extends BaseAdapter {
    private final Context context;
    private final int screenWidth;
    private List<Expert> list;

    public HomeDoctorAdapter(Context context, List<Expert> list) {
        this.list = list;
        this.context = context;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        screenWidth = wm.getDefaultDisplay().getWidth();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position % list.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CViewHolder holder;
        if (convertView == null) {
            holder = new CViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_famous_doctor, parent, false);
            convertView.setLayoutParams(new LinearLayout.LayoutParams( screenWidth/ 4, ViewGroup.LayoutParams.MATCH_PARENT));
            holder.imageView_1 = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.textView_1 = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (CViewHolder) convertView.getTag();
        }
        Expert expert1 = list.get(position % list.size());
        GlideUtil.setCircleAvatar(context, holder.imageView_1, expert1.getAvatar());
        holder.textView_1.setText(expert1.getName());
        return convertView;
    }
}
