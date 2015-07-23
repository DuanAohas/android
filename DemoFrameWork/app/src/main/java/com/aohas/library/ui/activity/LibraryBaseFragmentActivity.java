//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.aohas.library.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.Library;
import com.aohas.library.ui.util.RelayoutViewTool;
import com.aohas.library.ui.util.SystemBarTintManager;
import com.aohas.library.ui.util.UIDialogUtil;
import com.aohas.library.util.CheckUtil;
import com.aohas.library.util.Lg;
import com.aohas.library.util.ParcelUtil;
import java.io.Serializable;

public abstract class LibraryBaseFragmentActivity extends FragmentActivity {
    private Dialog progressBarDialog;
    private int resource = -1;
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context arg0, Intent intent) {
            String values = intent.getStringExtra("act-fgm-intent");
            if(CheckUtil.isEmpty(values)) {
                LibraryBaseFragmentActivity.this.getTransferMsg((Intent)intent);
            } else {
                LibraryBaseFragmentActivity.this.getTransferMsg((String)values);
            }

        }
    };

    public LibraryBaseFragmentActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        if(this.resource > -1) {
            if(VERSION.SDK_INT >= 19) {
                this.setTranslucentStatus(true);
            }

            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(this.resource);
        }

        super.onCreate(savedInstanceState);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = this.getWindow();
        LayoutParams winParams = win.getAttributes();
        int bits = 67108864;
        if(on) {
            winParams.flags |= 67108864;
        } else {
            winParams.flags &= -67108865;
        }

        win.setAttributes(winParams);
    }

    protected void setSystemBarResource(int resource) {
        this.resource = resource;
    }

    public void setContentView(View view) {
        super.setContentView(view);
        this.initHeader();
        this.initWidget();
        this.setWidgetState();
    }

    public void setContentView(int layoutResID) {
        View view = View.inflate(this, layoutResID, (ViewGroup)null);
        RelayoutViewTool.relayoutViewWithScale(view, Library.screenWidthScale);
        this.setContentView(view);
    }

    protected abstract void initHeader();

    protected abstract void initWidget();

    protected abstract void setWidgetState();

    protected <T> T getExtra(String key, T value) {
        Object o = null;
        if(value instanceof String) {
            o = this.getIntent().getStringExtra(key);
        } else if(value instanceof Boolean) {
            o = Boolean.valueOf(this.getIntent().getBooleanExtra(key, ((Boolean)value).booleanValue()));
        } else if(value instanceof Integer) {
            o = Integer.valueOf(this.getIntent().getIntExtra(key, ((Integer)value).intValue()));
        } else if(value instanceof Float) {
            o = Float.valueOf(this.getIntent().getFloatExtra(key, ((Float)value).floatValue()));
        } else if(value instanceof Long) {
            o = Long.valueOf(this.getIntent().getLongExtra(key, ((Long)value).longValue()));
        } else if(value instanceof Serializable) {
            o = this.getIntent().getSerializableExtra(key);
        }
        T t = (T)o;
        return t;
    }

    public void onDestroy() {
        super.onDestroy();

        try {
            this.unregisterReceiver(this.receiver);
        } catch (IllegalArgumentException var2) {
            Lg.e(LibraryBaseFragmentActivity.class.getName(), var2.getMessage());
        }

    }

    protected void setReceiver(String action) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        this.registerReceiver(this.receiver, filter);
    }

    protected void getTransferMsg(String values) {
    }

    protected void getTransferMsg(Intent intent) {
    }

    protected void setTransferMsg(String action, Intent intent) {
        intent.setAction(action);
        this.sendBroadcast(intent);
    }

    protected void setTransferMsg(String action, String values) {
        Intent intent = new Intent(action);
        intent.putExtra("act-fgm-intent", values);
        this.sendBroadcast(intent);
    }

    public Activity getTopActivity() {
        Object top;
        for(top = this; ((Activity)top).getParent() != null; top = ((Activity)top).getParent()) {
            ;
        }

        return (Activity)top;
    }

    public synchronized void startProgressBar(int message, final boolean canCancel, final boolean canFinish, final Future<JsonObject> json) {
        if(this.progressBarDialog == null || !this.progressBarDialog.isShowing()) {
            View view = View.inflate(this.getTopActivity(), ParcelUtil.getLayoutId("library_common_dialog_progressbar"), (ViewGroup)null);
            RelayoutViewTool.relayoutViewWithScale(view, Library.screenWidthScale);
            this.progressBarDialog = UIDialogUtil.getInstance().buildDialog(this.getTopActivity(), view, false);
            TextView titleTest = (TextView)view.findViewById(ParcelUtil.getItemId("library_common_dialog_loading_txt"));
            if(message > 0) {
                titleTest.setText(this.getString(message));
            }

            this.progressBarDialog.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if(i == 4 && canCancel) {
                        if(canFinish || canCancel) {
                            if(json != null) {
                                json.cancel();
                            }

                            if(canFinish) {
                                LibraryBaseFragmentActivity.this.getTopActivity().finish();
                            }
                        }

                        LibraryBaseFragmentActivity.this.closeProgressBar();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    public synchronized boolean closeProgressBar() {
        if(this.progressBarDialog != null && this.progressBarDialog.isShowing()) {
            this.progressBarDialog.dismiss();
            return true;
        } else {
            return false;
        }
    }
}
