package com.tianxiabuyi.sports_medicine.question.view;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.tianxiabuyi.sports_medicine.MyApp;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;

/**
 * Created by Administrator on 2016/12/13.
 */

public class QuestionLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.quick_view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        if (UserSpUtil.isLogin(MyApp.getInstance())) {
            holder.setText(R.id.tv_prompt, R.string.load_failed);
        } else {
            holder.setText(R.id.tv_prompt, R.string.toast_login_see_more);
        }
        super.convert(holder);
    }
}
