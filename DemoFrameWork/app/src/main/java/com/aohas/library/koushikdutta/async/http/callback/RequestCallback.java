package com.aohas.library.koushikdutta.async.http.callback;

import com.aohas.library.koushikdutta.async.callback.ResultCallback;
import com.aohas.library.koushikdutta.async.http.AsyncHttpResponse;

public interface RequestCallback<T> extends ResultCallback<AsyncHttpResponse, T> {
    public void onConnect(AsyncHttpResponse response);
    public void onProgress(AsyncHttpResponse response, long downloaded, long total);
}
