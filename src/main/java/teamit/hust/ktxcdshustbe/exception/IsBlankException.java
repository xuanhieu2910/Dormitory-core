package teamit.hust.ktxcdshustbe.exception;


import lombok.Getter;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.dto.ErrorsDetails;
import teamit.hust.ktxcdshustbe.enums.HttpStatusCustom;

@Getter
@Setter
public class IsBlankException  extends RuntimeException{
    public ErrorsDetails toErrorsDetails() {
        ErrorsDetails errorsDetails = new ErrorsDetails();
        HttpStatusCustom httpStatusCustom = HttpStatusCustom.resolve("KTX400-002");
        errorsDetails.setCode(httpStatusCustom.getValue());
        errorsDetails.setDescription(httpStatusCustom.getDescription());
        return errorsDetails;
    }
}
