package com.yobin_he.packagedemo.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @Author: yobin he
 * @Date:2019/8/30 16:10
 * @Email: heyibin@huawenpicture.com
 * @Des :用于图片的显示
 */
public class GlideUtil {

    //加载普通图片
    public static void load(Context context, String url, ImageView imageView) {
        if (context != null) {
            imageView.setBackgroundResource(0);
            Glide.with(context).load(url)
                    .fitCenter()
                    .into(imageView);
        }
    }

    public static void load(Context context, int imageRes, ImageView imageView) {
        if (context != null)
            Glide.with(context).load(imageRes).fitCenter().into(imageView);
    }

    //加载包含占位图
    public static void loadWithPlaceholder(Context context, String url, ImageView imageView, int placeHolder) {
        if (context != null)
            Glide.with(context).load(url).placeholder(placeHolder).error(placeHolder).into(imageView);
    }

    //加载高斯模糊图片
    public static void loadBlurePic(Context context, String url, ImageView imageView) {
        if (context != null)
            Glide.with(context).load(url).bitmapTransform(new BlurTransformation(context, 23, 4)).into(imageView);
    }

    //加载圆形图
    public static void loadCirclePic(Context context, String url, ImageView imageView) {
        if (context != null)
            Glide.with(context).load(url).bitmapTransform(new CropCircleTransformation(context)).into(imageView);
    }

    //加载圆角图
    public static void loadRoundPic(Context context, String url, ImageView imageView, int radius) {
        Glide.with(context).load(url)
                .bitmapTransform(new RoundedCornersTransformation(context, radius, radius)).into(imageView);
    }

    //加载列表等带有logo占位图的图片
    public static void loadWithLogoIcon(Context context, String url, ImageView imageView) {
        if (context != null) {
            imageView.setBackgroundResource(0);
            Glide.with(context).load(url)
                    .fitCenter()
                    .into(imageView);
        }
    }

}
