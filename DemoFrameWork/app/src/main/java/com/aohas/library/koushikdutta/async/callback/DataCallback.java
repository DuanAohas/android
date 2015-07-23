package com.aohas.library.koushikdutta.async.callback;


import com.aohas.library.koushikdutta.async.ByteBufferList;
import com.aohas.library.koushikdutta.async.DataEmitter;

public interface DataCallback {
    public void onDataAvailable(DataEmitter emitter, ByteBufferList bb);
}
