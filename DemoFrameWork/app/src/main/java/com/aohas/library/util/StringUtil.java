package com.aohas.library.util;

/**
 * Created with IntelliJ IDEA.
 * User: LiuYu
 * Date: 13-1-15
 * Time: 下午12:19
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {
    /**
     * 判断字符窜是否不为null和空串
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0;
    }
    public static String getDownloadFileName(String audio_url){//获取下载的音频文件名称  	
    	int lastPosition = audio_url.lastIndexOf('/');
    	String audio_name = audio_url.substring(lastPosition, audio_url.length());
    	return audio_name;
    }
}
