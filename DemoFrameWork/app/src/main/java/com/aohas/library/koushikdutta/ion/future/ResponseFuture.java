package com.aohas.library.koushikdutta.ion.future;

import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.ion.Response;

/**
 * Created by koush on 7/2/13.
 */
public interface ResponseFuture<T> extends Future<T> {
    Future<Response<T>> withResponse();
}
