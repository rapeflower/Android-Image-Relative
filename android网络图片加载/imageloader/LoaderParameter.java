package com.j1.healthcare.patient.utils.imageloader;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BaseTarget;


/***********
 *
 * @Author rape flower
 * @Date 2016-09-19 16:27
 * @Describe 加载图片参数对象封装
 * <p>
 *    LoaderParameter.Builder builder = LoaderParameter.newBuilder();
 *    builder.setUrl("");
 *    builder.setPlaceHolder(DrawableHolder.mPlaceholderDrawable);
 *    builder.setErrorHolder(DrawableHolder.mErrorDrawable);
 *    builder.setImageView(null);
 *    LoaderParameter loaderParameter = builder.build();
 * </p>
 */
public class LoaderParameter {

    private String url; //需要解析的url
    private Drawable placeHolder; //当加载中的时候显示的图片
    private Drawable errorHolder; //当加载过程中失败时显示的图片
    private ImageView imageView; //ImageView的实例
    private Proxy target;//重新设置图片大小的接口

    private LoaderParameter(Builder builder) {
        this.url = builder.url;
        this.placeHolder = builder.placeHolder;
        this.errorHolder = builder.errorHolder;
        this.imageView = builder.imageView;
        this.target = builder.target;
    }

    public static Builder newBuilder() {
        return Builder.create();
    }

    public String getUrl() {
        return url;
    }

    public Drawable getPlaceHolder() {
        return placeHolder;
    }

    public Drawable getErrorHolder() {
        return errorHolder;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Proxy getTarget() {
        return target;
    }

    public static class Builder {
        private String url; //需要解析的url
        private Drawable placeHolder; //当没有成功加载的时候显示的图片
        private Drawable errorHolder; //当加载过程中出现error时显示的图片
        private ImageView imageView; //ImageView的实例
        private Proxy target;//重新设置图片大小的接口

        private Builder() {
            this.url = "";
            this.placeHolder = DrawableHolder.mPlaceholderDrawable;
            this.errorHolder = DrawableHolder.mErrorDrawable;
            this.imageView = null;
            this.target = null;
        }

        private static Builder create() {
            return new Builder();
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setPlaceHolder(Drawable placeHolder) {
            this.placeHolder = placeHolder;
            return this;
        }

        public Builder setErrorHolder(Drawable errorHolder) {
            this.errorHolder = errorHolder;
            return this;
        }

        public Builder setImageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder setTarget(Proxy target) {
            this.target = target;
            return this;
        }

        public LoaderParameter build() {
            return new LoaderParameter(this);
        }
    }
}
