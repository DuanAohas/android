package com.aohas.library.koushikdutta.async.http.socketio;

public interface ConnectCallback {
    public void onConnectCompleted(Exception ex, SocketIOClient client);
}