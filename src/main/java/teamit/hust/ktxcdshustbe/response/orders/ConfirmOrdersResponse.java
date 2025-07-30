package teamit.hust.ktxcdshustbe.response.orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmOrdersResponse {
    @JsonProperty("session_id")
    private String sessionId;
    @JsonProperty("collection_url")
    private String collectionUrl;
    @JsonProperty("payment_url")
    private String paymentUrl;
    @JsonProperty("exp_time")
    private String expTime;
}
