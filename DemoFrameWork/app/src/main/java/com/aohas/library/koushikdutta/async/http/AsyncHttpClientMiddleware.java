package com.aohas.library.koushikdutta.async.http;

import com.aohas.library.koushikdutta.async.AsyncSocket;
import com.aohas.library.koushikdutta.async.DataEmitter;
import com.aohas.library.koushikdutta.async.callback.ConnectCallback;
import com.aohas.library.koushikdutta.async.future.Cancellable;
import com.aohas.library.koushikdutta.async.http.libcore.ResponseHeaders;
import com.aohas.library.koushikdutta.async.util.UntypedHashtable;

import java.util.Hashtable;

public interface AsyncHttpClientMiddleware {
    public static class GetSocketData {
        public UntypedHashtable state = new UntypedHashtable();
        public AsyncHttpRequest request;
        public ConnectCallback connectCallback;
        public Cancellable socketCancellable;
    }
    
    public static class OnSocketData extends GetSocketData {
        public AsyncSocket socket;
    }
    
    public static class OnHeadersReceivedData extends OnSocketData {
        public ResponseHeaders headers;
    }
    
    public static class OnBodyData extends OnHeadersReceivedData {
        public DataEmitter bodyEmitter;
    }

    public static class OnRequestCompleteData extends OnBodyData {
        public Exception exception;
    }

    public Cancellable getSocket(GetSocketData data);
    public void onSocket(OnSocketData data);
    public void onHeadersReceived(OnHeadersReceivedData data);
    public void onBodyDecoder(OnBodyData data);
    public void onRequestComplete(OnRequestCompleteData data);
}
