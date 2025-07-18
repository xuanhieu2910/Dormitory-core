package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "semester")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_semester")
    private Integer idSemester;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "status")
    private Integer status;

    @Column(name = "id_user_created")
    private Integer idUserCreated;

    @Column(name = "id_user_modified")
    private Integer idUserModified;

    @Column(name = "time_created")
    private Long timeCreated;

    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "code_semester")
    private String codeSemester;
}