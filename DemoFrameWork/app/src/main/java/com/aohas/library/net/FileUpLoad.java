package com.aohas.library.net;

import android.content.Context;
import android.text.TextUtils;
import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.async.future.FutureCallback;
import com.aohas.library.koushikdutta.ion.Ion;
import com.aohas.library.koushikdutta.ion.ProgressCallback;
import com.aohas.library.koushikdutta.ion.Response;
import com.aohas.library.koushikdutta.ion.builder.Builders;
import com.aohas.library.util.CheckUtil;

import java.io.File;
import java.util.*;

/**
 * Created by liu_yu on 2014/3/31.
 * 上传文件
 */
public class FileUpLoad {

    private static FileUpLoad fileUpLoad;

    public static FileUpLoad getInstance() {
        if (fileUpLoad == null) {
            fileUpLoad = new FileUpLoad();
        }
        return fileUpLoad;
    }

    /**
     * 上传文件
     *
     * @param context      上下文
     * @param url          文件地址
     * @param file         文件本地地址
     * @param fileCallback 回调方法
     */
    public void upLoadImage(Context context, String url, String file, final FileCallback fileCallback) {
        upLoadImage(context, url, file, new File(file).getName(), null, 60000, fileCallback);
    }

    /**
     * 上传文件
     *
     * @param context      上下文
     * @param url          文件地址
     * @param file         文件本地地址
     * @param fileCallback 回调方法
     */
    public void upLoadImage(Context context, String url, String file, String fileName, final FileCallback fileCallback) {
        upLoadImage(context, url, file, fileName, null, 60000, fileCallback);
    }

    /**
     * 上传文件
     *
     * @param context      上下文
     * @param url          文件地址
     * @param file         文件本地地址
     * @param fileCallback 回调方法
     */
    public void upLoadImage(Context context, String url, String file, Map<String, String> parameters, final FileCallback fileCallback) {
        upLoadImage(context, url, file, new File(file).getName(), parameters, 60000, fileCallback);
    }

    /**
     * 上传文件
     *
     * @param context      上下文
     * @param url          文件地址
     * @param file         文件本地地址
     * @param fileCallback 回调方法
     */
    public void upLoadImage(Context context, String url, String file, String fileName, Map<String, String> parameters, final FileCallback fileCallback) {
        upLoadImage(context, url, file, fileName, parameters, 60000, fileCallback);
    }

    /**
     * 上传文件
     *
     * @param context      上下文
     * @param url          文件地址
     * @param file         文件本地地址
     * @param timeOut      超时（毫秒）
     * @param fileCallback 回调方法
     */
    public void upLoadImage(Context context, String url, String file, String fileName, Map<String, String> parameters, int timeOut, final FileCallback fileCallback) {
        if (context == null || fileCallback == null || TextUtils.isEmpty(url) || TextUtils.isEmpty(file)) {
            return;
        }

        Builders.Any.B ion = Ion.with(context, url);
        ion.setTimeout(timeOut);

        if (!CheckUtil.isEmpty(parameters)) {
            Map<String, List<String>> map = new HashMap<String, List<String>>();
            Set<String> set = parameters.keySet();
            for (String key : set) {
                List<String> stringList = new ArrayList<String>();
                stringList.add(parameters.get(key));
                map.put(key, stringList);
            }
            ion.setMultipartParameters(map);
        }
        ion.uploadProgressHandler(new ProgressCallback() {
            @Override
            public void onProgress(long downloaded, long total) {
                if (fileCallback != null)
                    fileCallback.onProgress(downloaded, total);
            }
        });
        ion.setMultipartFile(fileName, new File(file));
        Future<Response<String>> json = ion.asString().withResponse();
        json.setCallback(new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> _result) {
                if (e != null) {
                    if (fileCallback != null)
                        fileCallback.onError(e);
                    return;
                }
                if (fileCallback != null) {
                    int code = -1;
                    String result = null;
                    if (_result != null) {
                        code = _result.getHeaders().getResponseCode();
                        result = _result.getResult();
                    }
                    fileCallback.onSuccess(code, result);
                }
            }
        });


    }

}
