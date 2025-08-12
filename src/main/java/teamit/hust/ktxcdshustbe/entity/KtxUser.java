package teamit.hust.ktxcdshustbe.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import teamit.hust.ktxcdshustbe.utility.Constants;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "ktx_user")
public class KtxUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ktx_user")
    private Integer idKtxUser;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "sex")
    private Integer sex;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "is_actived")
    private Integer isActived;
    @Column(name = "type_login")
    private String typeLogin;
    @Column(name = "code_user")
    private String codeUser;
    @Column(name = "id_user_created")
    private Integer idUserCreated;
    @Column(name = "id_user_modified")
    private Integer idUserModified;
    @Column(name = "id_year_group")
    private Integer idYearGroup;
    @Column(name = "id_priority_group")
    private Integer idPriorityGroup;
    @Column(name = "value")
    private String value;
    @Column(name = "is_initialize")
    private Integer isInitialize;


    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name="id_role"))
    private Collection<Role> role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities(getPrivileges(this.role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (this.getIsActived().equals(Constants.ACCOUNT_IS_ACTIVED_LOCK)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Capabilities> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add("ROLE_" + role.getTitle());
            collection.addAll(role.getCapabilities());
        }
        for (Capabilities item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }


}
