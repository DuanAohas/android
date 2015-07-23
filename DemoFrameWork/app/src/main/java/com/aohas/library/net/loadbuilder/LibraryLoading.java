package com.aohas.library.net.loadbuilder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.async.future.FutureCallback;
import com.aohas.library.koushikdutta.async.http.libcore.RawHeaders;
import com.aohas.library.koushikdutta.ion.HeadersCallback;
import com.aohas.library.koushikdutta.ion.Ion;
import com.aohas.library.koushikdutta.ion.Response;
import com.aohas.library.koushikdutta.ion.builder.Builders;
import com.aohas.library.Library;
import com.aohas.library.ui.util.RelayoutViewTool;
import com.aohas.library.ui.util.UIDialogUtil;
import com.aohas.library.util.Lg;
import com.aohas.library.util.NetWorkUtil;
import com.aohas.library.util.ParcelUtil;

import java.util.*;

/**
 * 网络操作实现类
 */
public class LibraryLoading {

    private Dialog progressBarDialog;
    private Future<Response<String>> future = null;

    private LibraryLoading() {
    }

    private static LibraryLoading libraryLoading;

    protected static synchronized LibraryLoading getInstence() {
        if (libraryLoading == null)
            libraryLoading = new LibraryLoading();
        return libraryLoading;
    }

    /**
     * 请求入口
     *
     * @param builder            请求实体
     * @param ionLoadingCallback 回调方法
     */
    protected void request(LibraryBuilder builder, final LibraryLoadingCallback ionLoadingCallback) {
        if (builder == null) {
            Toast.makeText(Library.context, "请传入请求参数", Toast.LENGTH_SHORT).show();
            return;
        }

        //判断网络是否正常
        if (!NetWorkUtil.networkCanUse(Library.context)) {
            ionLoadingCallback.onCompleted(new Exception("net error"), -1, null);
        }

        //判断是否需要弹起转轮
        if (builder.isShowBar) {
            if (builder.context instanceof Activity)
                startProgressBar((Activity) builder.context, builder.barMessage, builder.canCancel, builder.canFinish);
            else
                Lg.e("LibraryLoading", "you want to bounce a progressBar buy context is not activity");
        }
        new LoadingTask().execute(builder, ionLoadingCallback);
    }


    private class LoadingTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object[] params) {
            LibraryBuilder builder = (LibraryBuilder) params[0];
            final LibraryLoadingCallback ionLoadingCallback = (LibraryLoadingCallback) params[1];
            Future<Response<String>> future = null;
            //根据parameters和requestObject判断需要什么请求方式
            if (builder.requestType == LibraryBuilder.REQUEST_TYPE_GET) {
                future = requestGet(Library.context, builder, ionLoadingCallback);
            } else if (builder.requestType == LibraryBuilder.REQUEST_TYPE_DELETE) {
                future = requestDelete(Library.context, builder, ionLoadingCallback);
            } else if (builder.requestType == LibraryBuilder.REQUEST_TYPE_POST) {
                future = requestPost(builder, ionLoadingCallback);
            }

            if (future == null) {
                Toast.makeText(Library.context, "请求错误", Toast.LENGTH_SHORT).show();
                return null;
            }
            future.setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> _result) {
                    closeProgressBar();
                    int code = -1;
                    String result = null;
                    if (_result != null) {
                        code = _result.getHeaders().getResponseCode();
                        result = _result.getResult();
                    }
                    ionLoadingCallback.onCompleted(e, code, result);
                }
            });
            return null;
        }
    }

    /**
     * DELETE请求
     *
     * @param context            上下文
     * @param builder            请求实体
     * @param ionLoadingCallback 回调方法
     */
    private Future<Response<String>> requestDelete(Context context, LibraryBuilder builder, final LibraryLoadingCallback ionLoadingCallback) {
        String paramStr = prepareParam(builder.getObject);
        if (paramStr == null || paramStr.trim().length() < 1) {

        } else {
            builder.requestUrl += "?" + paramStr;
        }

        Builders.Any.B ion = Ion.with(context).load("DELETE", builder.requestUrl);
        ion.onHeaders(new HeadersCallback() {
            @Override
            public void onHeaders(RawHeaders rawHeaders) {
                Map<String, List<String>> map = rawHeaders.toMultimap();
                ionLoadingCallback.onHeaders(map);
            }
        });
        ion.setTimeout(builder.timeOut);
        if (builder.headerMap != null) {
            Set<String> keys = builder.headerMap.keySet();
            for (String key : keys) {
                ion.addHeader(key, builder.headerMap.get(key));
            }
        }
        Future<Response<String>> future = ion.asString().withResponse();
        return future;
    }

    /**
     * GET请求
     *
     * @param context            上下文
     * @param builder            请求实体
     * @param ionLoadingCallback 回调方法
     */
    private Future<Response<String>> requestGet(Context context, LibraryBuilder builder, final LibraryLoadingCallback ionLoadingCallback) {
        String paramStr = prepareParam(builder.getObject);
        if (paramStr == null || paramStr.trim().length() < 1) {

        } else {
            builder.requestUrl += "?" + paramStr;
        }

        Builders.Any.B ion = Ion.with(context).load("GET", builder.requestUrl);
        ion.onHeaders(new HeadersCallback() {
            @Override
            public void onHeaders(RawHeaders rawHeaders) {
                Map<String, List<String>> map = rawHeaders.toMultimap();
                ionLoadingCallback.onHeaders(map);
            }
        });
        ion.setTimeout(builder.timeOut);
        if (builder.headerMap != null) {
            Set<String> keys = builder.headerMap.keySet();
            for (String key : keys) {
                ion.addHeader(key, builder.headerMap.get(key));
            }
        }
        Future<Response<String>> future = ion.asString().withResponse();
        return future;
    }

    private Future<Response<String>> requestPost(LibraryBuilder builder, final LibraryLoadingCallback ionLoadingCallback) {
        if (builder.parameters != null && builder.requestObject == null) {
            future = requestParameter(Library.context, builder, ionLoadingCallback);
        } else if (builder.parameters == null && builder.requestObject != null) {
            future = requestJsonObject(Library.context, builder, ionLoadingCallback);
        }
        return future;
    }

    /**
     * 表单请求
     *
     * @param context            上下文
     * @param builder            请求实体
     * @param ionLoadingCallback 回调方法
     */
    private Future<Response<String>> requestParameter(Context context, LibraryBuilder builder, final LibraryLoadingCallback ionLoadingCallback) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        Set<String> set = builder.parameters.keySet();
        for (String key : set) {
            List<String> stringList = new ArrayList<String>();
            stringList.add(builder.parameters.get(key));
            map.put(key, stringList);
        }

        Builders.Any.B ion = Ion.with(context).load(builder.requestUrl);
        ion.onHeaders(new HeadersCallback() {
            @Override
            public void onHeaders(RawHeaders rawHeaders) {
                Map<String, List<String>> map = rawHeaders.toMultimap();
                ionLoadingCallback.onHeaders(map);
            }
        });
        ion.setTimeout(builder.timeOut);
        if (builder.headerMap != null) {
            Set<String> keys = builder.headerMap.keySet();
            for (String key : keys) {
                ion.addHeader(key, builder.headerMap.get(key));
            }
        }
        ion.setMultipartParameters(map);
        Future<Response<String>> future = ion.asString().withResponse();
        return future;
    }


    /**
     * JSON请求
     *
     * @param context            上下文
     * @param builder            请求实体
     * @param ionLoadingCallback 回调方法
     */
    private Future<Response<String>> requestJsonObject(Context context, LibraryBuilder builder, final LibraryLoadingCallback ionLoadingCallback) {
        Builders.Any.B ion = Ion.with(context).load(builder.requestUrl);
        ion.onHeaders(new HeadersCallback() {
            @Override
            public void onHeaders(RawHeaders rawHeaders) {
                Map<String, List<String>> map = rawHeaders.toMultimap();
                ionLoadingCallback.onHeaders(map);
            }
        });
        ion.setTimeout(builder.timeOut);

        ion.setLogging("MyLogs", Log.DEBUG);
        if (builder.headerMap != null) {
            Set<String> keys = builder.headerMap.keySet();
            for (String key : keys) {
                ion.addHeader(key, builder.headerMap.get(key));
            }
        }
        ion.setJsonObjectBody(builder.requestObject);
        Future<Response<String>> future = ion.asString().withResponse();
        return future;
    }

    protected void cancelRequest() {
        if (future != null) {
            future.cancel();
        }
    }


    /**
     * 启动加载进度条
     *
     * @param message   提示文字int 不接收String
     * @param canCancel 是否可关闭进度条状态
     * @param canFinish 是否可关闭当前Activity
     */
    private synchronized void startProgressBar(final Activity context, String message, final boolean canCancel, final boolean canFinish) {
        if (progressBarDialog != null && progressBarDialog.isShowing())
            return;
        View view = View.inflate(context, ParcelUtil.getLayoutId("library_common_dialog_progressbar"), null);
        RelayoutViewTool.relayoutViewWithScale(view, Library.screenWidthScale);
        progressBarDialog = UIDialogUtil.getInstance().buildDialog(context, view, false);
        TextView titleTest = (TextView) view.findViewById(ParcelUtil.getItemId("library_common_dialog_loading_txt"));
        titleTest.setText(message);

        progressBarDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && canCancel) {
                    if (canFinish || canCancel) {
                        if (future != null)
                            future.cancel();
                        if (canFinish) {
                            context.finish();
                        }
                    }
                    closeProgressBar();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 进度条 关
     */
    private synchronized boolean closeProgressBar() {
        if (progressBarDialog != null && progressBarDialog.isShowing()) {
            progressBarDialog.dismiss();
            return true;
        }
        return false;
    }

    private static String prepareParam(Map<String, String> paramMap) {
        StringBuffer sb = new StringBuffer();
        if (paramMap == null || paramMap.isEmpty()) {
            return "";
        } else {
            for (String key : paramMap.keySet()) {
                String value = (String) paramMap.get(key);
                if (sb.length() < 1) {
                    sb.append(key).append("=").append(value);
                } else {
                    sb.append("&").append(key).append("=").append(value);
                }
            }
            return sb.toString();
        }
    }
}
