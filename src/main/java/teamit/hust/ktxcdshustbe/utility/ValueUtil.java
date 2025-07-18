package teamit.hust.ktxcdshustbe.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class ValueUtil {

    private ValueUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getStringByObject(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static Double getDoubleByObject(Object obj) {
        if (obj == null) {
            return null;
        }
        return Double.valueOf(obj.toString());
    }

    public static Date getDateByObject(Object obj) {
        if (obj == null) {
            return null;
        }
        return (Date) obj;
    }

    public static LocalDateTime getLocalDateTimeByObject(Object obj) {
        if (obj == null || obj.toString().isEmpty()) {
            return null;
        }
        return ((Timestamp) obj).toLocalDateTime();
    }

    public static Long getLongByObject(Object obj) {
        if (obj == null || obj.toString().isEmpty()) {
            return null;
        }
        return Long.valueOf(obj.toString());
    }

    public static Integer getIntegerByObject(Object obj) {
        if (obj == null || obj.toString().isEmpty()) {
            return null;
        }
        return Integer.valueOf(obj.toString());
    }

    public static Timestamp getTimestampByObject(Object obj) {
        if (obj == null) {
            return null;
        }
        return (Timestamp) obj;
    }

    public static Float getFloatByObject(Object obj) {
        if (obj == null || obj.toString().isEmpty()) {
            return null;
        }
        return Float.valueOf(obj.toString());
    }


    public static String getClobString(Clob clob){
        if (null != clob) {
            BufferedReader stringReader = null;
            try {
                stringReader = new BufferedReader(
                        clob.getCharacterStream());

                String singleLine = null;
                StringBuffer strBuff = new StringBuffer();
                while ((singleLine = stringReader.readLine()) != null) {
                    strBuff.append(singleLine);
                }
                return strBuff.toString();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String formatCurrency(Double fee) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        return currencyVN.format(fee);
    }

    public static String formatCurrencyUSD(Double fee) {
        Locale localeVN = new Locale("en", "US");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        return currencyVN.format(fee);
    }

    public static Boolean getBooleanByObject(Object obj) {
        return (obj != null && "1,true".contains(obj.toString()));
    }

}
