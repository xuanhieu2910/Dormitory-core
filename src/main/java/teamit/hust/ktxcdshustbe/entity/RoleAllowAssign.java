package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "role_allow_assign")
public class RoleAllowAssign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role_allow_assign")
    private Integer idRoleAllowAssign;
    @Column(name = "id_role")
    private Integer idRole;
    @Column(name = "allow_assign")
    private Integer allowAssign;
    @Column(name = "status")
    private Integer status;
}
