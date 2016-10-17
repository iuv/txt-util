package com.jianpage.util;

import java.util.Map;

/**
 * Created by ixx on 2016/10/15.
 */
public class StringUtil {
    /**
     * 判断所以字符串不为空
     *
     * @param strs
     * @return
     */
    public static boolean allIsNotEmptys(String... strs) {
        boolean b = true;
        if (strs == null) {
            return false;
        }
        for (String s : strs) {
            if (s == null || s.isEmpty()) {
                b = false;
                break;
            }
        }
        return b;
    }

    /**
     * 将map中的数据根据需要输出log需要参数列表
     *
     * @param m
     * @param s
     * @return
     */
    public static String mapToLogString(Map m, String... s) {
        String ret = "";
        if (m != null) {
            for (String t : s) {
                ret += t + "=" + m.get(t) + ",";
            }
            ret = ret.length() > 1 ? ret.substring(0, ret.length() - 1) : ret;
        }
        return ret;
    }

    //取汉字长度
    public static int getWordCountRegex(String s) {

        s = s.replaceAll("[^\\x00-\\xff]", "**");
        int length = s.length();
        return length;
    }

    public static String subByAscii(String str, int length) {
        String s = str.replaceAll("[^\\x00-\\xff]", "``");
        s = s.substring(0, length);
        s = s.replaceAll("``", "a");
        s = s.replace("`", "");
        return str.substring(0, s.length());
    }

}
