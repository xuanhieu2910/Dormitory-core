package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role_capabilities")
public class RoleCapabilities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role_capabilities")
    private Integer idRoleCapabilities;
    @Column(name = "id_role")
    private Integer idRole;
    @Column(name = "id_capabilities")
    private Integer idCapabilities;
    @Column(name = "permission")
    private Integer permission;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "id_user_created")
    private Integer idUserCreated;
    @Column(name = "id_user_modified")
    private Integer idUserModified;
}
