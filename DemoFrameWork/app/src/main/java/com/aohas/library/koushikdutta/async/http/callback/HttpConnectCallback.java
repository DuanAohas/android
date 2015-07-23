package com.aohas.library.koushikdutta.async.http.callback;


import com.aohas.library.koushikdutta.async.http.AsyncHttpResponse;

public interface HttpConnectCallback {
    public void onConnectCompleted(Exception ex, AsyncHttpResponse response);
}
