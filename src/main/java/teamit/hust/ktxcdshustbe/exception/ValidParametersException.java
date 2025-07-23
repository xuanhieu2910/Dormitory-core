package teamit.hust.ktxcdshustbe.exception;


import teamit.hust.ktxcdshustbe.dto.ErrorsDetails;
import teamit.hust.ktxcdshustbe.enums.HttpStatusCustom;

public class ValidParametersException extends RuntimeException {
    public ErrorsDetails toErrorsDetails() {
        ErrorsDetails errorsDetails = new ErrorsDetails();
        HttpStatusCustom httpStatusCustom = HttpStatusCustom.resolve("KTX400-007");
        errorsDetails.setCode(httpStatusCustom.getValue());
        errorsDetails.setDescription(httpStatusCustom.getDescription());
        return errorsDetails;
    }

    public ValidParametersException() {
        super();
    }

    public ValidParametersException(String message) {
        super(message);
    }
}
