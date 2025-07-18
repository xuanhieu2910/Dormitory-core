package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_sessions")
public class OrderSessions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_sessions")
    private Integer idOrderSessions;
    @Column(name = "id_order")
    private Integer idOrder;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "expires_at")
    private Long expiresAt;
    @Column(name = "retry")
    private Integer retry;
    @Column(name = "status")
    private Integer status;
    @Column(name = "data")
    private Integer data;

}
