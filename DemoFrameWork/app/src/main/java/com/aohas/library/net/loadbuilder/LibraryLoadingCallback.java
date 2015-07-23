package com.aohas.library.net.loadbuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by liu_yu on 2014/4/16.
 * 请求回调方法
 */
public abstract class LibraryLoadingCallback {
    /**
     * 返回数据
     *
     * @param e      请求异常
     * @param result 返回JSON数据
     */
    public abstract void onCompleted(Exception e, int code, String result);


    /**
     * 请求返回header信息
     */
    public void onHeaders(Map<String, List<String>> headerMap) {

    }
}
