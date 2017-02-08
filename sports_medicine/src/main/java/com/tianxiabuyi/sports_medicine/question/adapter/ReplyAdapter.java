package com.tianxiabuyi.sports_medicine.question.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.DateUtil;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.login.activity.LoginActivity;
import com.tianxiabuyi.sports_medicine.model.Reply;
import com.tianxiabuyi.sports_medicine.question.activity.BrowseImgActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 回复列表适配器
 */
public class ReplyAdapter extends BaseListAdapter<Reply> implements AdapterView.OnItemClickListener, View.OnClickListener {

    public ReplyAdapter(Context context, List<Reply> list) {
        super(context, list);
    }

    @Override
    public void showView(CViewHolder holder, Reply reply, int position) {
        GlideUtil.setCircleAvatar(context, holder.imageView_1, reply.getAvatar());
        holder.textView_1.setText(reply.getUser_name());
        holder.textView_2.setText(DateUtil.getPrefix(reply.getCreate_time()));
        holder.textView_3.setText(reply.getContent());
        List<String> images = reply.getImgs();
        if (images == null || images.size() == 0) {
            holder.gridView_1.setVisibility(View.GONE);
        } else {
            holder.gridView_1.setVisibility(View.VISIBLE);
            PictureAdapter adapter = new PictureAdapter(context, reply.getImgs());
            holder.gridView_1.setAdapter(adapter);
            holder.gridView_1.setTag(images);
            holder.gridView_1.setOnItemClickListener(this);
        }
        if (reply.getIs_loved() == 1) {
            holder.imageView_2.setImageResource(R.mipmap.timeline_icon_like);
            holder.textView_4.setTextColor(Color.parseColor("#DF4023"));
        } else {
            holder.imageView_2.setImageResource(R.mipmap.timeline_icon_unlike);
            holder.textView_4.setTextColor(Color.parseColor("#989898"));
        }
        if (reply.getIs_treaded() == 1) {
            holder.imageView_3.setImageResource(R.mipmap.timeline_icon_tread);
            holder.textView_5.setTextColor(Color.parseColor("#DF4023"));
        } else {
            holder.imageView_3.setImageResource(R.mipmap.timeline_icon_untread);
            holder.textView_5.setTextColor(Color.parseColor("#989898"));
        }
        if (reply.getLove() == 0) {
            holder.textView_4.setText("");
        } else {
            holder.textView_4.setText(reply.getLove() + "");
        }
        if (reply.getTread() == 0) {
            holder.textView_5.setText("");
        } else {
            holder.textView_5.setText(reply.getTread() + "");
        }
        holder.linearLayout_1.setTag(reply);
        holder.linearLayout_1.setOnClickListener(this);
        holder.linearLayout_2.setTag(reply);
        holder.linearLayout_2.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_item_reply;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_head);
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_name);
        holder.textView_2 = (TextView) view.findViewById(R.id.tv_time);
        holder.textView_3 = (TextView) view.findViewById(R.id.tv_content);
        holder.gridView_1 = (GridView) view.findViewById(R.id.gv_picture);
        holder.imageView_2 = (ImageView) view.findViewById(R.id.iv_love);
        holder.imageView_3 = (ImageView) view.findViewById(R.id.iv_tread);
        holder.textView_4 = (TextView) view.findViewById(R.id.tv_love_number);
        holder.textView_5 = (TextView) view.findViewById(R.id.tv_tread_number);
        holder.linearLayout_1 = (LinearLayout) view.findViewById(R.id.ll_love);
        holder.linearLayout_2 = (LinearLayout) view.findViewById(R.id.ll_tread);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        ArrayList<String> list = (ArrayList<String>) adapterView.getTag();
        Intent intent = new Intent(context, BrowseImgActivity.class);
        intent.putStringArrayListExtra(Constant.KEY_1, list);
        intent.putExtra(Constant.KEY_2, position);
        context.startActivity(intent);
    }

    /**
     * 取消点赞、踩
     *
     * @param v
     * @param question
     * @param action   点赞：true  踩：false
     */
    private void cancelPraise(final View v, final Reply question, final boolean action) {
        v.setEnabled(false);
        Param param = new Param(Constant.CANCEL_PRAISE);
        param.addToken();
        param.addRequstParams("category", 4);
        if (action) {
            param.addRequstParams("id", question.getLoved_id());
            param.addRequstParams("operate", 3);
        } else {
            param.addRequstParams("id", question.getTreaded_id());
            param.addRequstParams("operate", 4);
        }
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(context, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                v.setEnabled(true);
                try {
                    JSONObject jsonObject = new JSONObject(httpresult.getResult());
                    if (action) {
                        question.setLoved_id(jsonObject.getLong("id"));
                        question.setIs_loved(0);
                    } else {
                        question.setTreaded_id(jsonObject.getLong("id"));
                        question.setIs_treaded(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                v.setEnabled(true);
            }
        });
    }

    /**
     * 点赞/踩
     *
     * @param v
     * @param reply
     * @param action 点赞：true  踩：false
     */
    private void praiseQuestion(final View v, final Reply reply, final boolean action) {
        v.setEnabled(false);
        Param param = new Param(Constant.PRAISE);
        param.addToken();
        param.addRequstParams("oid", reply.getId());
        param.addRequstParams("category", 4);
        if (action) {
            param.addRequstParams("operate", 3);
        } else {
            param.addRequstParams("operate", 4);
        }
        param.setIsShowLoading(false);
        new HttpUtil().HttpRequest(context, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                v.setEnabled(true);
                try {
                    JSONObject jsonObject = new JSONObject(httpresult.getResult());
                    if (action) {
                        reply.setLoved_id(jsonObject.getLong("id"));
                        reply.setIs_loved(1);
                    } else {
                        reply.setTreaded_id(jsonObject.getLong("id"));
                        reply.setIs_treaded(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void err(HttpResult httpresult) {
                v.setEnabled(true);
            }
        });
    }

    public void refreshData(List<Reply> replies) {
        list.clear();
        list.addAll(replies);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (!UserSpUtil.isLogin(context)) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }
        Reply reply = (Reply) v.getTag();
        switch (v.getId()) {
            case R.id.ll_tread:
                if (reply.getIs_treaded() == 1) {// 取消踩
                    reply.setIs_treaded(0);
                    reply.setTread(reply.getTread() - 1);
                    cancelPraise(v, reply, false);
                } else if (reply.getIs_loved() == 0) {// 踩
                    reply.setIs_treaded(1);
                    reply.setTread(reply.getTread() + 1);
                    praiseQuestion(v, reply, false);
                }
                break;
            case R.id.ll_love:
                if (reply.getIs_loved() == 1) {// 取消赞
                    reply.setIs_loved(0);
                    reply.setLove(reply.getLove() - 1);
                    cancelPraise(v, reply, true);
                } else if (reply.getIs_treaded() == 0) {// 赞
                    reply.setIs_loved(1);
                    reply.setLove(reply.getLove() + 1);
                    praiseQuestion(v, reply, true);
                }
                break;
        }
        notifyDataSetChanged();
    }
}
