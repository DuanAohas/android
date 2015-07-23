package com.aohas.library.util;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: SlothMonkey
 * Date: 12-12-22
 * Time: 下午1:16
 * To change this template use File | Settings | File Templates.
 */
public class UrlUtil {

//    private static String regex = "((http://)?([a-z]+[.])|(www.))\\w+[.]([a-z]{2,4})?[[.]([a-z]{2,4})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z]{2,4}+|/?)";
//    private static String regex = "(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)";
    private static String regex = "http://[\\w\\.\\-/:]+";
    private static String A1 = " <a href={0}>";
    private static String A2 = " </a>";
    public static String toHref(String title) {
        StringBuffer sb = new StringBuffer(title);
        Pattern pat = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher mat = pat.matcher(title);
        int index = 0;
        int index1 = 0;
        while (mat.find()) {
            String url = mat.group();
            //System.out.println(url);
            if (url.indexOf("http://") != 0)
                url = "http://" + url;
            Object obj[] = { "'" + url + "'" };
            String a = MessageFormat.format(A1, obj);
            int l = a.length();
            index += index1;
            sb.insert(mat.start() + index, a);
            index += l;
            sb.insert((mat.end()) + index, A2);
            index1 = A2.length();
        }
        return sb.toString();
    }
    /**
     * 目前住处理http://开头和https://开头的文件地址
     * @param url
     * @return
     */
    public static String getUrlFileName(String url) {

        if(url==null){
            return null;
        }

        if (!isURL(url)) {
            return null;
        }

        if (!isFileUrl(url)) {
            return null;
        }

        String[] urlArr = url.split("/");
        return urlArr[urlArr.length - 1];
    }


    public static boolean isFileUrl(String url) {

        int lastIndex = url.lastIndexOf("/");
        if (lastIndex < url.length()-1) {
            return true;
        }
        return false;
    }

    public static boolean isURL(String url) {

        if (url.startsWith("http://") || url.startsWith("https://")) {
            return true;
        }

        return false;
    }


    public static void main(String[] args) {

//        System.out.println(getUrlFileName("http://groupImage.baidu.com/static/widget/list/top/img/logo_8816bd4a.gifstitch"));
//        System.out.println(getUrlFileName("http://groupImage.baidu.com/static/widget/list/top/img/"));
//        System.out.println(getUrlFileName("http://groupImage.baidu.com/static/widget/list/top/img"));
//        System.out.println(getUrlFileName("http://groupImage.baidu.com/static/widget/list/top/img/logo_8816bd4a"));
//        System.out.println(getUrlFileName("ht://groupImage.baidu.com/static/widget/list/top/img/logo_8816bd4a.gifstitch"));
//        System.out.println(getUrlFileName("https://groupImage.baidu.com/static/widget/list/top/img/logo_8816bd4a.gifstitch"));
//        System.out.println(getUrlFileName(null));
//        System.out.println(getUrlFileName(""));
//        System.out.println(getUrlFileName("/"));
//        System.out.println(getUrlFileName("//"));


        System.out.println(toHref("打法发射点发生地方 http://baike.baidu.com/view/230199.htm?fr=ala0_1 打法发射点发生地方"));
        System.out.println(toHref("打法发射点发生地方 http:// 打法发射点发生地方"));
        System.out.println(toHref("Web版Google+整合Snapseed图片编辑工具，现已开放试用 | http://t.cn/z8N2ham Web版Google+推出了与Chrome浏览器配套的新Snapseed工具，新功能包括：一键美化、手动修饰和滤镜。非Chrome用户仅可使用裁剪、旋转的基础功能，更高级的Snapseed工具则不对他们开放。"));
        System.out.println(toHref("打法发射点发生地方"));




    }


}
