package com.aohas.library.koushikdutta.ion.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.aohas.library.koushikdutta.async.ByteBufferList;
import com.aohas.library.koushikdutta.async.DataEmitter;
import com.aohas.library.koushikdutta.async.DataSink;
import com.aohas.library.koushikdutta.async.Util;
import com.aohas.library.koushikdutta.async.callback.CompletedCallback;
import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.async.future.TransformFuture;
import com.aohas.library.koushikdutta.async.parser.AsyncParser;
import com.aohas.library.koushikdutta.async.parser.ByteBufferListParser;
import com.aohas.library.koushikdutta.async.stream.ByteBufferListInputStream;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

/**
 * Created by koush on 6/1/13.
 */
public class GsonSerializer<T> implements AsyncParser<T> {
    Gson gson;
    Type type;
    public GsonSerializer(Gson gson, Class<T> clazz) {
        this.gson = gson;
        type = clazz;
    }
    public GsonSerializer(Gson gson, TypeToken<T> token) {
        this.gson = gson;
        type = token.getType();
    }
    @Override
    public Future<T> parse(DataEmitter emitter) {
        return new ByteBufferListParser().parse(emitter)
        .then(new TransformFuture<T, ByteBufferList>() {
            @Override
            protected void transform(ByteBufferList result) throws Exception {
                ByteBufferListInputStream bin = new ByteBufferListInputStream(result);
                T ret = (T)gson.fromJson(new JsonReader(new InputStreamReader(bin)), type);
                setComplete(ret);
            }
        });
    }

    @Override
    public void write(DataSink sink, T pojo, CompletedCallback completed) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        OutputStreamWriter out = new OutputStreamWriter(bout);
        gson.toJson(pojo, type, out);
        try {
            out.flush();
        }
        catch (final Exception e) {
            throw new AssertionError(e);
        }
        Util.writeAll(sink, bout.toByteArray(), completed);
    }
}
