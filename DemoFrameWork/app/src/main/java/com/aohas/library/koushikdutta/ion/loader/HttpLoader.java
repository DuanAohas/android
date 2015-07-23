package com.aohas.library.koushikdutta.ion.loader;

import android.text.TextUtils;

import com.aohas.library.koushikdutta.async.DataEmitter;
import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.async.future.FutureCallback;
import com.aohas.library.koushikdutta.async.http.AsyncHttpRequest;
import com.aohas.library.koushikdutta.async.http.AsyncHttpResponse;
import com.aohas.library.koushikdutta.async.http.ResponseCacheMiddleware;
import com.aohas.library.koushikdutta.async.http.callback.HttpConnectCallback;
import com.aohas.library.koushikdutta.async.http.libcore.RawHeaders;
import com.aohas.library.koushikdutta.ion.Ion;

/**
 * Created by koush on 5/22/13.
 */
public class HttpLoader extends SimpleLoader {
    @SuppressWarnings("unchecked")
    @Override
    public Future<DataEmitter> load(Ion ion, AsyncHttpRequest request, final FutureCallback<LoaderEmitter> callback) {
        if (!request.getUri().getScheme().startsWith("http"))
            return null;
        return (Future< DataEmitter >)(Future)ion.getHttpClient().execute(request, new HttpConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, AsyncHttpResponse response) {
                long length = -1;
                int loadedFrom = LoaderEmitter.LOADED_FROM_NETWORK;
                RawHeaders headers = null;
                AsyncHttpRequest request = null;
                if (response != null) {
                    request = response.getRequest();
                    headers = response.getHeaders().getHeaders();
                    length = response.getHeaders().getContentLength();
                    String servedFrom = response.getHeaders().getHeaders().get(ResponseCacheMiddleware.SERVED_FROM);
                    if (TextUtils.equals(servedFrom, ResponseCacheMiddleware.CACHE))
                        loadedFrom = LoaderEmitter.LOADED_FROM_CACHE;
                    else if (TextUtils.equals(servedFrom, ResponseCacheMiddleware.CONDITIONAL_CACHE))
                        loadedFrom = LoaderEmitter.LOADED_FROM_CONDITIONAL_CACHE;
                }
                callback.onCompleted(ex, new LoaderEmitter(response, length, loadedFrom, headers, request));
            }
        });
    }
}
