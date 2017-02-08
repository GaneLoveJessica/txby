package com.tianxiabuyi.sports_medicine.question.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.util.DateUtil;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.model.Question;
import com.ninegrid.ImageInfo;
import com.ninegrid.NineGridView;
import com.tianxiabuyi.sports_medicine.question.activity.QuesDetActivity;
import com.tianxiabuyi.sports_medicine.question.util.MyViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题适配器
 */
public class QuestionAdapter extends BaseQuickAdapter<Question, MyViewHolder> implements MyViewHolder.MyItemClickListener {
    private int position;

    public QuestionAdapter(List<Question> data) {
        super(R.layout.list_item_question, data);
    }

    @Override
    protected void convert(MyViewHolder holder, Question question) {
        GlideUtil.setCircleAvatar(mContext, (ImageView) holder.getView(R.id.iv_image), question.getAvatar());
        holder.setText(R.id.tv_name, question.getUser_name())
                .setText(R.id.tv_time, DateUtil.getPrefix(question.getCreate_time()))
                .setText(R.id.tv_content, question.getContent())
                .setText(R.id.tv_love_num, "点赞" + question.getLove());
        List<String> images = question.getImgs();
        if (images == null || images.size() == 0) {
            holder.setVisible(R.id.gv_picture, false);
        } else {
            holder.setVisible(R.id.gv_picture, true);
            NineGridView gridView = holder.getView(R.id.gv_picture);
            ArrayList<ImageInfo> imageInfos = new ArrayList<>();
            for (String url : images) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(Constant.THUMB + "?name=" + url.substring(url.lastIndexOf("/") + 1));
                info.setBigImageUrl(url);
                imageInfos.add(info);
            }
            NineGridAdapter adapter = new NineGridAdapter(mContext, imageInfos);
            gridView.setAdapter(adapter);
        }
        holder.setOnItemClickListener(this);
    }

    @Override
    protected MyViewHolder createBaseViewHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    public void onItemClick(View view, int position) {
        this.position = position;
        Question question = getItem(position);
        Intent intent = new Intent(mContext, QuesDetActivity.class);
        intent.putExtra(Constant.KEY_1, question.getId() + "");
        intent.putExtra(Constant.KEY_2, false);
        mContext.startActivity(intent);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
