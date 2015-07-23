package com.aohas.library.koushikdutta.async.callback;

public interface ResultCallback<S, T> {
    public void onCompleted(Exception e, S source, T result);
}
