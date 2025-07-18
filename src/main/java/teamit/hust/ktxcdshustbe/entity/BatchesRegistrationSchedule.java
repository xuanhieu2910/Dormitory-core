package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "batches_registration_schedule")
public class BatchesRegistrationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_batches_registration_schedule")
    private Integer idBatchesRegistrationSchedule;
    @Column(name = "id_batches_registration")
    private Integer idBatchesRegistration;
    @Column(name = "id_priority_group")
    private Integer idPriorityGroup;
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
    @Column(name = "registration_start_time")
    private Long registrationStartTime;
    @Column(name = "registration_end_time")
    private Long registrationEndTime;

}
