package com.aohas.library.koushikdutta.async.wrapper;

import com.aohas.library.koushikdutta.async.AsyncSocket;

public interface AsyncSocketWrapper extends AsyncSocket, DataEmitterWrapper {
    public AsyncSocket getSocket();
}
