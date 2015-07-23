package com.aohas.library.util;

import android.content.Context;

import java.util.Locale;

/**
 * Created by liuyu on 14-4-30.
 */
public class BuildUtil {

    /**
     * CPU
     * */
    public static String getCPU(){
        return android.os.Build.CPU_ABI;
    }

    public static String getTags(){
        return android.os.Build.TAGS;
    }

    public static int getBase(){
        return android.os.Build.VERSION_CODES.BASE;
    }
    /**
     * 型号
     * */
    public static String getModel(){
        return android.os.Build.MODEL;
    }

    /**
     * SDK版本
     * */
    public static int getSDK(){
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 系统版本
     * */
    public static String getRELEASE(){
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 设备
     * */
    public static String getDEVICE(){
        return android.os.Build.DEVICE;
    }

    /**
     * 指纹
     * */
    public static String getFINGERPRINT(){
        return android.os.Build.FINGERPRINT;
    }

    /**
     * 指纹
     * */
    public static String getDISPLAY(){
        return android.os.Build.DISPLAY;
    }

    /**
     * 制造商
     * */
    public static String getMANUFACTURER(){
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 品牌
     * */
    public static String getBRAND(){
        return android.os.Build.BRAND;
    }

    /**
     * 语言
     * */
    public static String getLanguage(Context context){
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getDisplayLanguage();
        return language;
    }

    /**
     * 国家
     * */
    public static String getCountry(Context context){
        Locale locale = context.getResources().getConfiguration().locale;
        String country = locale.getDisplayCountry();
        return country;
    }

    /**
     * 地区
     * */
    public static String getZone(Context context){
        //todo 未获取到地区
        Locale locale = context.getResources().getConfiguration().locale;
        String country = locale.getDisplayCountry();
        return country;
    }



//    String phoneInfo = "Product: " + android.os.Build.PRODUCT;
//    phoneInfo += "\n CPU_ABI: " + android.os.Build.CPU_ABI;
//    phoneInfo += "\n TAGS: " + android.os.Build.TAGS;
//    phoneInfo += "\n VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE;
//    phoneInfo += "\n MODEL: " + android.os.Build.MODEL;
//    phoneInfo += "\n SDK: " + android.os.Build.VERSION.SDK_INT;//SDK版本
//    phoneInfo += "\n VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;//系统版本
//    phoneInfo += "\n DEVICE: " + android.os.Build.DEVICE;
//    phoneInfo += "\n FINGERPRINT: " + android.os.Build.FINGERPRINT;
//    phoneInfo += "\n DISPLAY: " + android.os.Build.DISPLAY;
//    phoneInfo += "\n BRAND: " + android.os.Build.BRAND;
//    phoneInfo += "\n BOARD: " + android.os.Build.BOARD;
//    phoneInfo += "\n ID: " + android.os.Build.ID;
//    phoneInfo += "\n MANUFACTURER: " + android.os.Build.MANUFACTURER;//制造商
//    phoneInfo += "\n USER: " + android.os.Build.USER;




}
