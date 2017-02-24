package com.ocellus.platform.utils;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil implements Converter {

    private static final Logger logger = Logger.getLogger(DateUtil.class);

    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATETIME_PATTERN_NO_SECOND = "yyyy-MM-dd HH:mm";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String MONTH_PATTERN = "yyyy-MM";


    public static Date dateAfter(Date date, int number, int field) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field, number);
        return c.getTime();
    }

    public static Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    public static boolean isLastDayOfMonth(java.util.Date dt) {
        if (dt == null) return false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.DATE, 1);
        return cal.get(Calendar.DATE) == 1;
    }

    public static boolean isFirstDayOfMonth(java.util.Date dt) {
        if (dt == null) return false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.DATE) == 1;
    }

    @SuppressWarnings("rawtypes")
    public Object convert(Class type, Object value) {
        Object result = null;
        if (type == Date.class) {
            try {
                result = doConvertToDate(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (type == String.class) {
            result = doConvertToString(value);
        }
        return result;
    }

    public Object convertToString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String result = null;
        if (date instanceof Date) {
            result = simpleDateFormat.format(date);
        }
        return result;
    }

    /**
     * 日期转字符串，静态方法
     *
     * @param date
     * @param format 要转换的格式
     * @return
     */
    public static String date2Str(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String result = null;
        if (date instanceof Date) {
            result = simpleDateFormat.format(date);
        }
        return result;
    }

    /**
     * Convert String to Date
     *
     * @param value
     * @return
     * @throws ParseException
     */
    private Date doConvertToDate(Object value) throws ParseException {
        Date result = null;

        if (value instanceof String) {
            result = DateUtils.parseDate((String) value, new String[]{DATE_PATTERN, DATETIME_PATTERN,
                    DATETIME_PATTERN_NO_SECOND, MONTH_PATTERN});

            // all patterns failed, try a milliseconds constructor
            if (result == null && StringUtils.isNotEmpty((String) value)) {

                try {
                    result = new Date(new Long((String) value).longValue());
                } catch (Exception e) {
                    logger.error("Converting from milliseconds to Date fails!");
                    e.printStackTrace();
                }

            }

        } else if (value instanceof Object[]) {
            // let's try to convert the first element only
            Object[] array = (Object[]) value;

            if (array.length >= 1) {
                value = array[0];
                result = doConvertToDate(value);
            }

        } else if (Date.class.isAssignableFrom(value.getClass())) {
            result = (Date) value;
        }
        return result;
    }

    /**
     * Convert Date to String
     *
     * @param value
     * @return
     */
    private String doConvertToString(Object value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        String result = null;
        if (value instanceof Date) {
            result = simpleDateFormat.format(value);
        }
        return result;
    }

    public static boolean isDate(String aDate, String format) {
        if (aDate == null || aDate.trim().equals(""))
            return false;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        try {
            sdf.parse(aDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static String format(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static java.util.Date parseDate(String dtStr, String format) throws Exception {
        if (dtStr == null || dtStr.trim().equals(""))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dtStr);

    }

    public static Date formatTmsDate(String date) throws Exception {
        if (date == null || date.trim().equals(""))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
        return sdf.parse(date.replace("T", " "));
    }

    /**
     * @param time eg:15:00:00
     */
    public static boolean isAfterTime(String time) throws Exception {
        Date today = getDate();
        String todayStr = format(today, DATE_PATTERN);
        Date dateTime = parseDate(todayStr + " " + time, DATETIME_PATTERN);
        return today.after(dateTime);
    }

    public static Date beginOfDate(Date dt) {
        if (dt == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Date(cal.getTimeInMillis());
    }

    public static Date endOfDate(Date dt) {
        if (dt == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return new Date(cal.getTimeInMillis());
    }

    public static Timestamp dayBegin(Date dt) {
        if (dt == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    public static Timestamp dayEnd(Date dt) {
        if (dt == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    public static int getDayOfMonth(Date dt) {
        if (dt == null)
            return -1;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfWeek(Date dt) {
        if (dt == null)
            return -1;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String getTheFirstDayofMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = format(c.getTime(), DATE_PATTERN);
        return first;
    }

    public static String getTheLastDayofMonth() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format(ca.getTime(), DATE_PATTERN);
        return last;
    }

    public static int daysAfter(Date dt1, Date dt2) {
        if (dt1 == null || dt2 == null)
            return 0;
        float diff = datePart(dt2).getTime() - datePart(dt1).getTime();
        float days = Math.round(diff / (1000 * 60 * 60 * 24));
        return new Float(days).intValue();
    }

    public static Date datePart(Date date) {
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Date(cal.getTimeInMillis());
    }

    public static List getDays(String startDate, String endDate) throws ParseException {
        SimpleDateFormat formatStart = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
        formatStart.parse(startDate);
        Calendar clStart = formatStart.getCalendar();
        sdfEnd.parse(endDate);
        Calendar clEnd = sdfEnd.getCalendar();
        List<String> selectedDate = new ArrayList();
        while (true) {
            if (clStart.before(clEnd) || clStart.equals(clEnd)) {
//                System.out.println(formatStart.format(clStart.getTime()));
                selectedDate.add(formatStart.format(clStart.getTime()).toString());
                clStart.add(clStart.DAY_OF_MONTH, 1);
                clStart.set(clStart.DAY_OF_MONTH, clStart.get(clStart.DAY_OF_MONTH));
            } else {
                break;
            }
        }
        return selectedDate;
    }

    public static boolean isSameDate(Date date0, Date date1) {
        if (date0 == null || date1 == null)
            return false;
        return daysAfter(date0, date1) == 0;
    }

    public static void main(String[] args) {
        try {
            System.out.println(DateUtil.getDayOfMonth(new Date()));
            System.out.println(DateUtil.getDate());
            System.out.println(DateUtil.date2Str(DateUtil.getDate(), DateUtil.DATE_PATTERN));
            System.out.println(DateUtil.parseDate(DateUtil.date2Str(DateUtil.getDate(), DateUtil.DATE_PATTERN), DateUtil.DATE_PATTERN));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
