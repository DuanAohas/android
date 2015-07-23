package com.aohas.library.koushikdutta.async.callback;

import com.aohas.library.koushikdutta.async.AsyncServerSocket;
import com.aohas.library.koushikdutta.async.AsyncSocket;

public interface ListenCallback extends CompletedCallback {
    public void onAccepted(AsyncSocket socket);
    public void onListening(AsyncServerSocket socket);
}
