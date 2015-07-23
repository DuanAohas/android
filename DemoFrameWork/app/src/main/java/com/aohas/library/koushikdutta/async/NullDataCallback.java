package com.aohas.library.koushikdutta.async;

import com.aohas.library.koushikdutta.async.callback.DataCallback;

public class NullDataCallback implements DataCallback {
    @Override
    public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
        bb.recycle();
    }
}
