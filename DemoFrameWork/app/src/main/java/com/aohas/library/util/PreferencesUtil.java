package com.aohas.library.util;

import android.content.SharedPreferences;
import com.aohas.library.Library;

/**
 * Created with IntelliJ IDEA.
 * User: liuyu
 * Date: 13-10-12
 * Time: 上午11:26
 * To change this template use File | Settings | File Templates.
 */
public class PreferencesUtil {

    public static <T> void putPreferences(String key, T value){
        SharedPreferences.Editor editor = Library.preferences.edit();
        if(value instanceof String){
            editor.putString(key, value.toString());
        }else if(value instanceof Boolean){
            editor.putBoolean(key, ((Boolean) value).booleanValue());
        }else if(value instanceof Integer){
            editor.putInt(key, ((Integer) value).intValue());
        }else if(value instanceof Float){
            editor.putFloat(key, ((Float) value).floatValue());
        }else if(value instanceof Long){
            editor.putLong(key, ((Long) value).longValue());
        }
        editor.commit();
    }

    public static <T> T getPreferences(String key, T value){
        Object o = null;
        if(value instanceof String){
            o =  Library.preferences.getString(key, value.toString());
        }else if(value instanceof Boolean){
            o = Library.preferences.getBoolean(key, ((Boolean) value).booleanValue());
        }else if(value instanceof Integer){
            o = Library.preferences.getInt(key, ((Integer) value).intValue());
        }else if(value instanceof Float){
            o = Library.preferences.getFloat(key, ((Float) value).floatValue());
        }else if(value instanceof Long){
            o = Library.preferences.getLong(key, ((Long) value).longValue());
        }
        T t = (T) o;
        return t;
    }
}
