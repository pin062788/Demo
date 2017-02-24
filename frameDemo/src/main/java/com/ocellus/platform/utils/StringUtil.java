package com.ocellus.platform.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String getNonNullStr(Object o) {
        if (o == null) {
            return "";
        } else {
            return String.valueOf(o);
        }
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == null && str2 == null)
            return true;
        if (!isEmpty(str1) && !isEmpty(str2) && str1.equals(str2)) {
            return true;
        }
        return false;
    }

    // If string is less than aLength, pad on left.
    public static String lPad(String aString, int aLength, String aPadCharacter) {
        if (aString == null)
            return null;
        int len = aString.length();
        StringBuffer pad = new StringBuffer("");
        if (aLength > len) {
            for (int i = 1; i <= aLength - len; i++)
                pad.append(aPadCharacter);
            return pad.toString() + aString;
        }
        return aString;
    }

    /**
     * 去除字符；
     *
     * @param vStr
     * @return
     */
    public static String removeSymbol(String vStr) {
        if (!isEmpty(vStr)) {
            String regEx = "[-`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(vStr);
            return m.replaceAll("").trim();
        }
        return vStr;
    }

    public static String subString(String vStr, int start, int end) {
        if (!isEmpty(vStr) && vStr.length() >= end) {
            return vStr.substring(start, end);
        }
        return vStr;
    }

    public static List<String> toList(String str, String separator) {
        List<String> list = new ArrayList<String>();
        if (!isEmpty(str)) {
            if (str.indexOf(separator) > -1) {
                String[] arr = str.split(separator);
                for (String item : arr) {
                    if (!isEmpty(item)) {
                        list.add(item);
                    }
                }
            } else
                list.add(str);
        }
        return list;
    }

    public static String unEncode(String data) {
        if (null == data || "".equals(data)) return data;
        data = data.replaceAll("&quot;", "\"");
        data = data.replaceAll("&lt;", "<");
        data = data.replaceAll("&gt;", ">");
        data = data.replaceAll("&#92;", "\\\\");
        data = data.replaceAll("&apos;", "'");
        data = data.replaceAll("&amp;", "&");
        data = data.replaceAll("%3c%2f", "</");
        return data;
    }

    public static String getGUID() {
        return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
    }

    public static String[] toArray(String str, String separator) {
        return str.split(separator);
    }

    public static String listToString(List<String> stringList, String separator) {
        if (stringList == null) {
            return "";
        }
        if (isEmpty(separator)) {
            separator = Constants.COMA_STR;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(separator);
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }
}
