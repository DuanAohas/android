package com.aohas.library.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: liuyu
 * Date: 13-3-21
 * Time: 下午5:08
 * To change this template use File | Settings | File Templates.
 */
public class VerificationUtil {
    /**
     * 验证用户名称
     * @param strUserName
     * @return
     */
    public static boolean isUserName_login(String strUserName) {
        String strPattern = "^(?!_)(?!.*?_$)([a-zA-Z0-9_]|[\u4E00-\u9FA5\uf900-\ufa2d])+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strUserName);
        return m.matches();
    }

    /**
     * 校验手机
     * @param strMoible
     * @return
     */
    public static boolean isMobile(String strMoible) {
        String strPattern = "1[3,4,8,5]{1}+[0-9]{9}";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strMoible);
        return m.matches();
    }
    /**
     * 验证邮箱
     * @param strEmail
     * @return
     */

    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

}
