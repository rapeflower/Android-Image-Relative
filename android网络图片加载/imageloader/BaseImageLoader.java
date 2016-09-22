package com.j1.healthcare.patient.utils.imageloader;

import android.content.Context;

/***********
 *
 * @Author rape flower
 * @Date 2016-09-19 15:38
 * @Describe 加载图片的公共接口
 *
 */
public interface BaseImageLoader {
    void loadImage(Context context, LoaderParameter loaderParameter);
    void clear(Context context);
}
