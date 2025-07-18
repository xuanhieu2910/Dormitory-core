package teamit.hust.ktxcdshustbe.entity;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "service_room")
public class ServiceRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service_room")
    private Integer idServiceRoom;
    @Column(name = "id_service")
    private Integer idService;
    @Column(name = "id_room")
    private Integer idRoom;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "status")
    private Integer status;
    @Column(name = "id_user_created")
    private Integer idUserCreated;
    @Column(name = "id_user_modified")
    private Integer idUserModified;
}
