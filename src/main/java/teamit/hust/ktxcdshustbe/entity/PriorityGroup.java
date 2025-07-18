package teamit.hust.ktxcdshustbe.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "priority_group")
public class PriorityGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_priority_group")
    private Integer idPriorityGroup;
    @Column(name = "priority_group_code")
    private String priorityGroupCode;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "id_user_created")
    private Integer idUserCreated;
    @Column(name = "id_user_modified")
    private Integer idUserModified;

}
