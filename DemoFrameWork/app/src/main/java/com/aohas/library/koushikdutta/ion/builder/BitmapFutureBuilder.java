package com.aohas.library.koushikdutta.ion.builder;

import android.graphics.Bitmap;

import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.ion.bitmap.BitmapInfo;
import com.aohas.library.koushikdutta.ion.bitmap.LocallyCachedStatus;

/**
* Created by koush on 5/30/13.
*/
public interface BitmapFutureBuilder {
    /**
     * Perform the request and get the result as a Bitmap
     * @return
     */
    public Future<Bitmap> asBitmap();

    /**
     * Attempt to immediately retrieve the cached Bitmap info from the memory cache
     * @return
     */
    public BitmapInfo asCachedBitmap();

    /**
     * Check whether the Bitmap can be loaded from either the file or memory cache
     * @return
     */
    public LocallyCachedStatus isLocallyCached();
}
