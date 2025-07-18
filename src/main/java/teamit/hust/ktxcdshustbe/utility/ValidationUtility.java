package teamit.hust.ktxcdshustbe.utility;

import teamit.hust.ktxcdshustbe.exception.StrongPasswordException;

public class ValidationUtility {

    public final static String MESSAGE_STRONG_PASSWORD = "Must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters.";
    public final static String PATTERN_STRONG_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$";


    public static boolean validateStrongPassword(String password) {
        if (password != null) {
            return password.matches(PATTERN_STRONG_PASSWORD);
        } else {
            throw new StrongPasswordException();
        }
    }
}
