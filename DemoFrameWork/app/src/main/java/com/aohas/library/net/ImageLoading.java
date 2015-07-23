package com.aohas.library.net;

import android.graphics.Color;
import android.widget.ImageView;
//import com.aohas.library.Library;
//import com.aohas.library.util.CheckUtil;
//import com.squareup.makeramen.RoundedTransformationBuilder;
//import com.squareup.picasso.PicassoTools;
import com.aohas.library.Library;
import com.aohas.library.squareup.makeramen.RoundedTransformationBuilder;
import com.aohas.library.squareup.picasso.PicassoTools;
import com.aohas.library.util.CheckUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created with IntelliJ IDEA. User: liu_yu Date: 13-9-3 Time: 下午2:50 To change
 * this template use File | Settings | File Templates.
 */
public class ImageLoading {

    private final String TAG_IS_ROUND = "is-round-circle";
    private final String TAG_IS_ROUNDEDCORNER = "is-rounded-corner";
    private final String TAG_DEFAULT = "default";

    private static ImageLoading imageLoading;

    public static ImageLoading getInstance() {
        if (imageLoading == null) {
            imageLoading = new ImageLoading();
        }
        return imageLoading;
    }

    /**
     * 异步加载图片
     * 直接设置最大边长度，不做圆角和圆形裁剪
     *
     * @param imageView       图片View
     * @param url             图片地址
     * @param maxSize         图片最大大小
     * @param defaultResource 预加载默认图片
     */
    public void downLoadImage(ImageView imageView, String url, int defaultResource, int maxSize) {
        downLoadImage(imageView, url, defaultResource, maxSize, -1, false);
    }

    /**
     * 异步加载图片
     * 设置最大边长度，做圆角裁剪
     *
     * @param imageView         图片View
     * @param url               图片地址
     * @param maxSize           图片最大大小
     * @param defaultResource   预加载默认图片图片外圆角尺寸
     * @param roundedCornerSize 图片外圆角尺寸
     */
    public void downLoadImage(ImageView imageView, String url, int defaultResource, int maxSize, float roundedCornerSize) {
        downLoadImage(imageView, url, defaultResource, maxSize, roundedCornerSize, false);
    }

    /**
     * 异步加载图片
     * 设置最大边长度，做圆形裁剪
     *
     * @param imageView       图片View
     * @param url             图片地址
     * @param maxSize         图片最大大小
     * @param defaultResource 预加载默认图片图片外圆角尺寸
     * @param isRound         图片是否裁剪成圆形
     */
    public void downLoadImage(ImageView imageView, String url, int defaultResource, int maxSize, boolean isRound) {
        downLoadImage(imageView, url, defaultResource, maxSize, -1, isRound);
    }

    /**
     * 异步加载图片
     *
     * @param imageView         图片View
     * @param url               图片地址
     * @param maxSize           图片最大大小
     * @param defaultResource   预加载默认图片
     * @param roundedCornerSize 图片外圆角尺寸
     * @param isRound           图片是否裁剪成圆形
     */
    private void downLoadImage(ImageView imageView, String url, int defaultResource, int maxSize, float roundedCornerSize, boolean isRound) {
        if (isRound) {
            downLoadImageRound(imageView, url, defaultResource, maxSize);
        } else if (roundedCornerSize > 0) {
            downLoadImageRounded(imageView, url, defaultResource, maxSize, roundedCornerSize);
        } else {
            downLoadImageMaxSize(imageView, url, defaultResource, maxSize);
        }
    }

    /**
     * 获取普通图片
     */
    private void downLoadImageMaxSize(ImageView imageView, String url, int defaultResource, int maxSize) {
        clearCache();
        if (CheckUtil.isEmpty(url)) {
            imageView.setImageResource(defaultResource);
            return;
        }

        Picasso.with(Library.context).load(url)
                .placeholder(defaultResource)
                .error(defaultResource)
//                .resize(maxSize, maxSize)
                .into(imageView);

//        UrlImageViewHelper.setUrlDrawable(imageView, url, defaultResource, maxSize, new UrlImageViewCallback() {
//            @Override
//            public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
//                if (imageView == null || loadedBitmap == null)
//                    return;
//                imageView.setImageBitmap(loadedBitmap);
//            }
//        });
    }

    /**
     * 获取圆角图片
     */
    private void downLoadImageRounded(ImageView imageView, String url, int defaultResource, int maxSize, final float roundedCornerSize) {
        clearCache();
        if (CheckUtil.isEmpty(url)) {
            imageView.setImageResource(defaultResource);
            return;
        }

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.TRANSPARENT)
                .borderWidthDp(0)
                .cornerRadiusDp(roundedCornerSize)
                .oval(false)
                .build();


        Picasso.with(Library.context).load(url)
                .placeholder(defaultResource)
                .error(defaultResource)
                .resize(maxSize, maxSize)
                .transform(transformation)
                .centerCrop()
                .into(imageView);

//        UrlImageViewHelper.setUrlDrawable(imageView, url, defaultResource, maxSize, new UrlImageViewCallback() {
//            @Override
//            public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
//                if (imageView == null || loadedBitmap == null)
//                    return;
//
//                int sz = loadedBitmap.getWidth();
//                if (loadedBitmap.getWidth() > loadedBitmap.getHeight()) {
//                    sz = loadedBitmap.getHeight();
//                }
//                loadedBitmap = ImageUtil.cutImage(loadedBitmap, sz, sz);
//                loadedBitmap = ImageUtil.getRoundedCornerBitmap(loadedBitmap, roundedCornerSize);
//
//                imageView.setImageBitmap(loadedBitmap);
//            }
//        });
    }

    /**
     * 获取圆图片
     */
    private void downLoadImageRound(ImageView imageView, String url, int defaultResource, int maxSize) {
        clearCache();
        if (CheckUtil.isEmpty(url)) {
            imageView.setImageResource(defaultResource);
            return;
        }

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.TRANSPARENT)
                .borderWidthDp(0)
                .cornerRadiusDp(300)
                .oval(false)
                .build();


        Picasso.with(Library.context).load(url)
                .placeholder(defaultResource)
                .error(defaultResource)
                .resize(maxSize, maxSize)
                .transform(transformation)
                .centerCrop()
                .into(imageView);


//        UrlImageViewHelper.setUrlDrawable(imageView, url, defaultResource, maxSize, new UrlImageViewCallback() {
//            @Override
//            public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
//                if (imageView == null || loadedBitmap == null)
//                    return;
//                loadedBitmap = ImageUtil.toRoundBitmap(loadedBitmap);
//                imageView.setImageBitmap(loadedBitmap);
//            }
//        });
    }

    private void clearCache() {
        if (PicassoTools.getCacheSize(Picasso.with(Library.context)) > 10000000) {
            PicassoTools.clearCache(Picasso.with(Library.context));
        }
    }
}
