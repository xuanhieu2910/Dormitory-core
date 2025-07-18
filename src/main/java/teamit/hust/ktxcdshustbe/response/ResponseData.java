package teamit.hust.ktxcdshustbe.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class ResponseData implements Serializable {

    private String errorCode;
    private Boolean success;
    private Object content;

    public ResponseData() {
        this.success = true;
    }

    public ResponseData(Object content) {
        this.content = content;
        this.success = true;
    }

    public ResponseData(String errorCode) {
        this.errorCode = errorCode;
        this.success = false;
    }

    public ResponseData(Boolean isSuccess, Object content) {
        this.content = content;
        this.success = isSuccess;
    }

    public ResponseData(Boolean isSuccess, String errorCode, Object content) {
        this.content = content;
        this.success = isSuccess;
        this.errorCode = errorCode;
    }
}
