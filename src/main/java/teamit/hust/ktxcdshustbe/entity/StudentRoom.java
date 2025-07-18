package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;


@Getter
@Setter
@Entity
@Table(name = "student_room")
public class StudentRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_student_room")
    private Integer idStudentRoom;
    @Column(name = "id_user")
    private Integer idUser;
    @Column(name = "id_room")
    private Integer idRoom;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "id_time_hired")
    private Integer idTimeHired;
    @Column(name = "id_user_created")
    private Integer idUserCreated;
    @Column(name = "id_user_modified")
    private Integer idUserModified;
    @Column(name = "status")
    private Integer status;
}
