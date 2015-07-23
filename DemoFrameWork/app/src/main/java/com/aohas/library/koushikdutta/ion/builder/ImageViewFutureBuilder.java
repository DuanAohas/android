package com.aohas.library.koushikdutta.ion.builder;

import android.widget.ImageView;

import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.ion.future.ImageViewFuture;

/**
* Created by koush on 5/30/13.
*/
public interface ImageViewFutureBuilder {
    /**
     * Perform the request and get the result as a Bitmap, which will then be loaded
     * into the given ImageView
     * @param imageView ImageView to set once the request completes
     * @return
     */
    public ImageViewFuture intoImageView(ImageView imageView);
}
