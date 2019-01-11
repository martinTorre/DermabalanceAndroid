package com.dermabalance.utils;

import java.text.DecimalFormat;

public class FormatUtils {
    private static DecimalFormat df2 = new DecimalFormat(".##");

    public static double get2Decimals(final double value) {
        return ConvertUtils.getDouble(df2.format(value));
    }
}
