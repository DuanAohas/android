package com.aohas.library.util;//package com.mrocker.kufa88.util;
//
//import net.sourceforge.pinyin4j.PinyinHelper;
//import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
//import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
//import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
//import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
//import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
//
//public class PinYinUtil {
//    static HanyuPinyinOutputFormat outputFormat;
//
//    static {
//        try {
//            outputFormat = new HanyuPinyinOutputFormat();
//            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
//            outputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
//            outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 汉字返回拼音，字母 小写
//     * 必须在Android中使用
//     *
//     * @param source stirng
//     * @return string
//     */
//
//    public static String getFullPinYin(String source) {
//        StringBuilder sb = new StringBuilder();
//
//        if (source.length() > 0) {
//            for (int i = 0; i < source.length(); i++) {
//                try {
//                    String[] arrays = PinyinHelper.toHanyuPinyinStringArray(source.charAt(i), outputFormat);
//                    if (arrays != null && arrays.length > 0) {
//                        sb.append(arrays[0]);
//                    }else{
//                        sb.append(source.charAt(i));
//                    }
//                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
//                    badHanyuPinyinOutputFormatCombination.printStackTrace();
//                }
//            }
//        }
//        return sb.toString().toLowerCase();
//
//    }
//
//    /**
//     * 获取汉字拼音首字母 小写
//     * 必须在Android中使用
//     * @param source string
//     * @return string
//     */
//    public static String getFirstPinYin(String source) {
//
//
//        StringBuilder sb = new StringBuilder();
//        if (source != null && source.length() > 0) {
//
//            for(int i = 0; i < source.length(); i++){
//                try {
//                    String[] arrays = PinyinHelper.toHanyuPinyinStringArray(source.charAt(i), outputFormat);
//                    if (arrays != null && arrays.length > 0) {
//                        sb.append(arrays[0].charAt(0));
//                    }else{
//                        sb.append(source.charAt(i));
//                    }
//                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
//                    badHanyuPinyinOutputFormatCombination.printStackTrace();
//                }
//            }
//        }
//        return sb.toString().toLowerCase();
//    }
//}
