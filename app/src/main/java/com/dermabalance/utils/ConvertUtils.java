package com.dermabalance.utils;

public class ConvertUtils {

    /**
     * Convert string value to long.
     * @param value to parse
     * @return long value
     */
    public static long getLong(final String value) {
        try {
            return Long.parseLong(value);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * Convert string value to int.
     * @param value to parse
     * @return int value
     */
    public static int getInt(final String value) {
        try {
            return Integer.parseInt(value);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Convert string value to double.
     * @param value to parse
     * @return double value
     */
    public static double getDouble(final String value) {
        try {
            return Double.parseDouble(value);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
