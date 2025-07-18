package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role_allow_override")
public class RoleAllowOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role_allow_override")
    private Integer idRoleAllowOverride;
    @Column(name = "id_role")
    private Integer idRole;
    @Column(name = "allow_override")
    private Integer allowOverride;
    @Column(name = "status")
    private Integer status;
}
