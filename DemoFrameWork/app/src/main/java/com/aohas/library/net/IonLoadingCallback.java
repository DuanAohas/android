package com.aohas.library.net;

import java.util.List;
import java.util.Map;

/**
 * Created by liu_yu on 2014/4/16.
 * 请求回调方法
 */
public abstract class IonLoadingCallback {

    /**
     * 网络连接失败
     */
    public abstract void netWorkError();

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
