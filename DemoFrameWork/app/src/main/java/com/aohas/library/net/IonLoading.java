package com.aohas.library.net;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.aohas.library.Library;
import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.async.future.FutureCallback;
import com.aohas.library.koushikdutta.async.http.libcore.RawHeaders;
import com.aohas.library.koushikdutta.ion.HeadersCallback;
import com.aohas.library.koushikdutta.ion.Ion;
import com.aohas.library.koushikdutta.ion.Response;
import com.aohas.library.koushikdutta.ion.builder.Builders;
import com.aohas.library.ui.activity.LibraryDialogFragment;
import com.aohas.library.ui.util.RelayoutViewTool;
import com.aohas.library.ui.util.UIDialogUtil;
import com.aohas.library.util.Lg;
import com.aohas.library.util.NetWorkUtil;
import com.aohas.library.util.ParcelUtil;

import java.util.*;

/**
 * Created by liu_yu on 13-12-26.
 */
public class IonLoading {

    private Dialog progressBarDialog;
    private Future<Response<String>> future = null;

//    private static IonLoading ionLoading;
//
//    public static IonLoading getInstance() {
//        if (ionLoading == null) {
//            ionLoading = new IonLoading();
//        }
//        return ionLoading;
//    }

    protected IonLoading() {
    }

    /**
     * 请求入口
     *
     * @param context            上下文
     * @param ionEntity          请求实体
     * @param ionLoadingCallback 回调方法
     */
    public void request(final Context context, final IonEntity ionEntity, final IonLoadingCallback ionLoadingCallback) {
        if (ionEntity == null) {
            Toast.makeText(Library.context, "请传入请求参数", Toast.LENGTH_SHORT).show();
            return;
        }

        //判断网络是否正常
        if (!NetWorkUtil.networkCanUse(Library.context)) {
            closeProgressBar();
            ionLoadingCallback.netWorkError();
        }

        if (ionEntity.isShowBar && context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActProgressBar(context, ionEntity.barMessage, ionEntity.canCancel, ionEntity.canFinish);
                }
            });
        }
        new LoadingTask().execute(ionEntity, ionLoadingCallback);
    }

    private class LoadingTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object[] params) {
            IonEntity ionEntity = (IonEntity) params[0];
            final IonLoadingCallback ionLoadingCallback = (IonLoadingCallback) params[1];
            Future<Response<String>> future = null;
            //根据parameters和requestObject判断需要什么请求方式
            if (ionEntity.parameters == null && ionEntity.requestObject == null) {
                future = requestGet(Library.context, ionEntity, ionLoadingCallback);
            } else if (ionEntity.parameters != null && ionEntity.requestObject == null) {
                future = requestParameter(Library.context, ionEntity, ionLoadingCallback);
            } else if (ionEntity.parameters == null && ionEntity.requestObject != null) {
                future = requestJsonObject(Library.context, ionEntity, ionLoadingCallback);
            }

            if (future == null) {
                Toast.makeText(Library.context, "请求错误", Toast.LENGTH_SHORT).show();
                return null;
            }
            future.setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(final Exception e, final Response<String> _result) {
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
     * GET请求
     *
     * @param context            上下文
     * @param ionEntity          请求实体
     * @param ionLoadingCallback 回调方法
     */
    private Future<Response<String>> requestGet(Context context, IonEntity ionEntity, final IonLoadingCallback ionLoadingCallback) {
        StringBuffer getData = new StringBuffer(ionEntity.requestUrl);
        if (ionEntity.getObject != null) {
            getData.append("?");
            Set<String> keys = ionEntity.getObject.keySet();
            for (String key : keys) {
                getData.append(key + "=" + ionEntity.getObject.get(key) + "&");
            }
            if (getData.toString().endsWith("&")) {
                getData = new StringBuffer(getData.toString().substring(0, getData.toString().length() - 1));
            }
        }
        Lg.d("LIBRARY-REQUEST-GET", getData.toString());
        Builders.Any.B ion = Ion.with(context.getApplicationContext()).load(getData.toString());
        ion.onHeaders(new HeadersCallback() {
            @Override
            public void onHeaders(RawHeaders rawHeaders) {
                Map<String, List<String>> map = rawHeaders.toMultimap();
                ionLoadingCallback.onHeaders(map);
            }
        });
        ion.setTimeout(ionEntity.timeOut);
        if (ionEntity.headerMap != null) {
            Set<String> keys = ionEntity.headerMap.keySet();
            for (String key : keys) {
                ion.addHeader(key, ionEntity.headerMap.get(key));
            }
        }
        Future<Response<String>> future = ion.asString().withResponse();
        return future;
    }

    /**
     * 表单请求
     *
     * @param context            上下文
     * @param ionEntity          请求实体
     * @param ionLoadingCallback 回调方法
     */
    private Future<Response<String>> requestParameter(Context context, IonEntity ionEntity, final IonLoadingCallback ionLoadingCallback) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        Set<String> set = ionEntity.parameters.keySet();
        for (String key : set) {
            List<String> stringList = new ArrayList<String>();
            stringList.add(ionEntity.parameters.get(key));
            map.put(key, stringList);
        }

        Builders.Any.B ion = Ion.with(context).load(ionEntity.requestUrl);
        ion.onHeaders(new HeadersCallback() {
            @Override
            public void onHeaders(RawHeaders rawHeaders) {
                Map<String, List<String>> map = rawHeaders.toMultimap();
                ionLoadingCallback.onHeaders(map);
            }
        });
        ion.setTimeout(ionEntity.timeOut);
        if (ionEntity.headerMap != null) {
            Set<String> keys = ionEntity.headerMap.keySet();
            for (String key : keys) {
                ion.addHeader(key, ionEntity.headerMap.get(key));
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
     * @param ionEntity          请求实体
     * @param ionLoadingCallback 回调方法
     */
    private Future<Response<String>> requestJsonObject(Context context, IonEntity ionEntity, final IonLoadingCallback ionLoadingCallback) {
        Builders.Any.B ion = Ion.with(context).load(ionEntity.requestUrl);
        ion.onHeaders(new HeadersCallback() {
            @Override
            public void onHeaders(RawHeaders rawHeaders) {
                Map<String, List<String>> map = rawHeaders.toMultimap();
                ionLoadingCallback.onHeaders(map);
            }
        });
        ion.setTimeout(ionEntity.timeOut);
        if (ionEntity.headerMap != null) {
            Set<String> keys = ionEntity.headerMap.keySet();
            for (String key : keys) {
                ion.addHeader(key, ionEntity.headerMap.get(key));
            }
        }
        ion.setJsonObjectBody(ionEntity.requestObject);
        Future<Response<String>> future = ion.asString().withResponse();
        return future;
    }

    public void cancelRequest() {
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
    private synchronized void startProgressBar(final Context context, String message, final boolean canCancel, final boolean canFinish) {
        if (context instanceof FragmentActivity) {
            startFgmProgressBar(context, message, canCancel, canFinish);
            Lg.d("===========library==========", "FragmentActivity: ");
        } else if (context instanceof Activity) {
            startActProgressBar(context, message, canCancel, canFinish);
            Lg.d("===========library==========", "Activity: ");
        }
    }

    private synchronized void startActProgressBar(final Context context, String message, final boolean canCancel, final boolean canFinish) {
        if (progressBarDialog != null && progressBarDialog.isShowing()) {
            Window windowView = progressBarDialog.getWindow();
            TextView titleTest = (TextView) windowView.findViewById(ParcelUtil.getItemId("library_common_dialog_loading_txt"));
            titleTest.setText(message);
            return;
        }

        Lg.d("===========library==========", "Activity: " + message);


        View view = View.inflate(context.getApplicationContext(), ParcelUtil.getLayoutId("library_common_dialog_progressbar"), null);
        RelayoutViewTool.relayoutViewWithScale(view, Library.screenWidthScale);
        TextView titleTest = (TextView) view.findViewById(ParcelUtil.getItemId("library_common_dialog_loading_txt"));
        titleTest.setText(message);

        progressBarDialog = UIDialogUtil.getInstance().buildDialog(context, view, false);
        progressBarDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && canCancel) {
                    if (canFinish || canCancel) {
                        if (future != null)
                            future.cancel();
                        if (canFinish) {
                            ((Activity) context).finish();
                        }
                    }
                    closeProgressBar();
                    return true;
                }
                return false;
            }
        });
    }

    LibraryDialogFragment dialogFragment;

    private void startFgmProgressBar(final Context context, String message, final boolean canCancel, final boolean canFinish) {
        dialogFragment = new LibraryDialogFragment();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "dialog");
        Lg.d("===========library=========show=", "FragmentActivity: ");
    }

    /**
     * 进度条 关
     */
    private synchronized boolean closeProgressBar() {
        if (progressBarDialog != null && progressBarDialog.isShowing()) {
            progressBarDialog.dismiss();
        }
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
        return false;
    }
}
