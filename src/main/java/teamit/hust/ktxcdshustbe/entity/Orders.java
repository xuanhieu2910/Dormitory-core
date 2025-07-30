package teamit.hust.ktxcdshustbe.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Integer idOrder;
    @Column(name = "title_order")
    private String titleOrder;
    @Column(name = "code_order")
    private String codeOrder;
    @Column(name = "id_user")
    private Integer idUser;
    @Column(name = "status_order")
    private Integer statusOrder;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "id_transaction_payment")
    private Integer idTransactionPayment;
    @Column(name = "id_user_created")
    private Integer idUserCreated;
    @Column(name = "id_user_modified")
    private Integer idUserModified;
    @Column(name = "total_money")
    private String totalMoney;
    @Column(name = "type_order")
    private Integer typeOrder;
    @Column(name = "value")
    private String value;

}
