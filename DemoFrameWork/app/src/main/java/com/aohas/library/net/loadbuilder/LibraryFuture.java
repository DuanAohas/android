package com.aohas.library.net.loadbuilder;

/**
 * 回调类
 */
public class LibraryFuture {
    LibraryBuilder builder;

    protected LibraryFuture(LibraryBuilder builder) {
        this.builder = builder;
    }

    public void setCallback(LibraryLoadingCallback ionLoadingCallback) {
        LibraryLoading.getInstence().request(builder, ionLoadingCallback);
    }
}
