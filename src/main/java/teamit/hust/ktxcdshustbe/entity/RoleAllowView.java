package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role_allow_view")
public class RoleAllowView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role_allow_view")
    private Integer idRoleAllowView;
    @Column(name = "id_role")
    private Integer idRole;
    @Column(name = "allow_view")
    private Integer allowView;
    @Column(name = "status")
    private Integer status;
}
