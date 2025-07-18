package teamit.hust.ktxcdshustbe.utility;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CodeUserUtil {

    private static int LENGTH_MAX = 4;
    public static String autoGenerateSecureRandomUser(Integer idUserCurrent){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int format = LENGTH_MAX;
        if (String.valueOf(idUserCurrent).length() > LENGTH_MAX) {
            format = String.valueOf(idUserCurrent).length();
        }
        return String.valueOf(year) + String.format("%0" + format + "d",idUserCurrent);
    }

    public static String autoGenerateSecureRandomUserAdministrator(String userName){
        return UUID.nameUUIDFromBytes(userName.getBytes()).toString();
    }
}
