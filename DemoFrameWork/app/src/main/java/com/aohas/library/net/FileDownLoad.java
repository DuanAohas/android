package com.aohas.library.net;

import android.content.Context;
import com.google.gson.JsonObject;
import com.aohas.library.koushikdutta.async.future.FutureCallback;
import com.aohas.library.koushikdutta.ion.Ion;
import com.aohas.library.koushikdutta.ion.ProgressCallback;

import java.io.File;

/**
 * Created by liu_yu on 13-12-31.
 * 下载文件
 */
public class FileDownLoad {

    private static FileDownLoad fileDownLoad;

    public static FileDownLoad getInstance() {
        if (fileDownLoad == null) {
            fileDownLoad = new FileDownLoad();
        }
        return fileDownLoad;
    }

    /**
     * 下载文件
     *
     * @param context      上下文
     * @param url          文件地址
     * @param filePath     下载到本地地址
     * @param fileCallback 回调方法
     */
    public void requestDownloadFile(Context context, String url, String filePath, final FileCallback fileCallback) {
        requestDownloadFile(context, url, filePath, 60000, fileCallback);
    }


    /**
     * 下载文件
     *
     * @param context      上下文
     * @param url          文件地址
     * @param filePath     下载到本地地址
     * @param timeOut      超时（毫秒）
     * @param fileCallback 回调方法
     */
    public void requestDownloadFile(Context context, String url, String filePath, int timeOut, final FileCallback fileCallback) {
        Ion.with(context, url)
                .setTimeout(timeOut)
                .progress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        System.out.println("" + downloaded + " / " + total);
                        if (fileCallback != null)
                            fileCallback.onProgress(downloaded, total);

                    }
                })
                .write(new File(filePath))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        if (e != null) {
                            if (fileCallback != null)
                                fileCallback.onError(e);
                            return;
                        }
                        if (fileCallback != null) {
                            fileCallback.onSuccess(200, file.toString());
                        }

                    }
                });
    }
}
