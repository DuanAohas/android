package com.aohas.library.koushikdutta.async.parser;

import com.aohas.library.koushikdutta.async.ByteBufferList;
import com.aohas.library.koushikdutta.async.DataEmitter;
import com.aohas.library.koushikdutta.async.DataSink;
import com.aohas.library.koushikdutta.async.callback.CompletedCallback;
import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.async.future.TransformFuture;
import com.aohas.library.koushikdutta.async.http.body.DocumentBody;
import com.aohas.library.koushikdutta.async.stream.ByteBufferListInputStream;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by koush on 8/3/13.
 */
public class DocumentParser implements AsyncParser<Document> {
    @Override
    public Future<Document> parse(DataEmitter emitter) {
        return new ByteBufferListParser().parse(emitter)
        .then(new TransformFuture<Document, ByteBufferList>() {
            @Override
            protected void transform(ByteBufferList result) throws Exception {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                setComplete(db.parse(new ByteBufferListInputStream(result)));
            }
        });
    }

    @Override
    public void write(DataSink sink, Document value, CompletedCallback completed) {
        new DocumentBody(value).write(null, sink, completed);
    }
}
