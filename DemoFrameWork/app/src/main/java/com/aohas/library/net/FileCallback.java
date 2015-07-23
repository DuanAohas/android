package com.aohas.library.net;

import com.google.gson.JsonObject;

/**
 * Created by liuyu on 14-5-4.
 * 文件上传下载回调
 */
public abstract class FileCallback {

    public abstract void onError(Exception e);

    public abstract void onSuccess(int code, String result);

    public abstract void onProgress(long uploaded, long total);
}
