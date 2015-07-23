package com.aohas.library.util;

import java.util.List;
import java.util.Map;

/**
 * Created by liuyu on 14-4-29.
 */
public class CheckUtil {


    public static boolean isEmpty(String str){
        if(str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(List list){
        if(list == null || list.size() == 0)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(Map map){
        if(map == null)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(Object object){
        if(object == null)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(Object object[]){
        if(object == null || object.length == 0)
            return true;
        else
            return false;
    }
}
