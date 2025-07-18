package teamit.hust.ktxcdshustbe.entity;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room")
    private Integer idRoom;
    @Column(name = "title")
    private String title;
    @Column(name = "id_department")
    private Integer idDepartment;
    @Column(name = "sex_room")
    private Integer sexRoom;
    @Column(name = "price")
    private String price;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "id_user_created")
    private Integer idUserCreated;
    @Column(name = "id_user_modified")
    private Integer idUserModified;
    @Column(name = "is_active")
    private Integer isActive;
    @Column(name = "limit_amount_people")
    private Integer limitAmountPeople;
    @Column(name = "quantity_hired")
    private Integer quantityHired;
    @Column(name = "remain_amount")
    private Integer remainAmount;
    @Column(name = "limit_amount_people_register")
    private Integer limitAmountPeopleRegister;
    @Column(name = "quantity_registered")
    private Integer quantityRegistered;
    @Column(name = "remain_amount_register")
    private Integer remainAmountRegister;
    @Column(name = "code_room")
    private String codeRoom;
}
