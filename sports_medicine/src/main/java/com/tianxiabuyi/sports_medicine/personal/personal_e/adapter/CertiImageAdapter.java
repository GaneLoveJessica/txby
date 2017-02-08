package com.tianxiabuyi.sports_medicine.personal.personal_e.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;

import java.util.List;

/**
 * 证书图片适配器
 */
public class CertiImageAdapter extends BaseListAdapter<String> implements View.OnClickListener {
    private boolean isEdit;

    public CertiImageAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, String path, int position) {
        if (path == null) {
            holder.imageView_1.setImageResource(R.mipmap.add_picture);
            holder.imageView_2.setVisibility(View.GONE);
        } else {
            Glide.with(context)
                    .load(path)
                    .dontAnimate()
                    .into(holder.imageView_1);
            if (!isEdit) {
                holder.imageView_2.setVisibility(View.GONE);
            } else if (list.size() <= 2) {
                holder.imageView_2.setVisibility(View.GONE);
            } else {
                holder.imageView_2.setVisibility(View.VISIBLE);
                holder.imageView_2.setTag(position);
                holder.imageView_2.setOnClickListener(this);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.grid_item_picture;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_picture);
        holder.imageView_2 = (ImageView) view.findViewById(R.id.iv_delete);
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        list.remove(position);
        notifyDataSetChanged();
    }

    public void setState(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            list.add(0, null);
        } else {
            list.remove(null);
        }
        notifyDataSetChanged();
    }

}
