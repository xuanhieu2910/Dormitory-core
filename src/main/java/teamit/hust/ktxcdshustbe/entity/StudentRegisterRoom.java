package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Getter
@Setter
@Entity
@Table(name = "student_register_room")
public class StudentRegisterRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_student_register_room")
    private Integer idStudentRegisterRoom;
    @Column(name = "id_user")
    private Integer idUser;
    @Column(name = "id_room")
    private Integer idRoom;
    @Column(name = "id_time_hired")
    private Integer idTimeHired;
    @Column(name = "status")
    private Integer status;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "id_user_modified")
    private Integer idUserModified;
    @Column(name = "id_user_created")
    private Integer idUserCreated;
    @Column(name = "id_order")
    private Integer idOrder;
    @Column(name = "id_batches_registration")
    private Integer idBatchesRegistration;
    @Column(name = "expires_at")
    private Long expiresAt;
}
