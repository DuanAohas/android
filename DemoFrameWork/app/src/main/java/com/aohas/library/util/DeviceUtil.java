package com.aohas.library.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import com.aohas.library.Library;

import java.lang.reflect.Method;

/**
* @author Nick create at 2011-3-18
*/
public class DeviceUtil {

    public static String getProp(String prop) {
        String output = "";
        try {
            Class<?> sp = Class.forName("android.os.SystemProperties");
            Method get = sp.getMethod("get", String.class);
            output = (String) get.invoke(null, prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String getImei() {
        TelephonyManager tm = (TelephonyManager) Library.context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static float getDensity() {
        DisplayMetrics displayMetrics = Library.context.getResources().getDisplayMetrics();
        return displayMetrics.density;
    }

    public static float getWidth() {
        DisplayMetrics displayMetrics = Library.context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static float getHeigh() {
        DisplayMetrics displayMetrics = Library.context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static String getResolution() {
        DisplayMetrics displayMetrics = Library.context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels + "*" + displayMetrics.heightPixels;
    }

    public static int getVersionCode() {
        PackageManager manager = Library.context.getPackageManager();
        ApplicationInfo info = Library.context.getApplicationInfo();

        try {
            return manager.getPackageInfo(info.packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    public static String getVersionName() {
        PackageManager manager = Library.context.getPackageManager();
        ApplicationInfo info = Library.context.getApplicationInfo();

        try {
            return manager.getPackageInfo(info.packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            return "Unknow";
        }
    }

}
