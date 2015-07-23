package com.aohas.library.koushikdutta.async.callback;


import com.aohas.library.koushikdutta.async.AsyncSocket;

public interface ConnectCallback {
    public void onConnectCompleted(Exception ex, AsyncSocket socket);
}
