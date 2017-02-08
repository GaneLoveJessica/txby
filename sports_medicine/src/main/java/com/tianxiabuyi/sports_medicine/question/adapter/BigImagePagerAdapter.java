package com.tianxiabuyi.sports_medicine.question.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tianxiabuyi.sports_medicine.Constant;
import com.tianxiabuyi.sports_medicine.R;
import com.tianxiabuyi.sports_medicine.question.activity.BrowseImgActivity;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 浏览大图适配器
 */
public class BigImagePagerAdapter extends PagerAdapter {
    private final ArrayList<String> images;
    private final BrowseImgActivity activity;

    public BigImagePagerAdapter(BrowseImgActivity activity, ArrayList<String> images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = activity.getLayoutInflater().inflate(R.layout.pager_big_image, container, false);
        final ImageView ivThumb = (ImageView) view.findViewById(R.id.iv_thumb);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        final PhotoView imageView = (PhotoView) view.findViewById(R.id.pv_image);
        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                activity.finish();
            }

            @Override
            public void onOutsidePhotoTap() {
                activity.finish();
            }
        });
        String url = images.get(position);
        if (url.startsWith("http://")) {
            Glide.with(activity)
                    .load(Constant.THUMB + "?name=" + url.substring(url.lastIndexOf("/") + 1))
                    .dontAnimate()
                    .into(ivThumb);
            Glide.with(activity)
                    .load(images.get(position))
                    .dontAnimate()
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            ivThumb.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            imageView.setImageDrawable(resource);
                        }
                    });
        }else{
            ivThumb.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Glide.with(activity)
                    .load(images.get(position))
                    .into(imageView);
        }
        container.addView(view);
        return view;
    }

}
