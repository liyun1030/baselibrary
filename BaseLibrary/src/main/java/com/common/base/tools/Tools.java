package com.common.base.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 常用工具类方法
 */
public class Tools {
    public static String formatDouble(double d) {
        BigDecimal bg = new BigDecimal(getTwoDecimal(d)).stripTrailingZeros();
        double num = bg.doubleValue();
        double tmp=Math.round(num);
        if (tmp - num == 0) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }
    public static double formatDoubleNum(double d) {
        BigDecimal bg = new BigDecimal(getTwoDecimal(d)).stripTrailingZeros();
        double num = bg.doubleValue();
        double tmp=Math.round(num);
        if (tmp - num == 0) {
            return num;
        }
        return num;
    }
    /**
     * 将数据保留两位小数
     */
    public static double getTwoDecimal(double num) {
        DecimalFormat dFormat = new DecimalFormat("0.00");
        dFormat.setRoundingMode(RoundingMode.HALF_UP);
        String yearString = dFormat.format(num);
        return Double.valueOf(yearString);
    }
}
