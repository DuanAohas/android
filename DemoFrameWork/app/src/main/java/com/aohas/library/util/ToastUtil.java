package com.aohas.library.util;

import android.widget.Toast;
import com.aohas.library.Library;

public class ToastUtil {

    private static boolean ISSHOW = false;

    public static void setDebugMode(boolean isShow) {
        ISSHOW = isShow;
    }

    public static void debug(final String msg) {
        if (ISSHOW)
            toast("调试：" + msg);
    }

    public static void toast(final String msg) {
        toast(msg, Toast.LENGTH_SHORT);
    }

    public static void toast(String msg, int time){
        Toast.makeText(Library.context, msg, time).show();
    }
}
