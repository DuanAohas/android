package com.aohas.library.net.loadbuilder;

import android.content.Context;
import com.aohas.library.util.CheckUtil;
import com.aohas.library.util.Lg;

/**
 * 网络操作入口
 */
public class LibraryLoad {

    /**
     * 加载
     *
     * @param context 上下文，如果需要弹起转轮，则需要传入Activity
     * @param url     请求的地址
     */
    public static LibraryBuilder load(Context context, String url) {
        if(context == null){
            Lg.e("LibraryLoad", "context is null");
        }else if(CheckUtil.isEmpty(url)){
            Lg.e("LibraryLoad", "url is null");
        }else{
            return new LibraryBuilder(context, url);
        }
        return null;
    }

    /**
     * 取消当前网络操作
     */
    public static void cancelRequest() {
        LibraryLoading.getInstence().cancelRequest();
    }
}
