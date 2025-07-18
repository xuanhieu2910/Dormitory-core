package teamit.hust.ktxcdshustbe.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import teamit.hust.ktxcdshustbe.response.ResponseData;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto<T> {

    @JsonIgnore
    private HttpStatus httpStatus;

    private HttpHeaders httpHeaders;

    private int code;

    private String resCode;

    private T data;

    private ErrorsDetails error;

    private String message = "Thành công";

    public static <T> ApiResponseDto<T> build() {
        return new ApiResponseDto<>();
    }

    public static @ResponseBody
    ResponseEntity<?> ok(Object data, String message) {
        return build().withHttpStatus(HttpStatus.OK)
                .withData(data)
                .withMessage(message)
                .toResponseEntity();
    }

    public static <T> ResponseEntity<T> success(T body) {
        ResponseData responseData = new ResponseData(body);
        return new ResponseEntity(responseData, HttpStatus.OK);
    }


    public static @ResponseBody
    ResponseEntity<?> ok(Object data) {
        return build().withHttpStatus(HttpStatus.OK)
                .withData(data)
                .toResponseEntity();
    }

    public static @ResponseBody
    ResponseEntity<?> data(Object data) {
        return build().withHttpStatus(HttpStatus.OK)
                .withData(data)
                .toResponseEntity();
    }

    public static @ResponseBody
    ResponseEntity<?> createdWithMessage(String message, HttpStatus status) {
        return build().withHttpStatus(status)
                .withMessage(message)
                .toResponseEntity();
    }

    public static @ResponseBody
    ResponseEntity<?> createdWithErrors(ErrorsDetails errors, HttpStatus status) {
        return build().withHttpStatus(status)
                .withErrors(errors)
                .toResponseEntity();
    }


    public static @ResponseBody
    ResponseEntity<?> createdWithState(Object data, String message, HttpStatus status) {
        return build().withHttpStatus(status)
                .withData(data)
                .withMessage(message)
                .toResponseEntity();
    }

    public static @ResponseBody
    ResponseEntity<?> createdWithStateAuth(Object data, String message, HttpStatus status, HttpHeaders httpHeaders) {
        return build().withHttpStatus(status)
                .withData(data)
                .withMessage(message)
                .header(httpHeaders)
                .toResponseEntity();
    }

    public static @ResponseBody
    ResponseEntity<?> createdWithStateAuthor(Object data, String message, HttpStatus status) {
        return build().withHttpStatus(status)
                .withData(data)
                .withMessage(message)
                .toResponseEntity();
    }


    public static @ResponseBody
    ResponseEntity<?> createdWithCode(Object data, String message, String resCode, HttpStatus status) {
        return build().withHttpStatus(status)
                .withData(data)
                .withCode(resCode)
                .withMessage(message)
                .toResponseEntity();
    }


    @PostConstruct
    private void init() {
        httpStatus = HttpStatus.OK;
        code = httpStatus.value();
    }

    public ApiResponseDto<T> withHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        return this;
    }

    public ApiResponseDto<T> withData(T data) {
        this.data = data;
        return this;
    }

    public ApiResponseDto<T> header(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
        return this;
    }

    public ApiResponseDto<T> withCode(String resCode) {
        this.resCode = resCode;
        return this;
    }

    public ApiResponseDto<T> withMessage(String message) {
        this.message = message;
        return this;
    }

    public ApiResponseDto<T> withErrors(ErrorsDetails errorsDetails){
        this.error = errorsDetails;
        return this;
    }

    public ResponseEntity<ApiResponseDto<?>> toResponseEntity() {
        return new ResponseEntity<>(this, httpHeaders, httpStatus);
    }
}
