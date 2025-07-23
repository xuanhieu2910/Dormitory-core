package teamit.hust.ktxcdshustbe.exception;

import teamit.hust.ktxcdshustbe.dto.ErrorsDetails;
import teamit.hust.ktxcdshustbe.enums.HttpStatusCustom;

public class ExitsObjectException extends RuntimeException{
    public ErrorsDetails toErrorsDetails() {
        ErrorsDetails errorsDetails = new ErrorsDetails();
        HttpStatusCustom httpStatusCustom = HttpStatusCustom.resolve("KTX409-001");
        errorsDetails.setCode(httpStatusCustom.getValue());
        errorsDetails.setDescription(httpStatusCustom.getDescription());
        return errorsDetails;
    }

    public ExitsObjectException() {
        super();
    }

    public ExitsObjectException(String message) {
        super(message);
    }
}
