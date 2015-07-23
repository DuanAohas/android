package com.aohas.library.koushikdutta.async.callback;

import com.aohas.library.koushikdutta.async.future.Continuation;

public interface ContinuationCallback {
    public void onContinue(Continuation continuation, CompletedCallback next) throws Exception;
}
