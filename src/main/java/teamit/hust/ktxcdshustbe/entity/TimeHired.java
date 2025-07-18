package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "time_hired")
public class TimeHired {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_time_hired")
    private Integer idTimeHired;
    @Column(name = "time_started")
    private Long timeStarted;
    @Column(name = "time_ended")
    private Long timeEnded;
    @Column(name = "status")
    private Integer status;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "id_user_created")
    private Integer idUserCreated;
    @Column(name = "id_user_modified")
    private Integer idUserModified;
}
