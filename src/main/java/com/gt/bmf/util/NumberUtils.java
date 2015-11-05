package com.gt.bmf.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 15-4-1.
 */
public class NumberUtils {

    private static List<Double> testData = new ArrayList<Double>(10);

    public  void addListItem(double a){
        testData.add(a);
        if(testData.size()>10)testData.remove(0);
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
    public static double format(double num) {
        return Math.round(num*1000)/1000;
    }



    /**
     * 求给定双精度数组中值的最大值
     *
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果,如果输入值不合法，返回为-1
     */
    public static double getMax(List<Double> inputData) {
        if (inputData == null || inputData.size() == 0)
            return -1;
        int len = inputData.size();
        double max = inputData.get(0);
        for (int i = 0; i < len; i++) {
            if (max < inputData.get(i))
                max = inputData.get(i);
        }
        return max;
    }

    /**
     * 求求给定双精度数组中值的最小值
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果,如果输入值不合法，返回为-1
     */
    public static double getMin(List<Double> inputData) {
        if (inputData == null || inputData.size() == 0)
            return -1;
        int len = inputData.size();
        double min = inputData.get(0);
        for (int i = 0; i < len; i++) {
            if (min > inputData.get(i))
                min = inputData.get(i);
        }
        return min;
    }

    /**
     * 求给定双精度数组中值的和
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public static double getSum(List<Double> inputData) {
        if (inputData == null || inputData.size() == 0)
            return -1;
        int len = inputData.size();
        double sum = 0;
        for (int i = 0; i < len; i++) {
            sum = sum + inputData.get(i);
        }

        return sum;

    }

    /**
     * 求给定双精度数组中值的数目
     *
     * @param inputData 输入数据数组
     * @return 运算结果
     */
    public static int getCount(List<Double> inputData) {
        if (inputData == null)
            return -1;

        return inputData.size();
    }

    /**
     * 求给定双精度数组中值的平均值
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public static double getAverage(List<Double> inputData) {
        if (inputData == null || inputData.size() == 0)
            return -1;
        int len = inputData.size();
        double result;
        result = getSum(inputData) / len;

        return result;
    }

    /**
     * 求给定双精度数组中值的平方和
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public static double getSquareSum(List<Double> inputData) {
        if(inputData==null||inputData.size()==0)
            return -1;
        int len=inputData.size();
        double sqrsum = 0.0;
        for (int i = 0; i <len; i++) {
            sqrsum = sqrsum + inputData.get(i) * inputData.get(i);
        }


        return sqrsum;
    }

    /**
     * 求给定双精度数组中值的方差
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public static double getVariance(List<Double> inputData) {
        int count = getCount(inputData);
        double sqrsum = getSquareSum(inputData);
        double average = getAverage(inputData);
        double result = (sqrsum - count * average * average) / count;


        return result;
    }

    /**
     * 求给定双精度数组中值的标准差
     *
     * @param inputData
     *            输入数据数组
     * @return 运算结果
     */
    public static double getStandardDiviation(List<Double> inputData) {
        double result;
        //绝对值化很重要
        result = Math.sqrt(Math.abs(getVariance(inputData)));
        return result;
    }

    public static void main(String[] args) {
   /*     List<Double> testData=new  ArrayList<Double>();
     */
        List<Double> testData=new  ArrayList<Double>();
        testData.add(10d);
        testData.add(10d);
        testData.add(10d);
        testData.add(10d);
        testData.add(25d);
        testData.add(20d);

            System.out.println("最大值：" + getMax(testData));
            System.out.println("最小值："+getMin(testData));
            System.out.println("计数："+getCount(testData));
            System.out.println("求和："+getSum(testData));
            System.out.println("求平均："+getAverage(testData));
            System.out.println("方差："+getVariance(testData));
            System.out.println("标准差："+getStandardDiviation(testData));
            System.out.println("---------------------------------------------");





    }

}
