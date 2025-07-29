package teamit.hust.ktxcdshustbe.enums;


import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public enum HttpStatusCustom {

    /**
    *  400 Bad request
    * */
    MISSING_PARAMETERS("KTX400-001", "Missing parameters!"),
    IS_BLANK("KTX400-002", "Parameters is blank, must not blank!"),
    IS_NULL("KTX400-003", "Parameters is null, must not null!"),
    SIZE("KTX400-004", "Parameters is invalid size!"),
    PATTERN("KTX400-005", "Invalid pattern signature!"),
    CHECKSUM_WRONG("KTX400-006", "Checksum wrong!"),
    VALID_PARAMETERS("KTX400-007", "Valid parameters, please check again!"),
    VALID_SIZE_FILE_UPLOAD("KTX400-008", "Valid limit size file, please check again!"),
    VALID_EXTENSION_FILE_UPLOAD("KTX400-009","Valid extension file, please check again!"),
    STRONG_PASSWORD("KTX400-010","Valid strong password, please check again!"),
    FILE_IS_NULL("KTX400-011","File is null, please chose another!"),
    VALID_FIELD("KTX400-012","Valid field, please check again!"),

    /**
     *  401 Unauthorized
     * */
    UNAUTHORIZED("KTX401-001", "Unauthorized!"),
    INVALID_CLINE_API_KEY("KTX401-002", "Invalid client id or api key!"),
    MISSING_PARTNER_HEADER("KTX401-003", "Missing partner service in header!"),
    EXPIRES_TIME("KTX401-004", "Expires time!"),
    ACCOUNT_LOCKED("KTX401-005", "Account locked!"),

    /**
     *  403 Forbidden
     * */
    FORBIDDEN("KTX403-001", "Forbidden!"),


    /**
     *  404 Not found
     * */
    NOT_FOUND("KTX404-001", "Not found, please check again!"),


    /**
     *  409 Conflict
     * */
    EXITS_OBJECT("KTX409-001", "Exits object, please check again!"),

    /**
     *
     *  499 SQL
     *
     * */
    UPDATE_FALSE("KTX499-001", "Execute data false!");

    private static final HttpStatusCustom[] VALUES = values();
    private final String value;
    private final String description;

    private HttpStatusCustom(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Nullable
    public static HttpStatusCustom resolve(String statusCode) {
        for(HttpStatusCustom status : VALUES) {
            if (status.value.equals(statusCode)) {
                return status;
            }
        }
        return null;
    }
}
