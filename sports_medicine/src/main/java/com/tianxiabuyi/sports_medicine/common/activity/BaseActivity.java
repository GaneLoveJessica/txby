package com.tianxiabuyi.sports_medicine.common.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tianxiabuyi.sports_medicine.R;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/7/25.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private InputMethodManager inputMethodManager;
    protected TextView title;
    protected TextView tvRight;
    protected ImageView ivRight;
    protected ProgressBar baseProgressBar;
    protected ImageView ivLeft;
    protected Toolbar baseToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (isFullScreen()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setStatusBarColor(android.R.color.transparent);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
        setContentView(R.layout.activity_base);
        if (showActionBar() && !isFullScreen()) {
             baseToolbar = (Toolbar) findViewById(R.id.toolbar);
            ivLeft = (ImageView) findViewById(R.id.iv_left);
            title = (TextView) findViewById(R.id.base_title);
            tvRight = (TextView) findViewById(R.id.tv_right);
            ivRight = (ImageView) findViewById(R.id.iv_right);
            baseProgressBar = (ProgressBar) findViewById(R.id.base_progressBar);
            setSupportActionBar(baseToolbar);
            ivLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftClick();
                }
            });
            RightClickListener listener = new RightClickListener();
            ivRight.setOnClickListener(listener);
            tvRight.setOnClickListener(listener);
        } else {
            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.abl_app_bar);
            appBarLayout.setVisibility(View.GONE);
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_parent);
        View view = getLayoutInflater().inflate(getLayoutId(), linearLayout, false);
        linearLayout.addView(view);
        initContentView();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public int getStatusBarHeight() {
        int sbar = 38;//默认为38，貌似大部分是这样的
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    protected void leftClick() {
        finish();
    }

    protected void rightClick() {
    }

    /**
     * 是否显示标题栏
     *
     * @return
     */
    protected boolean showActionBar() {
        return true;
    }

    protected boolean isFullScreen() {
        return false;
    }

    protected abstract int getLayoutId();

    protected abstract void initContentView();

    /**
     * 隐藏软键盘
     */
    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected String getText(TextView editText) {
        return editText.getText().toString().trim();
    }

    private class RightClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            rightClick();
        }
    }

    public void setStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(color));
        }
    }
}
