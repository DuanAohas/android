package com.aohas.library.koushikdutta.ion.loader;

import android.content.Context;

import com.aohas.library.koushikdutta.async.DataEmitter;
import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.async.future.FutureCallback;
import com.aohas.library.koushikdutta.async.http.AsyncHttpRequest;
import com.aohas.library.koushikdutta.ion.Ion;
import com.aohas.library.koushikdutta.ion.Loader;
import com.aohas.library.koushikdutta.ion.bitmap.BitmapInfo;

import java.io.InputStream;

/**
 * Created by koush on 12/22/13.
 */
public class SimpleLoader implements Loader {
    @Override
    public Future<InputStream> load(Ion ion, AsyncHttpRequest request) {
        return null;
    }

    @Override
    public Future<DataEmitter> load(Ion ion, AsyncHttpRequest request, FutureCallback<LoaderEmitter> callback) {
        return null;
    }

    @Override
    public Future<BitmapInfo> loadBitmap(Context context, Ion ion, String key, String uri, int resizeWidth, int resizeHeight, boolean animateGif) {
        return null;
    }

    @Override
    public Future<AsyncHttpRequest> resolve(Context context, Ion ion, AsyncHttpRequest request) {
        return null;
    }
}
