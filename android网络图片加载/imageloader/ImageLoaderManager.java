package com.j1.healthcare.patient.utils.imageloader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;


/***********
 * @Author rape flower
 * @Date 2016-09-20 09:43
 * @Describe 对外提供的加载图片的类
 */
public class ImageLoaderManager {

    //default instance class GlideImageLoader
    private static BaseImageLoader imageLoader = GlideImageLoader.getInstance();

    public ImageLoaderManager() {
    }

    /**
     * 构建请求参数
     *
     * @param imageView   显示图片的控件
     * @param url         图片地址
     * @param placeholder 加载中的显示图片
     * @param errorHolder 加载失败显示的图片
     * @param target      根据Bitmap重新设置图片大小的接口
     * @return
     */
    private static LoaderParameter createParameter(ImageView imageView, String url, Drawable placeholder, Drawable errorHolder, Proxy target) {
        LoaderParameter.Builder builder = LoaderParameter.newBuilder();
        builder.setUrl(url);
        builder.setPlaceHolder(placeholder);
        builder.setErrorHolder(errorHolder);
        builder.setImageView(imageView);
        builder.setTarget(target);
        return builder.build();
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param parameter 加载网络图片的参数对象
     */
    private static void loadImage(Context context, LoaderParameter parameter) {
        if (imageLoader == null) {
            throw new IllegalArgumentException("You must pass in a non null imageLoader");
        }
        imageLoader.loadImage(context, parameter);
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param imageView 显示图片的控件
     * @param url       图片的地址
     */
    public static void loadImage(Context context, ImageView imageView, String url) {
        LoaderParameter parameter = createParameter(imageView
                , url
                , DrawableHolder.mPlaceholderDrawable
                , DrawableHolder.mErrorDrawable
                , (Proxy) null);
        loadImage(context, parameter);
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param imageView 显示图片的控件
     * @param url       图片的地址
     * @param target    重新设置图片大小的接口
     */
    public static void loadImage(Context context, ImageView imageView, String url, Proxy target) {
        LoaderParameter parameter = createParameter(imageView
                , url
                , DrawableHolder.mPlaceholderDrawable
                , DrawableHolder.mErrorDrawable
                , target);
        loadImage(context, parameter);
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param imageView      显示图片的控件
     * @param url            图片的地址
     * @param placeholderRes 加载中的图片的资源ID
     * @param errorHolderRes 加载失败的图片的资源ID
     */
    public static void loadImage(Context context, ImageView imageView, String url, int placeholderRes, int errorHolderRes) {
        LoaderParameter parameter = createParameter(imageView
                , url
                , DrawableHolder.getDrawable(context, placeholderRes)
                , DrawableHolder.getDrawable(context, errorHolderRes)
                , (Proxy) null);
        loadImage(context, parameter);
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param imageView      显示图片的控件
     * @param url            图片的地址
     * @param placeholderRes 加载中的图片的资源ID
     * @param errorHolderRes 加载失败的图片的资源ID
     * @param target         重新设置图片大小的接口
     */
    public static void loadImage(Context context, ImageView imageView, String url, int placeholderRes, int errorHolderRes, Proxy target) {
        LoaderParameter parameter = createParameter(imageView
                , url
                , DrawableHolder.getDrawable(context, placeholderRes)
                , DrawableHolder.getDrawable(context, errorHolderRes)
                , target);
        loadImage(context, parameter);
    }

    /**
     * 释放资源
     *
     * @param context
     */
    public static void clear(Context context) {
        if (imageLoader == null) {
            throw new IllegalArgumentException("You must pass in a non null imageLoader");
        }
        imageLoader.clear(context);
    }

    /**
     * 清除掉所有的图片加载请求
     *
     * @param object 传入的值可以是这些类型或是其子类：
     *               View、Target<?>，FutureTarget<?>
     */
    public static void cancel(Object object) {
        if (imageLoader == null) {
            throw new IllegalArgumentException("You must pass in a non null imageLoader");
        }
        imageLoader.cancel(object);
    }
}
