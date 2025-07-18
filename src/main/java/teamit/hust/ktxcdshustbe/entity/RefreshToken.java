package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;


@Getter
@Setter
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_refresh_token")
    private Integer idRefreshToken;
    @Column(name = "id_user")
    private Integer idUser;
    @Column(name = "token")
    private String token;
    @Column(name = "expiry_date",nullable = false)
    private Timestamp expiryDate;
    @Column(name = "revoked")
    private boolean revoked;

}
