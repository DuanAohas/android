package com.aohas.library.koushikdutta.async.http;

import com.aohas.library.koushikdutta.async.AsyncSocket;
import com.aohas.library.koushikdutta.async.DataEmitter;
import com.aohas.library.koushikdutta.async.DataSink;
import com.aohas.library.koushikdutta.async.callback.CompletedCallback;
import com.aohas.library.koushikdutta.async.http.libcore.ResponseHeaders;

public interface AsyncHttpResponse extends AsyncSocket {
    public void setEndCallback(CompletedCallback handler);
    public CompletedCallback getEndCallback();
    public ResponseHeaders getHeaders();
    public void end();
    public AsyncSocket detachSocket();
    public AsyncHttpRequest getRequest();
}
