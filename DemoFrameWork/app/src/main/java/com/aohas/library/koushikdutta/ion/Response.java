package com.aohas.library.koushikdutta.ion;

import com.aohas.library.koushikdutta.async.http.AsyncHttpRequest;
import com.aohas.library.koushikdutta.async.http.libcore.RawHeaders;

/**
 * Created by koush on 7/6/13.
 */
public class Response<T> {
    T result;
    public T getResult() {
        return result;
    }

    Exception exception;
    public Exception getException() {
        return exception;
    }

    RawHeaders headers;
    public RawHeaders getHeaders() {
        return headers;
    }

    AsyncHttpRequest request;
    public AsyncHttpRequest getRequest() {
        return request;
    }
}
