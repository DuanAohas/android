package com.aohas.library.koushikdutta.ion.loader;

import android.net.Uri;

import com.aohas.library.koushikdutta.async.http.AsyncHttpRequest;
import com.aohas.library.koushikdutta.async.http.libcore.RawHeaders;

/**
 * Created by koush on 7/15/13.
 */
public interface AsyncHttpRequestFactory {
    public AsyncHttpRequest createAsyncHttpRequest(Uri uri, String method, RawHeaders headers);
}
