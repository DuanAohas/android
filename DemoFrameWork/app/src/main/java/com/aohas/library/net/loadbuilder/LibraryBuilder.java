package com.aohas.library.net.loadbuilder;

import android.content.Context;
import com.google.gson.JsonObject;
import com.aohas.library.util.CheckUtil;
import com.aohas.library.util.Lg;
import com.aohas.library.util.MD5Util;

import java.util.Map;

/**
 * 网络操作类
 */
public class LibraryBuilder {
    protected final static int REQUEST_TYPE_GET = 101;
    protected final static int REQUEST_TYPE_DELETE = 102;
    protected final static int REQUEST_TYPE_POST = 103;
//    protected final static int REQUEST_TYPE_GET = 101;

    protected Context context;
    /**
     * 请求地址
     */
    protected String requestUrl;

    protected int requestType;

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
     * DELETE请求参数
     */
    protected Map<String, String> deleteObject;

    /**
     * header
     */
    protected Map<String, String> headerMap;

    /**
     * 是否开启转轮
     */
    protected boolean isShowBar = false;

    /**
     * 转轮提示文字
     */
    protected String barMessage;

    /**
     * 时候可以打点请求
     */
    protected boolean canCancel = false;

    /**
     * 点击返回是否可以退出当前界面
     */
    protected boolean canFinish = false;

    /**
     * 超时时间 毫秒
     */
    protected int timeOut;


    /**
     * 是否先获取本地数据
     */
    protected boolean localFirst = false;

    /**
     * 本地缓存时间
     */
    protected long exceedTime;


    protected LibraryBuilder(Context context, String url) {
        this.context = context;
        this.requestUrl = url;
    }

    public LibraryBuilder addHeader(Map<String, String> headerMap) {
        this.headerMap = headerMap;
        return this;
    }

    public LibraryBuilder setTimeout(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public LibraryBuilder showProgressBar() {
        this.isShowBar = true;
        return this;
    }

    public LibraryBuilder barMessage(String msg) {
        this.barMessage = msg;
        return this;
    }

    public LibraryBuilder barCanCancel() {
        this.canCancel = true;
        return this;
    }

    public LibraryBuilder barCanFinish() {
        this.canFinish = true;
        return this;
    }

    public LibraryBuilder localFirst() {
        this.localFirst = true;
        return this;
    }

    public LibraryBuilder setExceedTime(long exceedTime) {
        this.exceedTime = exceedTime;
        return this;
    }

    public LibraryFuture asGet(Map<String, String> getObject) {
        this.requestType = REQUEST_TYPE_GET;
        this.getObject = getObject;
        return new LibraryFuture(this);
    }

    public LibraryFuture asGet() {
        return asGet(null);
    }

    public LibraryFuture asDelete(Map<String, String> deleteObject) {
        this.requestType = REQUEST_TYPE_DELETE;
        this.deleteObject = deleteObject;
        return new LibraryFuture(this);
    }

    public LibraryFuture asDelete() {
        return asDelete(null);
    }

    public LibraryFuture asPostParameters(Map<String, String> parameters) {
        this.requestType = REQUEST_TYPE_POST;
        this.parameters = parameters;
        return new LibraryFuture(this);
    }

    public LibraryFuture asPostJson(JsonObject requestObject) {
        this.requestType = REQUEST_TYPE_POST;
        this.requestObject = requestObject;
        return new LibraryFuture(this);
    }

    public synchronized String getRequestUrl() {
        return this.requestUrl;
    }

    public synchronized JsonObject getRequestJSONObject() {
        return this.requestObject;
    }

    public synchronized Map<String, String> getParameters() {
        return this.parameters;
    }

    public synchronized Map<String, String> getGetObject() {
        return this.getObject;
    }

    @Override
    public String toString() {
        return MD5Util.getMD5String(requestUrl + parameters + requestObject + getObject + deleteObject + headerMap);
    }
}
