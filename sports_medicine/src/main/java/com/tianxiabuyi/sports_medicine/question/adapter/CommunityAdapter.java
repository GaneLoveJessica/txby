package com.tianxiabuyi.sports_medicine.question.adapter;

import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ninegrid.ImageInfo;
import com.ninegrid.NineGridView;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.util.DateUtil;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.activity.LoginActivity;
import com.tianxiabuyi.sports_medicine.model.Question;
import com.tianxiabuyi.sports_medicine.question.activity.CommentActivity;
import com.tianxiabuyi.sports_medicine.question.activity.QuesDetActivity;
import com.tianxiabuyi.sports_medicine.question.util.LoveRequest;
import com.tianxiabuyi.sports_medicine.question.util.MyViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题适配器
 */
public class CommunityAdapter extends BaseQuickAdapter<Question, MyViewHolder> implements
        View.OnClickListener, MyViewHolder.MyItemClickListener {
    private int position;

    public CommunityAdapter(List<Question> data) {
        super(R.layout.list_item_community, data);
    }

    @Override
    protected void convert(MyViewHolder holder, Question question) {
        GlideUtil.setCircleAvatar(mContext, (ImageView) holder.getView(R.id.iv_image), question.getAvatar());
        holder.setTag(R.id.iv_comment, question)
                .setTag(R.id.iv_praise, question)
                .setTag(R.id.iv_tread, question);
        holder.getView(R.id.iv_comment).setOnClickListener(this);
        holder.getView(R.id.iv_praise).setOnClickListener(this);
        holder.getView(R.id.iv_tread).setOnClickListener(this);
        holder.setText(R.id.tv_name, question.getUser_name())
                .setText(R.id.tv_time, DateUtil.getPrefix(question.getCreate_time()))
                .setText(R.id.tv_content, question.getContent())
                .setText(R.id.tv_browse_num, "浏览" + question.getBrowse() + "次");
        holder.addOnClickListener(R.id.gv_picture);
        if (question.getIs_loved() == 1) {
            holder.setImageResource(R.id.iv_praise, R.mipmap.timeline_icon_like);
        } else {
            holder.setImageResource(R.id.iv_praise, R.mipmap.timeline_icon_unlike);
        }
        if (question.getIs_treaded() == 1) {
            holder.setImageResource(R.id.iv_tread, R.mipmap.timeline_icon_tread);
        } else {
            holder.setImageResource(R.id.iv_tread, R.mipmap.timeline_icon_untread);
        }
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
//            if (images.size() == 1){
//                gridView.setSingleImageRatio(images.get(0).getWidth() * 1.0f / images.get(0).getHeight());
//            }
        }
        holder.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (UserSpUtil.isLogin(mContext)) {
            Question question = (Question) v.getTag();
            ImageView imageView = (ImageView) v;
            switch (v.getId()) {
                case R.id.iv_comment:
                    toComment(question);
                    break;
                case R.id.iv_praise:
                    toPraise(question, imageView);
                    break;
                case R.id.iv_tread:
                    toTread(question, imageView);
                    break;
            }
        }else{
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
        }
    }

    private void toTread(Question question, ImageView imageView) {
        if (question.getIs_treaded() == 1) {// 取消踩
            imageView.setImageResource(R.mipmap.timeline_icon_untread);
            imageView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.love_anim));
            question.setIs_treaded(0);
            LoveRequest.cancelLove(mContext, imageView, question, false, null);
        } else if (question.getIs_loved() == 0) {// 踩
            imageView.setImageResource(R.mipmap.timeline_icon_tread);
            imageView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.love_anim));
            question.setIs_treaded(1);
            LoveRequest.toLove(mContext, imageView, question, false, null);
        }
    }

    private void toPraise(Question question, ImageView imageView) {
        if (question.getIs_loved() == 1) {// 取消赞
            imageView.setImageResource(R.mipmap.timeline_icon_unlike);
            imageView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.love_anim));
            question.setIs_loved(0);
            LoveRequest.cancelLove(mContext, imageView, question, true, null);
        } else if (question.getIs_treaded() == 0) {// 点赞
            imageView.setImageResource(R.mipmap.timeline_icon_like);
            imageView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.love_anim));
            question.setIs_loved(1);
            LoveRequest.toLove(mContext, imageView, question, true, null);
        }
    }

    private void toComment(Question question) {
        Intent intent = new Intent(mContext, CommentActivity.class);
        intent.putExtra(Constant.KEY_1, question);
        mContext.startActivity(intent);
    }

    @Override
    protected MyViewHolder createBaseViewHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    public void onItemClick(View view, int position) {
        this.position = position;
        Question question = getItem(position);
        Intent intent = new Intent();
        intent.setClass(mContext, QuesDetActivity.class);
        intent.putExtra(Constant.KEY_1, question.getId() + "");
        intent.putExtra(Constant.KEY_2, true);
        mContext.startActivity(intent);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
