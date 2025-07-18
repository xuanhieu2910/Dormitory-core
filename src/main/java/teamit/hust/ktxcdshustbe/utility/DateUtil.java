package teamit.hust.ktxcdshustbe.utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    public static final String FROM_DATE_FORMAT = "dd/MM/yyyy 00:00:00";
    public static final String TO_DATE_FORMAT = "dd/MM/yyyy 23:59:59";
    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_FORMAT_HH_MM = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_TIME_STAMP = "YYYY-MM-dd HH:mm:ss";
    public static final String HHMMSS_DDMMYYYY = "HH:mm:ss dd/MM/yyyy";
    public static final String DATE_FORMAT_UPLOAD = "ddMMyyyyHHmmssSSS";
    public static final String DDMMYYYY = "dd/MM/YYYY";
    public static final String DAY_DD_MONTH_MM_YEAR_YYYY = "'Ngày' dd 'tháng' MM 'năm' yyyy";
    public static final String YYYYMMDD_FOLDER = "yyyyMMdd";
    public static final String MMDDYYYYHHMMSS = "MM-dd-yyyy-HH-mm-ss"; //08-16-2020-23-59-59
    public static final String DATE_FORMAT_MINUTE = "dd/MM/yyyy HH:mm:00";
    public static final String DATE_FORMAT_YEAR = "yyyy-MM-dd";
    public static final String regexTime = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";

    public static final String regexDate = "(^(((0[1-9]|1[0-9]|2[0-8])[\\/](0[1-9]|1[012]))|((29|30|31)[\\/](0[13578]|1[02]))|((29|30)[\\/](0[4,6,9]|11)))[\\/](19|[2-9][0-9])\\d\\d$)|(^29[\\/]02[\\/](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)";
    public static final SimpleDateFormat cmdateFormat = new SimpleDateFormat(DATE_FORMAT);

    public static Date formatToDate(Date toDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TO_DATE_FORMAT);
        String strDate = dateFormat.format(toDate);
        try {
            return cmdateFormat.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date convertTimestampToDate(Date date) {
        return new Date(date.getTime());
    }

    public static Timestamp convertToTimestampFormat(String dateTime, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date parsedDate = dateFormat.parse(dateTime);
            return new Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    public static Timestamp formatTimestamp(Timestamp timestamp) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String strDate = dateFormat.format(timestamp);
            return (Timestamp) dateFormat.parse(strDate);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatToPattern(Date date, String pattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date formatFromDate(Date toDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FROM_DATE_FORMAT);
        String strDate = dateFormat.format(toDate);
        try {
            return cmdateFormat.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date formatDate(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        String strDate = dateFormat.format(date);
        try {
            return cmdateFormat.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date formatDatePattern(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatTimeLongToDateString(String timeLong, String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(new Date(Long.parseLong(timeLong)));
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date parseDatePattern(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String strDate = sdf.format(date);
        try {
            return cmdateFormat.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public synchronized static String getCurrentDateStr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_UPLOAD);
        return dateFormat.format(new Date());
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    public static Date getCurrentTimes() {
        return new Date();
    }

    public static String getTodayFolder() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYYMMDD_FOLDER);
        return dateFormat.format(new Date());
    }

    public static int daysBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return (int) ChronoUnit.DAYS.between(fromDate, toDate);
    }

    public static int weeksBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return (int) ChronoUnit.WEEKS.between(fromDate, toDate);
    }

    public static int monthsBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        int j = 0;
        for (int i = 0; i < ChronoUnit.MONTHS.between(fromDate, toDate); i++) {
            if (fromDate.getDayOfMonth() > toDate.plusMonths(i).getDayOfMonth()) {
                j--;
            } else {
                j++;
            }
        }
        return j;
    }

    public static String setMinute(Date date, int minus) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(MMDDYYYYHHMMSS);
        Date temp = new Date();
        temp.setTime(date.getTime() + (long) minus * 60 * 1000);
        return dateFormat.format(temp);
    }

    public static Date plusDay(Date date, int day) {
        return plusHour(date, day * 24);
    }

    public static Date plusMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    public static Date minusDay(Date date, int day) {
        return minusHour(date, day * 24);
    }

    public static Date plusHour(Date date, int hour) {
        return plusMinute(date, hour * 60);
    }

    public static Date minusHour(Date date, int hour) {
        return minusMinute(date, hour * 60);
    }

    public static Date plusMinute(Date date, int minus) {
        return plusSecond(date, minus * 60L);
    }

    public static Date minusMinute(Date date, int minus) {
        return minusSecond(date, minus * 60L);
    }

    public static Date plusSecond(Date date, long second) {
        try {
            Date temp = new Date();
            temp.setTime(date.getTime() + second * 1000);
            return temp;
        } catch (Exception e) {
            return null;
        }
    }

    public static Date minusSecond(Date date, long second) {
        try {
            Date temp = new Date();
            temp.setTime(date.getTime() - second * 1000);
            return temp;
        } catch (Exception e) {
            return null;
        }
    }

    public static String buildStringPattern(Date time, String pattern) {
        try {
            return new SimpleDateFormat(pattern).format(time);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date buildTimePattern(Date time, String pattern) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(new SimpleDateFormat(pattern).format(time));
        } catch (Exception e) {
            return null;
        }
    }

    public static Duration between(Date fromDate, Date toDate) {
        LocalDateTime fDate = convertToLocalDateTimeViaInstant(fromDate);
        LocalDateTime tDate = convertToLocalDateTimeViaInstant(toDate);
        return Duration.between(fDate, tDate);
    }

    public static int secondBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return (int) ChronoUnit.SECONDS.between(fromDate, toDate);
    }

    public static Long betweenDate(Date fromDate, Date toDate) {
        LocalDate fDate = toLocalDate(fromDate);
        LocalDate tDate = toLocalDate(toDate);
        Duration diff = Duration.between(fDate.atStartOfDay(), tDate.atStartOfDay());
        return diff.toDays();
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }


    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static boolean validateTimeRegex(String time){
        return time.matches(regexTime);
    }

    public static boolean validateDateRegex(String date){
        return date.matches(regexDate);
    }

}
