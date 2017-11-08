package com.fox.exercise.weibo.sina;

/**
 * @author @Cundong
 * @version 1.0
 * @weibo http://weibo.com/liucundong
 * @blog http://www.liucundong.com
 * @date Apr 29, 2011 2:50:48 PM
 */
public class StringUtils {
    /**
     * @param input
     * @return boolean
     */
    public static boolean isBlank(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(String s) {
        if (s == null || s.length() == 0)
            return true;
        else
            return false;

    }
}