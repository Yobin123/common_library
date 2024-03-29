package com.yobin_he.image_preview_library.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.yobin_he.image_preview_library.R;
import com.yobin_he.image_preview_library.entry.Image;
import com.yobin_he.image_preview_library.utils.ImageUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//import com.bumptech.glide.request.RequestOptions;
//import com.bumptech.glide.request.transition.Transition;

public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<PhotoView> viewList = new ArrayList<>(4);
    List<Image> mImgList;
    private OnItemClickListener mListener;

    public ImagePagerAdapter(Context context, List<Image> imgList) {
        this.mContext = context;
        createImageViews();
        mImgList = imgList;
    }

    private void createImageViews() {
        for (int i = 0; i < 4; i++) {
            PhotoView imageView = new PhotoView(mContext);
            imageView.setAdjustViewBounds(true);
            viewList.add(imageView);
        }
    }

    @Override
    public int getCount() {
        return mImgList == null ? 0 : mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof PhotoView) {
            PhotoView view = (PhotoView) object;
            view.setImageDrawable(null);
            viewList.add(view);
            container.removeView(view);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final PhotoView currentView = viewList.remove(0);
        final Image image = mImgList.get(position);
        container.addView(currentView);
        if (image.isGif()) {
            currentView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(mContext).load(new File(image.getPath())).diskCacheStrategy(DiskCacheStrategy.NONE).into(currentView);
            /*Glide.with(mContext).load(new File(image.getPath()))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(currentView);*/
        } else {
            if (image.getPath().contains("http")) {
//                currentView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(mContext).load(image.getPath()).centerCrop().error(R.drawable.timg).fitCenter().into(currentView);
            } else {
//                currentView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Glide.with(mContext).load(new File(image.getPath())).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int bw = resource.getWidth();
                        int bh = resource.getHeight();
                        if (bw > 8192 || bh > 8192) {
                            Bitmap bitmap = ImageUtil.zoomBitmap(resource, 8192, 8192);
                            setBitmap(currentView, bitmap);
                        } else {
                            setBitmap(currentView, resource);
                        }
                    }
                });
            }
           
            /*Glide.with(mContext).asBitmap()
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .load(new File(image.getPath())).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    int bw = resource.getWidth();
                    int bh = resource.getHeight();
                    if (bw > 8192 || bh > 8192) {
                        Bitmap bitmap = ImageUtil.zoomBitmap(resource, 8192, 8192);
                        setBitmap(currentView, bitmap);
                    } else {
                        setBitmap(currentView, resource);
                    }
                }
            });*/
        }
        currentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position, image);
                }
            }
        });
        return currentView;
    }

    private void setBitmap(PhotoView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        if (bitmap != null) {
            int bw = bitmap.getWidth();
            int bh = bitmap.getHeight();
            int vw = imageView.getWidth();
            int vh = imageView.getHeight();
            if (bw != 0 && bh != 0 && vw != 0 && vh != 0) {
                if (1.0f * bh / bw > 1.0f * vh / vw) {
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    float offset = (1.0f * bh * vw / bw - vh) / 2;
                    adjustOffset(imageView, offset);
                } else {
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Image image);
    }

    private void adjustOffset(PhotoView view, float offset) {
        PhotoViewAttacher attacher = view.getAttacher();
        try {
            Field field = PhotoViewAttacher.class.getDeclaredField("mBaseMatrix");
            field.setAccessible(true);
            Matrix matrix = (Matrix) field.get(attacher);
            matrix.postTranslate(0, offset);
            Method method = PhotoViewAttacher.class.getDeclaredMethod("resetMatrix");
            method.setAccessible(true);
            method.invoke(attacher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
