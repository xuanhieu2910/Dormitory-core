package teamit.hust.ktxcdshustbe.exception;

public class ValidateFiledException extends Exception {
    private String message;
    public ValidateFiledException(String message) {
        super(message);
    }
}
