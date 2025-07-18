package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role_allow_switch")
public class RoleAllowSwitch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role_allow_switch")
    private Integer idRoleAllowSwitch;
    @Column(name = "id_role")
    private Integer idRole;
    @Column(name = "allow_switch")
    private Integer allowSwitch;
    @Column(name = "status")
    private Integer status;
}
