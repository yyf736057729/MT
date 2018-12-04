package org.spring.springboot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    private final static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy/MM/dd");
    private final static SimpleDateFormat yyyyMMdd = new SimpleDateFormat(
            "yyyyMMdd");
    private final static SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat(
            "yyyy-MM-dd");
    private final static SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat(
            "yyyyMMddHHmm");
    private final static SimpleDateFormat sdf4 = new SimpleDateFormat(
            "yyyyMMdd");
    private final static SimpleDateFormat sdf1 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");

    private final static SimpleDateFormat sdf2 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private final static SimpleDateFormat sdf3 = new SimpleDateFormat(
            "HHmm");

    private final static SimpleDateFormat yyyyMMddhhmmssSSS = new SimpleDateFormat(
            "yyyyMMddhhmmssSSS");

    private final static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat(
            "yyyyMMddhhmmss");

    private final static SimpleDateFormat yyyyMMddHHmmss2 = new SimpleDateFormat(
            "yyyyMMddHHmmss");

    public String simpleDate(Date date) {
        if(date == null)
            return "";
        return sdf.format(date);
    }

    public static String simpleDate1(String date1) {
        if(date1 == null)
            return "";
        return sdf.format(date1);
    }

    public static String simpleDate2(Date date) {
        if(yyyyMMddhhmmssSSS == null)
            return "";
        return yyyyMMddhhmmssSSS.format(date);

    }

    public static String formatDate1(Date date) {
        if(date == null)
            return "";
        return yyyy_MM_dd.format(date);
    }

    public static String dateToString2(Date date) {
        try {
            return yyyyMMddHHmmss.format(date);
        } catch(Exception e) {
        }
        return null;
    }

    public static String dateToString3(Date date) {
        try {
            return yyyyMMddHHmmss2.format(date);
        } catch(Exception e) {
        }
        return null;
    }

    public static Date parseDate2(String date) {
        try {
            return sdf4.parse(date);
        } catch(ParseException e) {
        }
        return new Date();
    }

    public static String stringDate10(String date) {
        return DateUtil.formatDate1(parseDate2(date));
    }

    /**
     * 取当前时间
     * @return
     */
    public static String getCurrentTime() {

        Calendar calendar = Calendar.getInstance();

        int i = calendar.get(1);
        int j = calendar.get(2) + 1;
        int k = calendar.get(5);
        return "" + i + (j >= 10 ? "" + j : "0" + j)
                + (k >= 10 ? "" + k : "0" + k);
    }

    public static Date calculateDate(Date startDay, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDay);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return cal.getTime();
    }

    public static Date calculateMonth(Date startDay, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDay);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * String 获取当前时间yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentDateTime() {
        return dateToString4(getCurrentDate());
    }

    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * String 获取今天日期yyyyMMdd
     * @return
     */
    public static String getToday() {
        Calendar calendar = Calendar.getInstance();
        return DateUtil.getyyyyMMdd(calendar.getTime());
    }

    /**
     * String 获取明天日期yyyyMMdd
     * @return
     */
    public static String getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return DateUtil.getyyyyMMdd(calendar.getTime());
    }

    /**
     * String 获取几天后日期yyyyMMdd
     * @return
     */
    public static String getDateByDays(Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return DateUtil.getyyyyMMdd(calendar.getTime());
    }

    /**
     * String 获取几天后日期yyyy-MM-dd
     * @return
     */
    public static String getDateByDays2(Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return DateUtil.getyyyy_MM_dd(calendar.getTime());
    }

    /**
     * 获取几天(days)前的时间
     * @param days
     * @return
     */
    public static Date getDateByBeforeDays(Integer days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }
    /**
     * 获取当前时间的前8个小时的时间
     * 
     * @param utcTime 时间
     * @return
     */
    public static Date formatStrDateToUTCStr(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//date 换成已经已知的Date对象
        cal.add(Calendar.HOUR_OF_DAY, 8);// before 8 hour
        return cal.getTime();
    }


    /**
     * 获取几天(days)前的日期
     * @param days
     * @return
     */
    public static String getDateByDay_yyyy_hh_ss(Integer days) {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -days);
        String format = sdf.format(c.getTime());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = null;
//        try {
//            date = yyyy_MM_dd.parse(format);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        return format;
    }

    /**
     * 获取几天(days)前的日期
     * @param days
     * @return
     */
    public static String getDateByDay_Date(Integer days) {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -days);
        String format = sdf.format(c.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:ss");
        Date date = null;
        try {
            date = yyyy_MM_dd.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format;
    }


    /**
     * 将日期格式化为字符串
     * @param date
     * @return
     */
    @Deprecated
    public static String dateToString(Date date, String pattern) {
        if(date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String getyyyyMMdd(Date date) {
        if(date == null)
            return "";
        return yyyyMMdd.format(date);
    }

    public static String getyyyy_MM_dd(Date date) {
        if(date == null)
            return "";
        return sdf2.format(date);
    }

    public static Date StringToDate(String date) {
        try {
            return yyyyMMdd.parse(date);
        } catch(ParseException e) {
        }
        return new Date();
    }

    /**
     * String 获取时间 yyyy-MM-dd HH:mm
     * @param date
     * @return
     */
    public static String format1(Date date) {
        if(date == null)
            date = new Date();
        return sdf1.format(date);
    }

    public static String dateToString1(Date date) {
        try {
            return sdf1.format(date);
        } catch(Exception e) {
        }
        return null;
    }
    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1,Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }

    /**
     * 获取当天日期
     */
    public static String StringDateDay(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(dt);
        return format;
    }

    /**
     * Date 获取时间 yyyy-MM-dd HH:mm
     * @param String
     * @return Date
     */
    public static Date StringToDate1(String date) {
        try {
            return sdf1.parse(date);
        } catch(ParseException e) {
        }
        return new Date();
    }

    /**
     * Date 获取时间 yyyy/MM/dd
     * @param String
     * @return Date
     */
    public static Date StringToDate5(String date) {
        try {
            return sdf.parse(date);
        } catch(ParseException e) {
        }
        return new Date();
    }

    /**
     * Date 获取时间yyyyMMddHHmm
     * @param String
     *            yyyyMMddHHmm
     * @return Date
     */
    public static Date StringToDate2(String date) {
        try {
            return yyyyMMddHHmm.parse(date);
        } catch(ParseException e) {
        }
        return new Date();
    }

    /**
     * Date 获取时间yyyyMMdd
     * @param String
     *            yyyyMMdd
     * @return Date
     */
    public static Date StringToDate3(String date) {
        try {
            return yyyyMMdd.parse(date);
        } catch(ParseException e) {
        }
        return new Date();
    }

    public static Date StringToDate4(String date) {
        try {
            return sdf2.parse(date);
        } catch(ParseException e) {
        }
        return new Date();
    }

    public static String dateToString4(Date date) {
        try {
            return sdf2.format(date);
        } catch(Exception e) {
        }
        return null;
    }

    public static String getHHmm(Date date) {
        try {
            return sdf3.format(date);
        } catch(Exception e) {
        }
        return null;
    }

    public static Integer getWeekId(String date) {
        Date d = new Date();
        try {
            d = yyyyMMdd.parse(date);
        } catch(ParseException e) {
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        Integer i = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return i;
    }

    /**
     * 获取周几
     * @param date
     *            yyyyMMdd
     * @return
     */
    public static String getWeek(String date) {
        String week = "";
        switch(getWeekId(date)) {
        case 1:
            week = "星期一";
            break;
        case 2:
            week = "星期二";
            break;
        case 3:
            week = "星期三";
            break;
        case 4:
            week = "星期四";
            break;
        case 5:
            week = "星期五";
            break;
        case 6:
            week = "星期六";
            break;
        case 0:
            week = "星期日";
            break;

        default:
            break;
        }
        return week;
    }

    public static Integer getWeekId2(String date) {
        Date d = new Date();
        try {
            d = yyyy_MM_dd.parse(date);
        } catch(ParseException e) {
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        Integer i = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return i;
    }

    /**
     * 获取周几
     * @param date
     *            yyyyMMdd
     * @return
     */
    public static synchronized String getWeek2(String date) {
        String week = "";
        switch(getWeekId2(date)) {
        case 1:
            week = "星期一";
            break;
        case 2:
            week = "星期二";
            break;
        case 3:
            week = "星期三";
            break;
        case 4:
            week = "星期四";
            break;
        case 5:
            week = "星期五";
            break;
        case 6:
            week = "星期六";
            break;
        case 0:
            week = "星期日";
            break;

        default:
            break;
        }
        return week;
    }

    /**
     * 时间计算（日）
     * @param date
     * @param n
     * @return
     */
    public static Date getDate(Date date, int n) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DAY_OF_MONTH, n);
        return gc.getTime();
    }

    public static Date getDate(Date date, int field, int n) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(field, n);
        return gc.getTime();
    }

    public static Date weekToDate(Number n) {
        Integer w = n.intValue();
        Calendar calendar = Calendar.getInstance();
        Integer t = calendar.get(Calendar.DAY_OF_WEEK);
        t = t - 1 == 0 ? 7 : t - 1;
        Integer i = w - t < 0 ? w - t + 7 : w - t;
        calendar.add(Calendar.DAY_OF_MONTH, i);
        return calendar.getTime();
    }

    public static boolean isEqual(Date one, Date another) {
        if(one == null || another == null)
            return false;
        return yyyyMMdd.format(one).equals(yyyyMMdd.format(another));
    }

    // 当前时间是上午还是下午（0：上午；1：下午）
    public static int AM_PM() {
        GregorianCalendar ca = new GregorianCalendar();
        return ca.get(GregorianCalendar.AM_PM);
    }

    public static String getyyyy_MM_dd3(String date) throws ParseException {
        if(date == null)
            return "";
        String s = "";
        try {
            s = yyyy_MM_dd.format(yyyyMMdd.parse(date));
        } catch(Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }

    // 多少分钟前后的时间date
    public static Date getDateYY_MM_DD(Integer m) {
        try {
            Calendar ca = Calendar.getInstance();
            ca.add(Calendar.MINUTE, m);
            return sdf1.parse(format1(ca.getTime()));
        } catch(Exception e) {
        }
        return null;
    }

    // 限制时间：参数minute，和date

    public static Boolean inLimitDate(Integer m, Date date) {
        try {
            return getDateYY_MM_DD(m).before(date);
        } catch(Exception e) {
        }
        return null;
    }

    // 限制时间：参数minute，和date

    public static int inLimitYYTime(String am) {
        String nowHm = getHHmm(new Date());
        Integer hs = Integer.parseInt(nowHm);
        int flag = 0;
        if("A".equals(am)) {
            if((601 < hs && hs <= 1159)) {
                flag = 1;
            } else if((hs > 1159)) {
                flag = 2;
            } else if((hs < 600)) {
                flag = 3;
            }
        }
        if("P".equals(am)) {
            if((hs <= 1559)) {
                flag = 1;
            } else if((hs > 1559)) {
                flag = 4;
            }
        }
        return flag;
    }

    // 限制时间：参数minute，和date

    public static int inLimitGHTime() {
        String nowHm = getHHmm(new Date());
        Integer hs = Integer.parseInt(nowHm);
        int flag = 0;
        if((1100 < hs && hs <= 1200)) {
            flag = 1;
        }
        if((hs < 600)) {
            flag = 2;
        }
        if((hs > 1600)) {
            flag = 3;
        }
        return flag;
    }

    public static String getDay(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        int day = gc.get(Calendar.DAY_OF_MONTH);
        return day >= 10 ? "" + day : "0" + day;
    }

    public static void main(String args[]) {
        System.out.print(DateUtil.inLimitYYTime("P"));

    }
}
