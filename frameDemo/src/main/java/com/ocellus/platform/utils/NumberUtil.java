package com.ocellus.platform.utils;

import java.math.BigDecimal;

public class NumberUtil {
    public static boolean isNumber(String numStr) {
        try {
            if (numStr == null || numStr.trim().equals(""))
                return false;
            new BigDecimal(numStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static BigDecimal min(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) >= 0 ? num2 : num1;
    }

    public static BigDecimal max(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) >= 0 ? num1 : num2;
    }

    public static Integer toInteger(String str) {
        Integer integer = null;
        if (str != null && str.trim().length() > 0) {
            try {
                integer = new Integer(Integer.parseInt(str));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return integer;
    }

    public static Integer toInteger(Object object) {
        Integer integer = null;
        if (object != null) {
            if (object instanceof java.math.BigDecimal) {
                return ((java.math.BigDecimal) object).intValue();
            }

            String string = object.toString();
            integer = toInteger(string);
        }
        return integer;
    }


    public static BigDecimal toBigDecimal(String value) {
        if (isNumber(value)) {
            return new BigDecimal(value);
        }
        return null;

    }

    public static Double toDouble(String aString) {
        try {
            double value = 0.00;
            if (aString == null || aString.trim().equals(""))
                return 0.00;
            value = Double.parseDouble(aString.trim());
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BigDecimal getNotNullBigDecimal(BigDecimal number) {
        return getNotNullBigDecimal(number, BigDecimal.ZERO);
    }

    public static BigDecimal getNotNullBigDecimal(BigDecimal number,
                                                  BigDecimal defaultVal) {
        return number == null ? defaultVal : number;
    }

    public static BigDecimal add(BigDecimal number1, BigDecimal number2) {
        BigDecimal a = (number1 == null ? BigDecimal.ZERO : number1);
        BigDecimal b = (number2 == null ? BigDecimal.ZERO : number2);
        return a.add(b);
    }

    public static BigDecimal subtract(BigDecimal number1, BigDecimal number2) {
        BigDecimal a = (number1 == null ? BigDecimal.ZERO : number1);
        BigDecimal b = (number2 == null ? BigDecimal.ZERO : number2);
        return a.subtract(b);
    }
}
