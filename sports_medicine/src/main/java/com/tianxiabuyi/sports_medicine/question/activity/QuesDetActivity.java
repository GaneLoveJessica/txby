package com.tianxiabuyi.sports_medicine.question.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.DateUtil;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.activity.LoginActivity;
import com.tianxiabuyi.sports_medicine.model.Praise;
import com.tianxiabuyi.sports_medicine.model.Question;
import com.tianxiabuyi.sports_medicine.model.Reply;
import com.tianxiabuyi.sports_medicine.model.User;
import com.ninegrid.ImageInfo;
import com.ninegrid.NineGridView;
import com.tianxiabuyi.sports_medicine.question.adapter.NineGridAdapter;
import com.tianxiabuyi.sports_medicine.question.adapter.PraiseAdapter;
import com.tianxiabuyi.sports_medicine.question.adapter.ReplyAdapter;
import com.tianxiabuyi.sports_medicine.question.event.CommentEvent;
import com.tianxiabuyi.sports_medicine.question.event.LoveOrTreadEvent;
import com.tianxiabuyi.sports_medicine.question.util.LoveRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 问答详情
 */
public class QuesDetActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.lv_reply)
    ListView lvReply;
    private View head;
    private List<Reply> replyList = new ArrayList<>();
    private List<Praise> praiseList = new ArrayList<>();
    private List<Praise> treadList = new ArrayList<>();
    private ReplyAdapter replyAdapter;
    private Question question;
    private PraiseAdapter praiseAdapter;
    private PraiseAdapter treadAdapter;
    private RadioButton rbComment;
    private RadioButton rbPraise;
    private RadioButton rbTread;
    private ImageView ivLove;
    private ImageView ivTread;

    @Override
    public int getLayoutId() {
        return R.layout.activity_question_detail;
    }

    @Override
    public void initContentView() {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        title.setText("详情");
        // 1：社区  2：问答 3：推送 4:专家详情中的解答 5:我的问题 6:问答搜索 7：社区搜索
        // 200：专家 100：用户
        head = getLayoutInflater().inflate(R.layout.list_head_reply, lvReply, false);
        lvReply.addHeaderView(head);
        head.setVisibility(View.INVISIBLE);
        replyAdapter = new ReplyAdapter(this, replyList);
        lvReply.setAdapter(replyAdapter);
        praiseAdapter = new PraiseAdapter(this, praiseList);
        treadAdapter = new PraiseAdapter(this, treadList);
        loadReply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 请求点赞的人/踩的人
     *
     * @param flag 点赞的人：true  踩的人：false
     */
    private void requestPraise(final boolean flag) {
        Param param = new Param(Constant.PRAISE_PERSON);
        param.addRequstParams("id", question.getId());
        if (!flag) {
            param.addRequstParams("type", 4);
        }
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                List<Praise> praises = httpresult.getListResult("love", new TypeToken<List<Praise>>() {
                });
                if (flag) {
                    praiseList.clear();
                    praiseList.addAll(praises);
                    rbPraise.setText(TextUtils.concat("赞\t" + praises.size()));
                } else {
                    treadList.clear();
                    treadList.addAll(praises);
                    rbTread.setText(TextUtils.concat("踩\t" + praises.size()));
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(QuesDetActivity.this, httpresult.getMsg());
            }
        });
    }

    /**
     * 请求评论列表
     */
    public void loadReply() {
        Param param = new Param(Constant.QUESTION_DETAIL);
        param.addRequstParams("quest_id", getIntent().getStringExtra(Constant.KEY_1));
        param.addRequstParams("uid", UserSpUtil.getUid(this));
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                List<Reply> replies = httpresult.getListResult("replies", new TypeToken<List<Reply>>() {
                });
                question = httpresult.getObjectResult("quest", Question.class);
                initHeadView();
                requestPraise(true);
                requestPraise(false);
                replyAdapter.refreshData(replies);
                rbComment.setText("评论\t" + replies.size());
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(QuesDetActivity.this, httpresult.getMsg());
            }
        });
    }

    private void initHeadView() {
        head.setVisibility(View.VISIBLE);
        ImageView headImage = (ImageView) head.findViewById(R.id.iv_image);
        TextView name = (TextView) head.findViewById(R.id.tv_name);
        TextView time = (TextView) head.findViewById(R.id.tv_time);
//        TextView title = (TextView) head.findViewById(R.id.tv_title);
        TextView content = (TextView) head.findViewById(R.id.tv_content);
        RadioGroup radioGroup = (RadioGroup) head.findViewById(R.id.rg_indicator);
        rbComment = (RadioButton) head.findViewById(R.id.rb_comment);
        rbPraise = (RadioButton) head.findViewById(R.id.rb_praise);
        rbTread = (RadioButton) head.findViewById(R.id.rb_tread);
        ImageView ivComment = (ImageView) head.findViewById(R.id.iv_comment);
        ivLove = (ImageView) head.findViewById(R.id.iv_love);
        ivTread = (ImageView) head.findViewById(R.id.iv_tread);
        if (question.getIs_loved() == 1) {
            ivLove.setImageResource(R.mipmap.timeline_icon_like);
        } else {
            ivLove.setImageResource(R.mipmap.timeline_icon_unlike);
        }
        if (question.getIs_treaded() == 1) {
            ivTread.setImageResource(R.mipmap.timeline_icon_tread);
        } else {
            ivTread.setImageResource(R.mipmap.timeline_icon_untread);
        }
        ivTread.setOnClickListener(this);
        ivLove.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        ivComment.setOnClickListener(this);
        boolean canComment = getIntent().getBooleanExtra(Constant.KEY_2, false);
        if (canComment) {
            ivComment.setVisibility(View.VISIBLE);
        }else if (UserSpUtil.getStatus(this) == 200) {
            ivComment.setVisibility(View.VISIBLE);
        } else {
            ivComment.setVisibility(View.GONE);
        }
        radioGroup.setVisibility(View.VISIBLE);
        GlideUtil.setCircleAvatar(this,headImage,question.getAvatar());
        name.setText(this.question.getUser_name());
        time.setText(DateUtil.getPrefix(this.question.getCreate_time()));
//        title.setText(question.getTitle());
        content.setText(this.question.getContent());
        List<String> imgList = this.question.getImgs();
        if (imgList != null && imgList.size() > 0) {
            NineGridView gridView = (NineGridView) head.findViewById(R.id.gv_picture);
            gridView.setVisibility(View.VISIBLE);
            ArrayList<ImageInfo> imageInfos = new ArrayList<>();
            for (String url : imgList) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(Constant.THUMB + "?name=" + url.substring(url.lastIndexOf("/") + 1));
                info.setBigImageUrl(url);
                imageInfos.add(info);
            }
            NineGridAdapter adapter = new NineGridAdapter(this, imageInfos);
            gridView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        if (!UserSpUtil.isLogin(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        switch (v.getId()) {
            case R.id.iv_comment: {
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra(Constant.KEY_1, question);
                startActivity(intent);
            }
            break;
            case R.id.iv_love:
                if (question.getIs_loved() == 1) {// 取消赞
                    question.setIs_loved(0);
                    ivLove.setImageResource(R.mipmap.timeline_icon_unlike);
                    ivLove.startAnimation(AnimationUtils.loadAnimation(this, R.anim.love_anim));
                    String username = UserSpUtil.getUsername(this);
                    for (int i = 0; i < praiseList.size(); i++) {
                        if (praiseList.get(i).getUser_name().toLowerCase().equals(username.toLowerCase())) {
                            praiseList.remove(i);
                        }
                    }
                    LoveRequest.cancelLove(this, v, question, true, new LoveRequest.LoveCallback() {
                        @Override
                        public void onSuccess() {
                            EventBus.getDefault().post(new LoveOrTreadEvent(true, question));
                        }
                    });
                } else if (question.getIs_treaded() == 0) {// 点赞
                    question.setIs_loved(1);
                    ivLove.setImageResource(R.mipmap.timeline_icon_like);
                    ivLove.startAnimation(AnimationUtils.loadAnimation(this, R.anim.love_anim));
                    User user = UserSpUtil.getUser(this);
                    praiseList.add(0, new Praise(user.getAvatar(), user.getUser_name()));
                    LoveRequest.toLove(this, v, question, true, new LoveRequest.LoveCallback() {
                        @Override
                        public void onSuccess() {
                            EventBus.getDefault().post(new LoveOrTreadEvent(true, question));
                        }
                    });
                }
                rbPraise.setText("赞\t" + praiseList.size());
                praiseAdapter.notifyDataSetChanged();
                break;
            case R.id.iv_tread:
                if (question.getIs_treaded() == 1) {// 取消踩
                    ivTread.setImageResource(R.mipmap.timeline_icon_untread);
                    ivTread.startAnimation(AnimationUtils.loadAnimation(this, R.anim.love_anim));
                    question.setIs_treaded(0);
                    String username = UserSpUtil.getUsername(this);
                    for (int i = 0; i < treadList.size(); i++) {
                        if (treadList.get(i).getUser_name().toLowerCase().equals(username.toLowerCase())) {
                            treadList.remove(i);
                        }
                    }
                    LoveRequest.cancelLove(this, v, question, false, new LoveRequest.LoveCallback() {
                        @Override
                        public void onSuccess() {
                            EventBus.getDefault().post(new LoveOrTreadEvent(false, question));
                        }
                    });
                } else if (question.getIs_loved() == 0) {// 踩
                    ivTread.setImageResource(R.mipmap.timeline_icon_tread);
                    ivTread.startAnimation(AnimationUtils.loadAnimation(this, R.anim.love_anim));
                    question.setIs_treaded(1);
                    User user = UserSpUtil.getUser(this);
                    treadList.add(0, new Praise(user.getAvatar(), user.getUser_name()));
                    LoveRequest.toLove(this, v, question, false, new LoveRequest.LoveCallback() {
                        @Override
                        public void onSuccess() {
                            EventBus.getDefault().post(new LoveOrTreadEvent(false, question));
                        }
                    });
                }
                rbTread.setText("踩\t" + treadList.size());
                treadAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Subscribe
    public void onCommentEvent(CommentEvent event) {
        replyAdapter.refreshData(event.getReplies());
        lvReply.setSelection(replyList.size() - 1);
        rbComment.setText("评论\t" + event.getReplies().size());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(this, BrowseImgActivity.class);
        intent.putStringArrayListExtra(Constant.KEY_1, question.getImgs());
        intent.putExtra(Constant.KEY_2, position);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_comment:
                lvReply.setDividerHeight(10);
                lvReply.setAdapter(replyAdapter);
                break;
            case R.id.rb_praise:
                lvReply.setDividerHeight(0);
                lvReply.setAdapter(praiseAdapter);
                break;
            case R.id.rb_tread:
                lvReply.setDividerHeight(0);
                lvReply.setAdapter(treadAdapter);
                break;
        }
    }
}
