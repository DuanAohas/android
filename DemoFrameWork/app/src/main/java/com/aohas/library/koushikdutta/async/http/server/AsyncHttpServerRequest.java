package com.aohas.library.koushikdutta.async.http.server;

import com.aohas.library.koushikdutta.async.AsyncSocket;
import com.aohas.library.koushikdutta.async.DataEmitter;
import com.aohas.library.koushikdutta.async.http.body.AsyncHttpRequestBody;
import com.aohas.library.koushikdutta.async.http.Multimap;
import com.aohas.library.koushikdutta.async.http.libcore.RequestHeaders;

import java.util.regex.Matcher;

public interface AsyncHttpServerRequest extends DataEmitter {
    public RequestHeaders getHeaders();
    public Matcher getMatcher();
    public AsyncHttpRequestBody getBody();
    public AsyncSocket getSocket();
    public String getPath();
    public Multimap getQuery();
    public String getMethod();
}
