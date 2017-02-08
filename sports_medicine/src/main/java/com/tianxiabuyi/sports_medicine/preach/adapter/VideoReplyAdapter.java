package com.tianxiabuyi.sports_medicine.preach.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.model.Reply;

import java.util.List;

/**
 * Created by Administrator on 2016/10/15.
 */

public class VideoReplyAdapter extends BaseListAdapter<Reply> {
    public VideoReplyAdapter(Context context, List<Reply> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, Reply reply, int position) {
        GlideUtil.setCircleAvatar(context,holder.imageView_1,reply.getAvatar());
        holder.textView_1.setText(reply.getUser_name());
        holder.textView_2.setText(reply.getCreate_time());
        holder.textView_3.setText(reply.getContent());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_item_video_reply;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_avatar);
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_name);
        holder.textView_2 = (TextView) view.findViewById(R.id.tv_time);
        holder.textView_3 = (TextView) view.findViewById(R.id.tv_content);
    }
}
