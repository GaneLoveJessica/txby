package com.tianxiabuyi.sports_medicine.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.eeesys.frame.utils.GsonTool;
import com.eeesys.frame.utils.ToastTool;
import com.google.gson.reflect.TypeToken;
import com.photo.app.TakePhoto;
import com.photo.app.TakePhotoImpl;
import com.photo.compress.CompressConfig;
import com.photo.model.CropOptions;
import com.photo.model.InvokeParam;
import com.photo.model.TContextWrap;
import com.photo.model.TResult;
import com.photo.permission.InvokeListener;
import com.photo.permission.PermissionManager;
import com.photo.permission.TakePhotoInvocationHandler;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.common.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */

public abstract class BasePhotoActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    private static final String TAG = BasePhotoActivity.class.getName();
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private Uri imageUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        imageUri = Uri.fromFile(file);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    private void configCompress() {
        CompressConfig config;
        config = new CompressConfig.Builder()
                .enableReserveRaw(false)// 是否保存原文件,针对相机拍照
                .create();
        getTakePhoto().onEnableCompress(config, true);
    }

    private CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(1)
                .setAspectY(1)
                .setOutputX(200)
                .setOutputY(200);
        return builder.create();
    }

    public void takePhoto(boolean isCrop) {
        configCompress();
        if (isCrop) {
            getTakePhoto().onPickFromCaptureWithCrop(imageUri, getCropOptions());
        } else {
            getTakePhoto().onPickFromCapture(imageUri);
        }
    }

    public void selectPhoto(boolean isCrop) {
        configCompress();
        if (isCrop) {
            getTakePhoto().onPickFromGalleryWithCrop(imageUri, getCropOptions());
        } else {
            getTakePhoto().onPickFromGallery();
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
        ToastTool.show(this, msg);
    }

    @Override
    public void takeCancel() {
    }

    /**
     * 上传单张图片
     *
     * @param filePath
     */
    protected void uploadPhoto(String filePath, final PhotoUploadBack back) {
        File file = new File(filePath);
        RequestParams params = new RequestParams(Constant.UPLOAD_IMAGE);
        params.addBodyParameter("name", file.getName());
        params.addBodyParameter("type", "file");
        params.addBodyParameter("file", file);
        final ProgressDialog pd = ProgressDialog.show(this, null, "加载中...", false, false);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jo = new JSONObject(result);
                    int code = jo.optInt("errcode");
                    if (code == 0) {
                        back.onSuccess(jo.optString("img"));
                    } else {
                        back.onError(jo.optString("errmsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("BasePhotoActivity", ex.getMessage());
                ToastTool.show(BasePhotoActivity.this, "网络异常，请重试");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("BasePhotoActivity", cex.getMessage());
            }

            @Override
            public void onFinished() {
                pd.dismiss();
            }
        });
    }

    /**
     * 上传多张图片
     *
     * @param imgPath
     * @param back
     */
    protected void uploadPhotos(List<String> imgPath, final PhotosUploadBack back) {
        final ProgressDialog pd = ProgressDialog.show(this, null, "加载中...", false, false);
        RequestParams params = new RequestParams(Constant.UPLOAD_IMAGE);
        for (int i = 0; i < imgPath.size(); i++) {
            if (imgPath.get(i) != null && imgPath.get(i).length() > 0) {
                params.addBodyParameter("file[]", new File(imgPath.get(i)));
            }
        }
        params.setMultipart(true);
        params.addBodyParameter("act", "multi");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jo = new JSONObject(result);
                    int code = jo.optInt("errcode");
                    if (code == 0) {
                        List<String> imgUrls = GsonTool.fromJson(jo.optString("result"), new TypeToken<List<String>>() {
                        });
                        back.onSuccess(imgUrls);
                    } else {
                        back.onError(jo.optString("errmsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                pd.dismiss();
            }
        });
    }

    public interface PhotosUploadBack {
        void onSuccess(List<String> imgUrls);

        void onError(String s);
    }

    public interface PhotoUploadBack {
        void onSuccess(String imgUrl);

        void onError(String s);
    }
}
