package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_item")
    private Integer idOrderItem;
    @Column(name = "code_order_item")
    private String codeOrderItem;
    @Column(name = "id_order")
    private Integer idOrder;
    @Column(name = "id_object")
    private Integer idObject;
    @Column(name = "id_user")
    private Integer idUser;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "total_money")
    private String totalMoney;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "id_user_created")
    private Integer idUserCreated;
    @Column(name = "id_user_modified")
    private Integer idUserModified;
    @Column(name = "notes")
    private String notes;
    @Column(name = "status")
    private Integer status;
    @Column(name = "type_order")
    private Integer typeOrder;
}
