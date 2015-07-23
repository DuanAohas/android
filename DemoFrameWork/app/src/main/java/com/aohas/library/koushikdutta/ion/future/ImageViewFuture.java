package com.aohas.library.koushikdutta.ion.future;

import android.widget.ImageView;

import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.ion.ImageViewBitmapInfo;

/**
 * Created by koush on 7/1/14.
 */
public interface ImageViewFuture extends Future<ImageView> {
    Future<ImageViewBitmapInfo> withBitmapInfo();
}
