package com.aohas.library;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.aohas.library.util.CrashHandler;
import com.aohas.library.util.PreferencesUtil;

/**
 * Created with IntelliJ IDEA.
 * User: liu_yu
 * Date: 13-9-22
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class Library extends Application {

    public static int versionCode;
    public static String versionName;
    public static Context context;
    public static SharedPreferences preferences;

    public static DisplayMetrics displayMetrics;
    // 原始UI界面设计图的宽度(px)，用于后期对控件做缩放
    private static float UI_Design_Width = 640;
    private static float UI_Design_Height = 960;
    // 屏幕宽度缩放比（相对于原设计图）
    public static float screenWidthScale = 1f;
    public static float screenHeightScale = 1f;

    public static Resources resource;
    public static String pkgName;
    public static String app_identity;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        resource = this.getResources();
        pkgName = this.getPackageName();

        displayMetrics = getResources().getDisplayMetrics();


        float[] widthHeight = getWidthHeight();
        if (widthHeight.length == 2) {
            setWidthHeight(widthHeight[0], widthHeight[1]);
        }

        // 初始化屏幕宽度缩放比例
        if (PreferencesUtil.getPreferences("screenWidthScale", -1f) == -1) {
            screenWidthScale = displayMetrics.widthPixels < displayMetrics.heightPixels ? displayMetrics.widthPixels / UI_Design_Width : displayMetrics.heightPixels / UI_Design_Width;
            PreferencesUtil.putPreferences("screenWidthScale", screenWidthScale);
        } else {
            screenWidthScale = PreferencesUtil.getPreferences("screenWidthScale", 1f);
        }
        if (PreferencesUtil.getPreferences("screenHeightScale", -1f) == -1) {
            screenHeightScale = displayMetrics.widthPixels < displayMetrics.heightPixels ? displayMetrics.heightPixels / UI_Design_Height : displayMetrics.heightPixels / UI_Design_Height;
            PreferencesUtil.putPreferences("screenHeightScale", screenHeightScale);
        } else {
            screenHeightScale = PreferencesUtil.getPreferences("screenHeightScale", 1f);
        }
    }

    protected void setWidthHeight(float width, float height) {
        UI_Design_Width = width;
        UI_Design_Height = height;
    }

    protected float[] getWidthHeight() {
        return new float[]{640f, 960f};
    }

    protected void openCrash() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(context);
    }
}
