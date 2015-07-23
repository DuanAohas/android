package com.aohas.library.koushikdutta.async.http.body;

import com.aohas.library.koushikdutta.async.DataEmitter;
import com.aohas.library.koushikdutta.async.DataSink;
import com.aohas.library.koushikdutta.async.callback.CompletedCallback;
import com.aohas.library.koushikdutta.async.http.AsyncHttpRequest;

public interface AsyncHttpRequestBody<T> {
    public void write(AsyncHttpRequest request, DataSink sink, CompletedCallback completed);
    public void parse(DataEmitter emitter, CompletedCallback completed);
    public String getContentType();
    public boolean readFullyOnRequest();
    public int length();
    public T get();
}
