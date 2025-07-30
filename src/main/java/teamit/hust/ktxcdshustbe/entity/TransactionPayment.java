package teamit.hust.ktxcdshustbe.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction_payment")
public class TransactionPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction_payment")
    private Integer idTransactionPayment;
    @Column(name = "id_order")
    private Integer idOrder;
    @Column(name = "code_transaction_payment")
    private String codeTransactionPayment;
    @Column(name = "status")
    private Integer status;
    @Column(name = "type")
    private Integer type;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "json_data")
    private String jsonData;
    @Column(name = "check_sum")
    private String checkSum;
    @Column(name = "return_url")
    private String returnUrl;
    @Column(name = "cancel_url")
    private String cancelUrl;
    @Column(name = "method_payment")
    private String methodPayment;
    @Column(name = "merchant_id")
    private String merchantId;
    @Column(name = "terminal_id")
    private String terminalId;

}
