package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_role")
    private Integer idUserRole;
    @Column(name = "id_user")
    private Integer idUser;
    @Column(name = "id_role")
    private Integer idRole;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column (name = "id_department")
    private Integer idDepartment;
    @Column(name = "picked")
    private Integer picked;
    @Column(name = "id_user_created")
    private Integer idUserCreated;
    @Column(name = "id_user_modified")
    private Integer idUserModified;
}
