package com.tianxiabuyi.sports_medicine.personal.personal_e.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.aihook.alertview.library.AlertView;
import com.aihook.alertview.library.OnItemClickListener;
import com.eeesys.frame.http.CommHttp;
import com.eeesys.frame.http.HttpResult;
import com.eeesys.frame.utils.ToastTool;
import com.eeesys.frame.view.WheelView;
import com.photo.model.TResult;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.base.BasePhotoActivity;
import com.tianxiabuyi.sports_medicine.common.net.HttpUtil;
import com.tianxiabuyi.sports_medicine.common.net.Param;
import com.tianxiabuyi.sports_medicine.common.util.UserSpUtil;
import com.tianxiabuyi.sports_medicine.model.User;
import com.tianxiabuyi.sports_medicine.personal.personal_e.adapter.CertiImageAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 专家修改资料
 */
public class E_ModifyDataActivity extends BasePhotoActivity implements AdapterView.OnItemClickListener, OnItemClickListener {
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.tv_gender)
    TextView tvGender;
    @Bind(R.id.et_positional_title)
    EditText etPositionalTitle;
    @Bind(R.id.et_major)
    EditText etMajor;
    @Bind(R.id.et_introduce)
    EditText etIntroduce;
    @Bind(R.id.gv_picture)
    GridView gvPicture;
    private List<String> imgPath = new ArrayList<>();
    private CertiImageAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_e__modify_data;
    }

    @Override
    protected void initContentView() {
        ButterKnife.bind(this);
        title.setText(R.string.personal_info);
        tvRight.setText(R.string.edit);
        tvRight.setVisibility(View.VISIBLE);
        gvPicture.setEnabled(false);
        User user = UserSpUtil.getUser(this);
        tvUsername.setText(user.getUser_name());
        etName.setText(user.getName());
        tvGender.setText(getGender(user.getGender()));
        etPositionalTitle.setText(user.getMy_title());
        etMajor.setText(user.getMajor());
        etIntroduce.setText(user.getIntroduce());
        String[] cers = user.getCertification().split(",");
        if (cers.length > 0) {
            imgPath.addAll(Arrays.asList(cers));
        }
        adapter = new CertiImageAdapter(this, imgPath);
        gvPicture.setAdapter(adapter);
        gvPicture.setOnItemClickListener(this);
    }

    private String getGender(String gender) {
        if ("0".equals(gender)) {
            return "女";
        }
        if ("1".equals(gender)) {
            return "男";
        }
        return "";
    }

    /**
     * 选择性别
     *
     * @param view
     */
    public void chooseGender(View view) {
        View v = getLayoutInflater().inflate(R.layout.dialog_wheelview, null);
        final WheelView wheelView = (WheelView) v.findViewById(R.id.wheelView);
        String[] gender = {"男", "女"};
        wheelView.setItems(Arrays.asList(gender));
        new AlertView(null, null, "取消", null, new String[]{"确定"}, this,
                AlertView.Style.AlertDialog, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    tvGender.setText(wheelView.getSeletedItem());
                }
            }
        })
                .addExtView(v)
                .show();
    }

    @Override
    protected void rightClick() {
        if (tvRight.getText().toString().equals("编辑")) {
            if (UserSpUtil.getUser(this).getName().length() == 0) {
                etName.setEnabled(true);
            }
            tvGender.setEnabled(true);
            etPositionalTitle.setEnabled(true);
            etIntroduce.setEnabled(true);
            etMajor.setEnabled(true);
            gvPicture.setEnabled(true);
            tvRight.setText(R.string.save);
            adapter.setState(true);
        } else {
            if (checkInfo()) {
                hideSoftKeyboard();
                List<String> pathList = new ArrayList<>();
                pathList.addAll(imgPath);
                Iterator<String> paths = pathList.iterator();
                while (paths.hasNext()) {
                    String path = paths.next();
                    if (path == null || path.startsWith("http") || path.equals("null")) {
                        paths.remove();
                    }
                }
                if (pathList.size() > 0) {
                    uploadPhotos(pathList, new PhotosUploadBack() {
                        @Override
                        public void onSuccess(List<String> imgUrls) {
                            uploadData(getCertification(imgUrls));
                        }

                        @Override
                        public void onError(String s) {

                        }
                    });
                } else {
                    uploadData(getCertification(imgPath));
                }
            }
        }
    }

    private String getCertification(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                if (i != list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        for (int i = 0; i < imgPath.size(); i++) {
            if (imgPath.get(i) != null && imgPath.get(i).startsWith("http")) {
                sb.append("," + imgPath.get(i));
            } else {
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 上传资料
     *
     * @param certification
     */
    private void uploadData(final String certification) {
        Param param = new Param(Constant.MODIFY);
        param.addToken();
        param.addRequstParams("name", getText(etName));
        param.addRequstParams("gender", getText(tvGender).equals("男") ? 1 : 0);
        param.addRequstParams("my_title", getText(etPositionalTitle));
        if (certification != null) {
            param.addRequstParams("certification", certification);
        }
        param.addRequstParams("introduce", getText(etIntroduce));
        param.addRequstParams("major", getText(etMajor));
        new HttpUtil().HttpRequest(this, param, new CommHttp.HttpBack() {
            @Override
            public void success(HttpResult httpresult) {
                tvRight.setText(R.string.edit);
                etName.setEnabled(false);
                tvGender.setEnabled(false);
                etPositionalTitle.setEnabled(false);
                etMajor.setEnabled(false);
                etIntroduce.setEnabled(false);
                gvPicture.setEnabled(false);
                adapter.setState(false);
                User user = UserSpUtil.getUser(E_ModifyDataActivity.this);
                user.setName(getText(etName));
                user.setGender(getText(tvGender).equals("男") ? "1" : "0");
                user.setMy_title(getText(etPositionalTitle));
                user.setMajor(getText(etMajor));
                user.setIntroduce(getText(etIntroduce));
                if (certification != null) {
                    user.setCertification(certification);
                }
                UserSpUtil.setUserInfo(E_ModifyDataActivity.this, user);
                ToastTool.show(E_ModifyDataActivity.this, "资料修改成功");
            }

            @Override
            public void err(HttpResult httpresult) {
                ToastTool.show(E_ModifyDataActivity.this, httpresult.getMsg());
            }
        });
    }

    /**
     * 检查信息
     */
    private boolean checkInfo() {
        if (TextUtils.isEmpty(getText(etName))) {
            ToastTool.show(this, "请输入真实姓名");
            return false;
        }
        if (!Pattern.matches(Constant.REGEX_NAME, getText(etName))) {
            ToastTool.show(this, "姓名格式不正确");
            return false;
        }
        if (getText(etPositionalTitle).length() > 20) {
            ToastTool.show(this, "职称最多可输入20个字符");
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            hideSoftKeyboard();
            new AlertView("添加图片", null, "取消", null,
                    new String[]{"拍照", "从相册中选择"},
                    this, AlertView.Style.ActionSheet, this).show();
        }
    }

    @Override
    public void onItemClick(Object o, int position) {
        if (position == 0) { // 拍照
            takePhoto(false);
        } else if (position == 1) { // 选择相册
            selectPhoto(false);
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        imgPath.add(result.getImage().getCompressPath());
        adapter.notifyDataSetChanged();
    }
}
