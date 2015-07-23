package com.aohas.library.koushikdutta.async.parser;

import com.aohas.library.koushikdutta.async.ByteBufferList;
import com.aohas.library.koushikdutta.async.DataEmitter;
import com.aohas.library.koushikdutta.async.DataSink;
import com.aohas.library.koushikdutta.async.callback.CompletedCallback;
import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.async.future.TransformFuture;
import com.aohas.library.koushikdutta.async.stream.ByteBufferListInputStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by koush on 5/27/13.
 */
public class StringParser implements AsyncParser<String> {
    @Override
    public Future<String> parse(DataEmitter emitter) {
        final String charset = emitter.charset() == null ? Charset.defaultCharset().name() : emitter.charset();
        return new ByteBufferListParser().parse(emitter)
                .then(new TransformFuture<String, ByteBufferList>() {
                    @Override
                    protected void transform(ByteBufferList _result) throws Exception {
                        InputStreamReader inputStreamReader = new InputStreamReader(new ByteBufferListInputStream(_result), charset);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder result = new StringBuilder();
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null && !"".equals(line)) {
                            result.append(line);
                        }
                        inputStreamReader.close();
                        bufferedReader.close();
                        setComplete(result.toString());
//                        setComplete(result.readString(Charset.forName(charset)));
                    }
                });
    }

    @Override
    public void write(DataSink sink, String value, CompletedCallback completed) {
        new ByteBufferListParser().write(sink, new ByteBufferList(value.getBytes()), completed);
    }
}
