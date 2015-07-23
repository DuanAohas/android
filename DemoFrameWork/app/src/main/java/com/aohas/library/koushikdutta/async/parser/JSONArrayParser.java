package com.aohas.library.koushikdutta.async.parser;

import com.aohas.library.koushikdutta.async.DataEmitter;
import com.aohas.library.koushikdutta.async.DataSink;
import com.aohas.library.koushikdutta.async.callback.CompletedCallback;
import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.async.future.TransformFuture;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by koush on 5/27/13.
 */
public class JSONArrayParser implements AsyncParser<JSONArray> {
    @Override
    public Future<JSONArray> parse(DataEmitter emitter) {
        return new StringParser().parse(emitter)
        .then(new TransformFuture<JSONArray, String>() {
            @Override
            protected void transform(String result) throws Exception {
                setComplete(new JSONArray(result));
            }
        });
    }

    @Override
    public void write(DataSink sink, JSONArray value, CompletedCallback completed) {
        new StringParser().write(sink, value.toString(), completed);
    }
}
