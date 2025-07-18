package teamit.hust.ktxcdshustbe.exception;

public class FileExcelException extends Exception {
    private String message;
    public FileExcelException(String message) {
        super(message);
    }
}
