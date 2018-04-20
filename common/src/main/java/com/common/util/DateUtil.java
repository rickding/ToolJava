package com.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 2017/9/23.
 */
public class DateUtil {
    public static String getTodayStr() {
        return getTodayStr("MMdd");
    }

    public static String getTodayStr(String format) {
        return DateUtil.format(new Date(), format);
    }

    public static Date getToday() {
        return DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Date date, String format) {
        if (date == null || StrUtil.isEmpty(format)) {
            return "";
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception e) {
            System.out.printf("%s, %s\r\n", e.getMessage(), format);
        }
        return "";
    }

    public static Date parse(String value, String[] formatArray) {
        return parse(value, formatArray, false);
    }

    public static Date parse(String value, String[] formatArray, boolean showError) {
        if (!StrUtil.isEmpty(value) && formatArray != null && formatArray.length > 0) {
            for (String format : formatArray) {
                Date date = DateUtil.parse(value, format, showError);
                if (date != null) {
                    return date;
                }
            }
        }

        if (showError) {
            System.out.printf("Error when parseDate: %s, %s\r\n", value, Arrays.asList(formatArray).toString());
        }
        return null;
    }

    public static Date parse(String str) {
        return parse(str, "yyyy-MM-dd HH:mm:ss", true);
    }

    public static Date parse(String str, String format) {
        return parse(str, format, true);
    }

    public static Date parse(String str, String format, boolean showError) {
        if (StrUtil.isEmpty(str) || StrUtil.isEmpty(str.trim()) || StrUtil.isEmpty(format)) {
            return null;
        }

        try {
            DateFormat df = new SimpleDateFormat(format);
            return df.parse(str.trim());
        } catch (ParseException e) {
            if (showError) {
                System.out.printf("%s, %s\r\n", e.getMessage(), format);
            }
        }
        return null;
    }

    public static int diffDates(String date1, String date2) {
        return diffDates(DateUtil.parse(date1, "yyyy-MM-dd"), DateUtil.parse(date2, "yyyy-MM-dd"));
    }

    public static int diffDates(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return 0;
        }
        return (int) ((d1.getTime() - d2.getTime()) / (1000 * 3600 * 24));
    }

    public static int dayOfWeek(String strDate) {
        return dayOfWeek(parse(strDate, "yyyy-MM-dd"));
    }

    public static int dayOfWeek(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static Date adjustDate(Date date, int days) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    /**
     * Format the date weekly (sprint)
     *
     * @param date
     * @return
     */
    public static Date getSprintDate(Date date) {
        return getSprintDate(date, false);
    }

    public static Date getSprintDate(Date date, boolean adjust) {
        if (date == null) {
            return null;
        }

        Date today = new Date();
        if (adjust && date.compareTo(today) < 0) {
            date = today;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        int day = cal.get(Calendar.DAY_OF_WEEK);
        day = (Calendar.SUNDAY - day + 7) % 7;
        if (day == 0) {
            day = 7;
        }
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    /**
     * Is Saturday or Sunday
     *
     * @param date
     * @return
     */
    public static boolean isWeekend(Date date) {
        if (date == null) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        return Calendar.SATURDAY == day || Calendar.SUNDAY == day;
    }
}
