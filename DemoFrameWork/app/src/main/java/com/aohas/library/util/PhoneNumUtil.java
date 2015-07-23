package com.aohas.library.util;

/**
 * Created with IntelliJ IDEA.
 * User: LiuYu
 * Date: 13-1-31
 * Time: 下午3:23
 * To change this template use File | Settings | File Templates.
 */
public class PhoneNumUtil {

    public static boolean isPhoneNum(String phoneNum){
        return phoneNum.matches("^1\\d{10}$") || phoneNum.matches("^+861\\d{10}$") || phoneNum.matches("^861\\d{10}$");
    }
}
