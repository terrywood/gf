package com.gt.bmf.util;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 15-4-1.
 */
public class NumberUtils {

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
    public static double format(double num) {
        return Math.round(num*1000)/1000;
    }
}
