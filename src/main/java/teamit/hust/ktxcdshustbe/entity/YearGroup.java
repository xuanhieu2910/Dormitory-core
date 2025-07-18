package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "year_group")
public class YearGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_year_group")
    private Integer idYearGroup;
    @Column(name = "code_year_group")
    private String codeYearGroup;
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
