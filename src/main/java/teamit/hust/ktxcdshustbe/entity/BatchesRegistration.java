package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "batches_registration")
public class BatchesRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_batches_registration")
    private Integer idBatchesRegistration;
    @Column(name = "title")
    private String title;
    @Column(name = "code_batches_registration")
    private String codeBatchesRegistration;
    @Column(name = "id_semester")
    private Integer idSemester;
    @Column(name = "description")
    private String description;
    @Column(name = "notes")
    private String notes;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
}
