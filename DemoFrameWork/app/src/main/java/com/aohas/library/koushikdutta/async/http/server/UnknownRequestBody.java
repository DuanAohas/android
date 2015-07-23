package com.aohas.library.koushikdutta.async.http.server;

import com.aohas.library.koushikdutta.async.DataEmitter;
import com.aohas.library.koushikdutta.async.DataSink;
import com.aohas.library.koushikdutta.async.NullDataCallback;
import com.aohas.library.koushikdutta.async.Util;
import com.aohas.library.koushikdutta.async.callback.CompletedCallback;
import com.aohas.library.koushikdutta.async.callback.DataCallback;
import com.aohas.library.koushikdutta.async.http.AsyncHttpRequest;
import com.aohas.library.koushikdutta.async.http.body.AsyncHttpRequestBody;

public class UnknownRequestBody implements AsyncHttpRequestBody<Void> {
    public UnknownRequestBody(String contentType) {
        mContentType = contentType;
    }

    int length = -1;
    public UnknownRequestBody(DataEmitter emitter, String contentType, int length) {
        mContentType = contentType;
        this.emitter = emitter;
        this.length = length;
    }

    @Override
    public void write(final AsyncHttpRequest request, DataSink sink, final CompletedCallback completed) {
        Util.pump(emitter, sink, completed);
        if (emitter.isPaused())
            emitter.resume();
    }

    private String mContentType;
    @Override
    public String getContentType() {
        return mContentType;
    }

    @Override
    public boolean readFullyOnRequest() {
        return false;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public Void get() {
        return null;
    }

    @Deprecated
    public void setCallbacks(DataCallback callback, CompletedCallback endCallback) {
        emitter.setEndCallback(endCallback);
        emitter.setDataCallback(callback);
    }

    public DataEmitter getEmitter() {
        return emitter;
    }

    DataEmitter emitter;
    @Override
    public void parse(DataEmitter emitter, CompletedCallback completed) {
        this.emitter = emitter;
        emitter.setEndCallback(completed);
        emitter.setDataCallback(new NullDataCallback());
    }
}
