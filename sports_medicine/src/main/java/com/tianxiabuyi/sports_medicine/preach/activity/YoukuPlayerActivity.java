package com.tianxiabuyi.sports_medicine.preach.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.NetUtils;
import com.eeesys.frame.utils.ToastTool;
import com.eeesys.frame.view.ScrollViewListView;
import com.google.gson.reflect.TypeToken;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.DateUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.activity.LoginActivity;
import com.tianxiabuyi.sports_medicine.model.Preach;
import com.tianxiabuyi.sports_medicine.model.Reply;
import com.tianxiabuyi.sports_medicine.model.User;
import com.tianxiabuyi.sports_medicine.preach.adapter.VideoReplyAdapter;
import com.tianxiabuyi.sports_medicine.preach.event.VideoPraiseEvent;
import com.youku.player.VideoDefinition;
import com.youku.player.base.YoukuPlayerView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/26.
 */
public class YoukuPlayerActivity extends BaseActivity {

    private static final String TAG = "YoukuPlayerActivity";
    @Bind(R.id.tv_comment_number)
    TextView tvCommentNumber;
    @Bind(R.id.tv_play_time)
    TextView tvPlayTime;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_publish)
    TextView tvPublish;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.lv_reply)
    ScrollViewListView lvReply;
    @Bind(R.id.ll_root)
    LinearLayout llRoot;
    @Bind(R.id.tv_video_title)
    TextView tvVideoTitle;
    @Bind(R.id.full_holder)
    YoukuPlayerView youkuPlayerView;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.iv_love)
    ImageView ivLove;
    @Bind(R.id.tv_love_number)
    TextView tvLoveNumber;
    @Bind(R.id.iv_tread)
    ImageView ivTread;
    @Bind(R.id.tv_tread_number)
    TextView tvTreadNumber;
    private Preach preach;
    private List<Reply> list = new ArrayList<>();
    private VideoReplyAdapter adapter;

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_youku_player;
    }

    @Override
    protected void initContentView() {
        setStatusBarColor(android.R.color.black);
        ButterKnife.bind(this);
        preach = getIntent().getParcelableExtra(Constant.KEY_1);
        // 初始化播放器
        youkuPlayerView.attachActivity(this);
        youkuPlayerView.setPreferVideoDefinition(VideoDefinition.VIDEO_HD);
        if (NetUtils.isConnected(this) && NetUtils.isWifi(this)) {
            youkuPlayerView.playYoukuVideo(unicodeToUtf8(preach.getJson().getId()));
            ivPlay.setVisibility(View.GONE);
        }
        iniView();
    }

    private void iniView() {
        if (preach != null) {
            tvVideoTitle.setText(preach.getTitle());
            tvPlayTime.setText("播放：" + preach.getBrowse());
            tvTime.setText("时间：" + preach.getTime());
            if (preach.getIs_loved() == 1) {
                ivLove.setImageResource(R.mipmap.timeline_icon_like);
            } else {
                ivLove.setImageResource(R.mipmap.timeline_icon_unlike);
            }
            if (preach.getIs_treaded() == 1) {
                ivTread.setImageResource(R.mipmap.timeline_icon_tread);
            } else {
                ivTread.setImageResource(R.mipmap.timeline_icon_untread);
            }
            tvLoveNumber.setText(preach.getLove() + "");
            tvTreadNumber.setText(preach.getTread() + "");
        }
        adapter = new VideoReplyAdapter(this, list);
        lvReply.setAdapter(adapter);
        loadData();
    }

    private void cancelPraise(final boolean action) {
        Param param = new Param(Constant.CANCEL_PRAISE);
        param.setIsShowLoading(false);
        param.addToken();
        if (action) {
            param.addRequstParams("id", preach.getLoved_id());
            param.addRequstParams("operate", 3);
        } else {
            param.addRequstParams("id", preach.getTreaded_id());
            param.addRequstParams("operate", 4);
        }
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                try {
                    JSONObject jsonObject = new JSONObject(httpresult.getResult());
                    if (action) {
                        preach.setLoved_id(jsonObject.getLong("id"));
                        preach.setLove(preach.getLove() - 1);
                        preach.setIs_loved(0);
                    } else {
                        preach.setTreaded_id(jsonObject.getLong("id"));
                        preach.setTread(preach.getTread() - 1);
                        preach.setIs_treaded(0);
                    }
                    EventBus.getDefault().postSticky(new VideoPraiseEvent(preach));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void err(HttpResult httpresult) {

            }
        });
    }

    private void toPraise(final boolean action) {
        final Param param = new Param(Constant.PRAISE);
        param.addRequstParams("oid", preach.getId());
        param.addRequstParams("category", 1);
        if (action) {
            param.addRequstParams("operate", 3);
        } else {
            param.addRequstParams("operate", 4);
        }
        param.setIsShowLoading(false);
        param.addToken();
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                try {
                    JSONObject jsonObject = new JSONObject(httpresult.getResult());
                    if (action) {
                        preach.setLoved_id(jsonObject.getLong("id"));
                        preach.setIs_loved(1);
                        preach.setLove(preach.getLove() + 1);
                    } else {
                        preach.setTreaded_id(jsonObject.getLong("id"));
                        preach.setIs_treaded(1);
                        preach.setTread(preach.getTread() + 1);
                    }
                    EventBus.getDefault().postSticky(new VideoPraiseEvent(preach));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void err(HttpResult httpresult) {

            }
        });
    }

    private void loadData() {
        Param param = new Param(Constant.PREACH_DETAIL);
        param.addRequstParams("news_id", preach.getNews_id());
        param.addRequstParams("uid", UserSpUtil.getUid(this));
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                List<Reply> replies = httpresult.getListResult("comment", new TypeToken<List<Reply>>() {
                });
                tvCommentNumber.setText(replies.size() + "评论");
                list.clear();
                list.addAll(replies);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(YoukuPlayerActivity.this, httpresult.getMsg());
            }
        });
    }

    /**
     * 发表评论
     *
     * @param view
     */
    public void publishReply(View view) {
        if (!UserSpUtil.isLogin(this)) {
            ToastTool.show(this, "请先登录");
            return;
        }
        if (TextUtils.isEmpty(getText(etContent))) {
            ToastTool.show(this, "请输入评论内容");
            return;
        }
        hideSoftKeyboard();
        tvPublish.setEnabled(false);
        Param param = new Param(Constant.REPLY_NEWS);
        param.addRequstParams("id", preach.getId());
        param.addRequstParams("content", getText(etContent));
        param.addToken();
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                tvPublish.setEnabled(true);
                Reply reply = new Reply();
                User user = UserSpUtil.getUser(YoukuPlayerActivity.this);
                reply.setUser_name(user.getUser_name());
                reply.setAvatar(user.getAvatar());
                reply.setCreate_time(DateUtil.getTodayDate());
                reply.setContent(getText(etContent));
                list.add(reply);
                adapter.notifyDataSetChanged();
                tvCommentNumber.setText(list.size() + "评论");
                etContent.setText("");
            }

            @Override
            public void err(HttpResult httpresult) {
                tvPublish.setEnabled(true);
                ToastTool.show(YoukuPlayerActivity.this, httpresult.getMsg());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 必须重写的onPause()
        youkuPlayerView.onPause();
        Log.e("liyh", "player onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 必须重写的onResume()
        youkuPlayerView.onResume();
        Log.e("liyh", "player onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 必须重写的onDestroy()
        youkuPlayerView.onDestroy();
        Log.e("liyh", "player onDestroy");
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //后退键的处理，当全屏时，后退为变为小屏
        if (youkuPlayerView.isFullScreen()) {
            youkuPlayerView.goSmallScreen();
        } else {
            super.onBackPressed();
        }
    }

    public String unicodeToUtf8(String theString) {
        if (theString == null) return "";
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    @OnClick(R.id.iv_play)
    public void onClick() {
        youkuPlayerView.playYoukuVideo(unicodeToUtf8(preach.getJson().getId()));
        ivPlay.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_love, R.id.iv_tread})
    public void onClick(View view) {
        if (!UserSpUtil.isLogin(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        switch (view.getId()) {
            case R.id.iv_love:
                if (preach.getIs_loved() == 1) {
                    ivLove.setImageResource(R.mipmap.timeline_icon_unlike);
                    ivLove.startAnimation(AnimationUtils.loadAnimation(this, R.anim.love_anim));
                    tvLoveNumber.setText(preach.getLove() - 1 + "");
                    cancelPraise(true);
                } else if (preach.getIs_treaded() == 0) {// 点赞
                    ivLove.setImageResource(R.mipmap.timeline_icon_like);
                    ivLove.startAnimation(AnimationUtils.loadAnimation(this, R.anim.love_anim));
                    tvLoveNumber.setText(preach.getLove() + 1 + "");
                    toPraise(true);
                }
                break;
            case R.id.iv_tread:
                if (preach.getIs_treaded() == 1) {
                    ivTread.setImageResource(R.mipmap.timeline_icon_untread);
                    ivTread.startAnimation(AnimationUtils.loadAnimation(this, R.anim.love_anim));
                    tvTreadNumber.setText(preach.getTread() - 1 + "");
                    cancelPraise(false);
                } else if (preach.getIs_loved() == 0) {
                    ivTread.setImageResource(R.mipmap.timeline_icon_tread);
                    ivTread.startAnimation(AnimationUtils.loadAnimation(this, R.anim.love_anim));
                    tvTreadNumber.setText(preach.getTread() + 1 + "");
                    toPraise(false);
                }
                break;
        }
    }
}
