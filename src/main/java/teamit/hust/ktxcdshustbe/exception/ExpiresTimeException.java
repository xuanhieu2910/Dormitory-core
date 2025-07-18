package teamit.hust.ktxcdshustbe.exception;


import teamit.hust.ktxcdshustbe.dto.ErrorsDetails;
import teamit.hust.ktxcdshustbe.enums.HttpStatusCustom;

public class ExpiresTimeException extends RuntimeException{
    public ErrorsDetails toErrorsDetails() {
        ErrorsDetails errorsDetails = new ErrorsDetails();
        HttpStatusCustom httpStatusCustom = HttpStatusCustom.resolve("KTX401-004");
        errorsDetails.setCode(httpStatusCustom.getValue());
        errorsDetails.setDescription(httpStatusCustom.getDescription());
        return errorsDetails;
    }
}
