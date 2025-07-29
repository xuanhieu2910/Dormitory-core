package teamit.hust.ktxcdshustbe.request.transactionPayment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallBackPaymentRequest {

    private String merchant_id;
    private String terminal_id;
    private String txn_code;
    private String txn_amount;
    private String txn_fee;
    private String txn_currency;
    private String order_id;
    private String order_status;
    private String payment_method;
    private String payment_card_brand;
    private String payment_card_bin;
    private String payment_card_suffix;
    private String token_card_id;
    private String token_card_brand;
    private String token_card_bin;
    private String token_card_suffix;
    private String token_value;
    private String result_code;
    private String result_explicit_code;
    private String signature;


}

