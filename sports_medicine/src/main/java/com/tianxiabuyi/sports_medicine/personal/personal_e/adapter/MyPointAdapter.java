package com.tianxiabuyi.sports_medicine.personal.personal_e.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eeesys.frame.listview.abs.BaseListAdapter;
import com.eeesys.frame.listview.model.CViewHolder;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.util.DateUtil;
import com.tianxiabuyi.sports_medicine.common.util.GlideUtil;
import com.tianxiabuyi.sports_medicine.model.Point;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Administrator on 2016/9/28.
 */

public class MyPointAdapter extends BaseListAdapter<Point.DataBean> implements StickyListHeadersAdapter {

    private final LayoutInflater mInflater;

    public MyPointAdapter(Context context, List<Point.DataBean> list) {
        super(context, list);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void showView(CViewHolder holder, Point.DataBean point, int position) {
        holder.textView_1.setText(TextUtils.concat(point.getWeek_day() + "\n" +
                changeoverDayTime(point.getCreate_time())));
        if (point.getScore() > 0) {
            holder.textView_2.setText("+" + TextUtils.concat(point.getScore() + ""));
        } else {
            holder.textView_2.setText(TextUtils.concat(point.getScore() + ""));
        }
        if (point.getJson().getTypes() != null) {
            holder.textView_3.setText(point.getJson().getTypes());
        } else if (point.getOperator().equals("系统")) {
            holder.textView_3.setText("系统 赠送积分");
        } else if (point.getCategory() == 2) {
            holder.textView_3.setText("提了一个问题");
        } else if (point.getCategory() == 5) {
            holder.textView_3.setText("签到");
        } else if (point.getCategory() == 6) {
            holder.textView_3.setText("分享新闻");
        } else if (point.getCategory() == 7) {
            holder.textView_3.setText("日步数达到5000步");
        } else if (point.getCategory() == 8) {
            holder.textView_3.setText("分享APP");
        } else {
            String desc = null;
            if (point.getType() == 3) {
                desc = point.getOperator_name() + " 赞了您的评论";
            } else if (point.getType() == 4) {
                desc = point.getOperator_name() + " 踩了您的评论";
            } else if (point.getType() == 5) {
                desc = point.getOperator_name() + " 取消赞您的评论";
            } else if (point.getType() == 6) {
                desc = point.getOperator_name() + " 取消踩您的评论";
            }
            SpannableString spannableString = new SpannableString(desc);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ff8228")), 0,
                    point.getOperator_name().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.textView_3.setText(spannableString);
        }
        GlideUtil.setCircleAvatar(context, holder.imageView_1, point.getOperator_avatar());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_item_my_point;
    }

    @Override
    protected void initViewHolder(CViewHolder holder, View view) {
        holder.textView_1 = (TextView) view.findViewById(R.id.tv_time);
        holder.textView_2 = (TextView) view.findViewById(R.id.tv_score);
        holder.textView_3 = (TextView) view.findViewById(R.id.tv_desc);
        holder.textView_4 = (TextView) view.findViewById(R.id.tv_month);
        holder.imageView_1 = (ImageView) view.findViewById(R.id.iv_avatar);
    }

    /**
     * 月份转换
     *
     * @param datetime
     * @return
     */
    private String changeoverMonthTime(String datetime) {
        if (datetime.equals(DateUtil.getCurrentMonth())) {
            return "本月";
        } else {
            return datetime.replace("-", "年") + "月";
        }
    }

    private String changeoverDayTime(String datetime) {
        if (datetime.substring(0, 10).equals(DateUtil.getTodayDate())) {
            return "今天";
        } else {
            return datetime.substring(5, 10);
        }
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header_point, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.tv_month);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        // set header text as first char in name
        holder.text.setText(changeoverMonthTime(list.get(position).getMonth()));
        return convertView;
    }

    private class HeaderViewHolder {
        TextView text;
    }

    @Override
    public long getHeaderId(int position) {
        return Integer.valueOf(list.get(position).getMonth().substring(5));
    }
}
