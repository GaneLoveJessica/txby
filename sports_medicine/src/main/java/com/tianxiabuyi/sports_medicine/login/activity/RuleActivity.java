package com.tianxiabuyi.sports_medicine.login.activity;

import android.widget.TextView;

import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;
import com.tianxiabuyi.sports_medicine.Constant;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RuleActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rule;
    }

    @Override
    protected void initContentView() {
        int flag = getIntent().getIntExtra(Constant.KEY_1, 0);
        TextView tvRule = (TextView) findViewById(R.id.tv_rule);
        if (flag == 1) {
            title.setText("免责申明");
            tvRule.setText(getFromAssets("disclaimer.txt"));
        } else if (flag == 2) {
            title.setText("服务条款");
            tvRule.setText(getFromAssets("term_of_service.txt"));
        }
    }

    public String getFromAssets(String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open(fileName),"utf-8");
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line+"\n";
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
