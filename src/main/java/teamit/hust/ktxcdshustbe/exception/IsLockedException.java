package teamit.hust.ktxcdshustbe.exception;


import teamit.hust.ktxcdshustbe.dto.ErrorsDetails;
import teamit.hust.ktxcdshustbe.enums.HttpStatusCustom;

public class IsLockedException   extends RuntimeException {

    public ErrorsDetails toErrorsDetails() {
        ErrorsDetails errorsDetails = new ErrorsDetails();
        HttpStatusCustom httpStatusCustom = HttpStatusCustom.resolve("KTX401-005");
        errorsDetails.setCode(httpStatusCustom.getValue());
        errorsDetails.setCode(httpStatusCustom.getDescription());
        return errorsDetails;
    }

}
