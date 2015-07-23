package com.aohas.library.koushikdutta.ion;

import com.aohas.library.koushikdutta.async.ByteBufferList;
import com.aohas.library.koushikdutta.async.DataEmitter;
import com.aohas.library.koushikdutta.async.DataSink;
import com.aohas.library.koushikdutta.async.callback.CompletedCallback;
import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.async.future.TransformFuture;
import com.aohas.library.koushikdutta.async.parser.AsyncParser;
import com.aohas.library.koushikdutta.async.parser.ByteBufferListParser;
import com.aohas.library.koushikdutta.async.stream.ByteBufferListInputStream;

import java.io.InputStream;

/**
 * Created by koush on 11/3/13.
 */
public class InputStreamParser implements AsyncParser<InputStream> {
    @Override
    public Future<InputStream> parse(DataEmitter emitter) {
        return new ByteBufferListParser().parse(emitter)
        .then(new TransformFuture<InputStream, ByteBufferList>() {
            @Override
            protected void transform(ByteBufferList result) throws Exception {
                setComplete(new ByteBufferListInputStream(result));
            }
        });
    }

    @Override
    public void write(DataSink sink, InputStream value, CompletedCallback completed) {
        throw new AssertionError("not implemented");
    }
}
