package teamit.hust.ktxcdshustbe.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "capabilities")
public class Capabilities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_capability")
    private Integer idCapability;
    @Column(name = "name")
    private String name;
    @Column(name = "cap_type")
    private String capType;
    @Column(name = "status")
    private Integer status;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "component")
    private String component;
    @ManyToMany(mappedBy = "capabilities")
    private Set<Role> roleSet;
}
