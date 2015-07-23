package com.aohas.library.net;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by liu_yu on 2014/4/16.
 * 请求协议实体
 */
public class IonEntity {

    public static final int HTTP_TIMEOUT = 30000;

    /**
     * 请求地址
     */
    protected String requestUrl;

    /**
     * 表单请求参数
     */
    protected Map<String, String> parameters;

    /**
     * JSON请求参数
     */
    protected JsonObject requestObject;

    /**
     * GET请求参数
     */
    protected Map<String, String> getObject;

    /**
     * header
     */
    protected Map<String, String> headerMap;

    /**
     * 是否开启转轮
     */
    protected boolean isShowBar = true;

    /**
     * 转轮提示文字
     */
    protected String barMessage;

    /**
     * 时候可以打点请求
     */
    protected boolean canCancel = true;

    /**
     * 点击返回是否可以退出当前界面
     */
    protected boolean canFinish = false;

    protected int timeOut;

    /**
     * GET请求
     *
     * @param requestUrl 请求地址
     * @param isShowBar  是否开启转轮
     * @param barMessage 转轮提示
     * @param canCancel  是否可以打断请求
     * @param canFinish  打断请求时，是否关掉当前界面
     */
    @Deprecated
    public IonEntity(String requestUrl, boolean isShowBar, String barMessage, boolean canCancel, boolean canFinish) {
        this(requestUrl, null, null, null, HTTP_TIMEOUT, isShowBar, barMessage, canCancel, canFinish);
    }

    /**
     * GET请求
     *
     * @param requestUrl 请求地址
     * @param timeOut    超时时间（单位-毫秒）
     * @param isShowBar  是否开启转轮
     * @param barMessage 转轮提示
     * @param canCancel  是否可以打断请求
     * @param canFinish  打断请求时，是否关掉当前界面
     */
    @Deprecated
    public IonEntity(String requestUrl, int timeOut, boolean isShowBar, String barMessage, boolean canCancel, boolean canFinish) {
        this(requestUrl, null, null, null, timeOut, isShowBar, barMessage, canCancel, canFinish);
    }

    /**
     * GET请求/表单提交
     *
     * @param requestUrl 请求地址
     * @param isShowBar  是否开启转轮
     * @param barMessage 转轮提示
     * @param canCancel  是否可以打断请求
     * @param canFinish  打断请求时，是否关掉当前界面
     */
    public IonEntity(String requestUrl, Map<String, String> object, boolean isGet, boolean isShowBar, String barMessage, boolean canCancel, boolean canFinish) {
        this(requestUrl, isGet ? object : null, isGet ? null : object, null, HTTP_TIMEOUT, isShowBar, barMessage, canCancel, canFinish);
    }

    /**
     * GET请求/表单提交
     *
     * @param requestUrl 请求地址
     * @param timeOut    超时时间（单位-毫秒）
     * @param isShowBar  是否开启转轮
     * @param barMessage 转轮提示
     * @param canCancel  是否可以打断请求
     * @param canFinish  打断请求时，是否关掉当前界面
     */
    public IonEntity(String requestUrl, Map<String, String> object, boolean isGet, int timeOut, boolean isShowBar, String barMessage, boolean canCancel, boolean canFinish) {
        this(requestUrl, isGet ? object : null, isGet ? null : object, null, timeOut, isShowBar, barMessage, canCancel, canFinish);
    }

    /**
     * 表单提交
     *
     * @param requestUrl 请求地址
     * @param parameters 表单数据
     * @param isShowBar  是否开启转轮
     * @param barMessage 转轮提示
     * @param canCancel  是否可以打断请求
     * @param canFinish  打断请求时，是否关掉当前界面
     */
    @Deprecated
    public IonEntity(String requestUrl, Map<String, String> parameters, boolean isShowBar, String barMessage, boolean canCancel, boolean canFinish) {
        this(requestUrl, null, parameters, null, HTTP_TIMEOUT, isShowBar, barMessage, canCancel, canFinish);
    }

    /**
     * 表单提交
     *
     * @param requestUrl 请求地址
     * @param parameters 表单数据
     * @param timeOut    超时时间（单位-毫秒）
     * @param isShowBar  是否开启转轮
     * @param barMessage 转轮提示
     * @param canCancel  是否可以打断请求
     * @param canFinish  打断请求时，是否关掉当前界面
     */
    @Deprecated
    public IonEntity(String requestUrl, Map<String, String> parameters, int timeOut, boolean isShowBar, String barMessage, boolean canCancel, boolean canFinish) {
        this(requestUrl, null, parameters, null, timeOut, isShowBar, barMessage, canCancel, canFinish);
    }

    /**
     * JSON提交
     *
     * @param requestUrl    请求地址
     * @param requestObject JSON数据
     * @param isShowBar     是否开启转轮
     * @param barMessage    转轮提示
     * @param canCancel     是否可以打断请求
     * @param canFinish     打断请求时，是否关掉当前界面
     */
    public IonEntity(String requestUrl, JsonObject requestObject, boolean isShowBar, String barMessage, boolean canCancel, boolean canFinish) {
        this(requestUrl, null, null, requestObject, HTTP_TIMEOUT, isShowBar, barMessage, canCancel, canFinish);
    }

    /**
     * JSON提交
     *
     * @param requestUrl    请求地址
     * @param requestObject JSON数据
     * @param timeOut       超时时间（单位-毫秒）
     * @param isShowBar     是否开启转轮
     * @param barMessage    转轮提示
     * @param canCancel     是否可以打断请求
     * @param canFinish     打断请求时，是否关掉当前界面
     */
    public IonEntity(String requestUrl, JsonObject requestObject, int timeOut, boolean isShowBar, String barMessage, boolean canCancel, boolean canFinish) {
        this(requestUrl, null, null, requestObject, timeOut, isShowBar, barMessage, canCancel, canFinish);
    }

    private IonEntity(String requestUrl, Map<String, String> getObject, Map<String, String> parameters, JsonObject requestObject, int timeOut, boolean isShowBar, String barMessage, boolean canCancel, boolean canFinish) {
        this.requestUrl = requestUrl;
        this.getObject = getObject;
        this.parameters = parameters;
        this.requestObject = requestObject;
        this.timeOut = timeOut;
        this.isShowBar = isShowBar;
        this.barMessage = barMessage;
        this.canCancel = canCancel;
        this.canFinish = canFinish;
    }

    public void setCookie(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestJSONObject(JsonObject requestObject) {
        this.requestObject = requestObject;
    }

    public JsonObject getRequestJSONObject() {
        return requestObject;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<String, String> getGetObject() {
        return getObject;
    }
    /**
     * 调用请求方法
     *
     * @param activity    当前Activit
     * @param ionCallback 回调方法
     */
//    public void request(Activity activity, IonLoadingCallback ionCallback) {
//        IonLoading.getInstance().request(activity, this, ionCallback);
//    }

}
