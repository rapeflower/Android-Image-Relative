package com.j1.healthcare.patient.utils.imageloader;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.util.Util;
import com.j1.healthcare.patient.utils.DisplayUnitUtils;

/***********
 *
 * @Author rape flower
 * @Date 2016-09-20 15:22
 * @Describe BaseTarget获取bitmap根据bitmap设定图片显示尺寸：
 * (这里适配屏幕宽度，高度按比例拉伸)
 *
 */
public class CustomTargetProxy<T extends View> extends BaseTarget<Bitmap> implements Proxy{

    private final String TAG = CustomTargetProxy.class.getSimpleName();
    private T view;
    private final int width;
    private final int height;

    public CustomTargetProxy(T container) {
        this(container, SIZE_ORIGINAL, SIZE_ORIGINAL);
    }

    public CustomTargetProxy(T container, int width) {
        this(container, width, SIZE_ORIGINAL);
    }

    public CustomTargetProxy(T container, int width, int height) {
        this.view = container;
        this.width = width;
        this.height = height;
    }

    @Override
    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
        if (view instanceof ImageView) {
            ImageView img = (ImageView) view;

            if (this.width == SIZE_ORIGINAL && this.height == SIZE_ORIGINAL) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                ViewGroup.LayoutParams lp = img.getLayoutParams();
                lp.width = DisplayUnitUtils.getDisplayWidth();

                float tempHeight = height * ((float) lp.width / width);
                lp.height = (int) tempHeight;
                img.setLayoutParams(lp);
            } else if (this.width != SIZE_ORIGINAL && this.height == SIZE_ORIGINAL){
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                ViewGroup.LayoutParams lp = img.getLayoutParams();
                lp.width = this.width;

                float tempHeight = height * ((float) lp.width / width);
                lp.height = (int) tempHeight;
                img.setLayoutParams(lp);
            } else {
                ViewGroup.LayoutParams lp = img.getLayoutParams();
                lp.width = this.width;
                lp.height = this.height;
                img.setLayoutParams(lp);
            }
            img.setImageBitmap(bitmap);
        }
    }

    @Override
    public void getSize(SizeReadyCallback cb) {
        if (!Util.isValidDimensions(width, height)) {
            Log.w(TAG, "Width and height must both be > 0 or Target#SIZE_ORIGINAL, "
                    + "but given width: " + width + " and height: " + height
                    + ", either provide dimensions in the constructor or call override()");
        }
        cb.onSizeReady(width, height);
    }
}
