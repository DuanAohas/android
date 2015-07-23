package com.aohas.library.util;

import android.util.Log;
import com.aohas.library.Library;

/**
 * @author {YueJinbiao}
 */
public class Lg {
    private static boolean ISSHOW = false;

    public static void setDebugMode(boolean isShow) {
        ISSHOW = isShow;
    }

    public static void d(String msg) {
        d("", msg);
    }

    public static void e(String msg) {
        e("", msg);
    }

    public static void i(String msg) {
        i("", msg);
    }

    public static void w(String msg) {
        w("", msg);
    }

    public static void d(String tag, String msg) {
        if (ISSHOW)
            Log.d(">>>" + Library.resource.getString(ParcelUtil.getStringId("app_name")) + "<<<", tag + " >> " + msg);
    }

    public static void e(String tag, String msg) {
        if (ISSHOW)
            Log.e(">>>" + Library.resource.getString(ParcelUtil.getStringId("app_name")) + "<<<", tag + " >> " + msg);
    }

    public static void i(String tag, String msg) {
        if (ISSHOW)
            Log.i(">>>" + Library.resource.getString(ParcelUtil.getStringId("app_name")) + "<<<", tag + " >> " + msg);
    }

    public static void w(String tag, String msg) {
        if (ISSHOW)
            Log.w(">>>" + Library.resource.getString(ParcelUtil.getStringId("app_name")) + "<<<", tag + " >> " + msg);
    }
}
