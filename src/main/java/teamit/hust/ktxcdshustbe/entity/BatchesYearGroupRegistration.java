package teamit.hust.ktxcdshustbe.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "batches_year_group_registration")
public class BatchesYearGroupRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_batches_year_group_registration")
    private Integer idBatchesYearGroupRegistration;
    @Column(name = "id_batches_registration")
    private Integer idBatchesRegistration;
    @Column(name = "id_year_group")
    private Integer idYearGroup;
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
