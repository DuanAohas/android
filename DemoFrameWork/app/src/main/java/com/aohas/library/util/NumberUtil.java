package com.aohas.library.util;

import android.util.Log;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: SlothMonkey
 * Date: 12-4-28
 * Time: 下午2:39
 * To change this template use File | Settings | File Templates.
 */
public class NumberUtil {

    private static final String TAG = NumberUtil.class.getSimpleName();

    public static BigDecimal long2Decimal(long money) {
        return BigDecimal.valueOf(money, 2);
    }


    //有两位小数的钱转换为以分为单位的钱
    public static Long decimal2Long(BigDecimal money) {
        return (money.multiply(BigDecimal.valueOf(100))).longValue();
    }

    //将分转换成元的字符串
    public static String getMoneyYuanStr(long moneyFen) {
        return long2Decimal(moneyFen).toString();
    }

    // Folat保留n位小数，四舍五入，返回Folat类型对象
    public static Float getFloatFromFloatRoundHalfUp(float sourceNum, int scale) {
        BigDecimal bigDecimal = new BigDecimal(sourceNum);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    // Double保留n位小数，四舍五入，返回Folat类型对象
    public static Float getDoubleFromDoubletRoundHalfUp(double sourceNum, int scale) {
        BigDecimal bigDecimal = new BigDecimal(sourceNum);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    // Double保留n位小数，四舍五入，返回double类型对象
    public static Double getDoubleFromDoubletRoundHalfUp1(double sourceNum, int scale) {
        BigDecimal bigDecimal = new BigDecimal(sourceNum);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //float 转换至 int 小数四舍五入
    public static int convertFloatToInt(float sourceNum) {
        BigDecimal bigDecimal = new BigDecimal(sourceNum);
        return bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    //double 转换至 int 小数四舍五入
    public static int convertDoubleToInt(double sourceNum) {
        BigDecimal bigDecimal = new BigDecimal(sourceNum);
        return bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    public static int parseInt(String str, int def) {

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            Log.d(TAG, "String to Int Error! Use def value! ");
        }
        return def;
    }

    public static long parseLong(String str, long def) {

        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            Log.d(TAG, "String to Long Error! Use def value!");
        }
        return def;
    }

    public static float parseFloat(String str, float def) {

        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            Log.d(TAG, "String to Float Error! Use def value!");
        }
        return def;
    }

    public static Double parseDouble(String str, double def) {

        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            Log.d(TAG, "String to Double Error! Use def value!");
        }
        return def;
    }

    /*
    * 格式化 数字
    * */
    public static String getToFormatNum(int num) {
        String numStr = "" + num;
        if (num < 10 && num != 0) {
            numStr = "0" + num;
        }
        return numStr;
    }

    /**
     *  @param startNum //获取随机 数开始数字
     *  @param endNum   //获取随机 数结束数字
     * */
    public static int getRandom(int startNum, int endNum) {
        int num = (int) (Math.round(Math.random() * (endNum - startNum) + startNum));
        return num;
    }

    /**
     *  @param startNum //获取随机 数开始数字
     *  @param endNum   //获取随机 数结束数字
     *  @param needNum      //获取随机个数
     * */
    public static int[] getRandom(int startNum, int endNum, int needNum) {
        int nums[] = new int[needNum];
        Set<Integer> integerSet = new HashSet<Integer>();
        while (integerSet.size() < needNum){
            int num = (int) (Math.round(Math.random() * (endNum - startNum) + startNum));
            integerSet.add(num);
        }
        Object[] ints = integerSet.toArray();
        for (int i = 0; i < ints.length; i++) {
            nums[i] = Integer.parseInt(ints[i].toString());
        }
        return nums;
    }



    /**
     * @param int size 需要的随机数 数量
     * @param int beginNum 随机 范围的开始
     * @param int endNum 随机 范围的结束
     * @return int[]
     */
    public static int[] getMachineSelectionNum(int size, int numbers[]) {
        int length = numbers.length;
        int num[] = new int[size];
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < size) {
            int number = numbers[random.nextInt(length)];
            int m = 0;
//            for (int hava_num : havaNum) {
//                if (hava_num == number) {
//                    m++;
//                }
//            }
            if (m == 0) {
                set.add(number);
            }
        }
        Object[] ints = set.toArray();
        for (int i = 0; i < ints.length; i++) {
            num[i] = Integer.parseInt(ints[i].toString());
        }
        return num;
    }

    /**
     * @param int size 需要的随机数 数量
     * @param int beginNum 随机 范围的开始
     * @param int endNum 随机 范围的结束
     * @return int[]
     */
    public static int[] getMachineSelectionNumNo(int size, int numbers[]) {
        int length = numbers.length;
        int num[] = new int[size];
        Random random = new Random();
        List<Integer> list = new ArrayList<Integer>();
        while (list.size() < size) {
            int number = numbers[random.nextInt(length)];
            list.add(number);
        }

        for (int i = 0; i < list.size(); i++) {
            num[i] = list.get(i);
        }
        return num;
    }

    public static int[] getUseNums(int allNums[], int havaNums[]) {
        List<Integer> integers = new ArrayList<Integer>();
        for (int allNum : allNums) {
            int n = 0;
            for (int havaNum : havaNums) {
                if (allNum == havaNum) {
                    n++;
                }
            }
            if (n == 0) {
                integers.add(allNum);
            }
        }
        int numbers[] = new int[integers.size()];
        for (int i = 0; i < integers.size(); i++) {
            numbers[i] = integers.get(i);
        }
        return numbers;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = DeviceUtil.getDensity();
        return (int) (dpValue * scale + 0.5f);
    }

    public static void main(String[] args) {
        System.out.println(getMoneyYuanStr(123456789));
        System.out.println(getMoneyYuanStr(0));
        System.out.println(getMoneyYuanStr(10));
        System.out.println(getMoneyYuanStr(000));
        System.out.println(getMoneyYuanStr(123));
        System.out.println(getMoneyYuanStr(1234));
        System.out.println(getMoneyYuanStr(1));
    }


}
