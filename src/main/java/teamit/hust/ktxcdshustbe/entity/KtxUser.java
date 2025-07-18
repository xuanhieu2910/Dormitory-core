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
    @Column(name = "number_student")
    private String numberStudent;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "cccd")
    private String cccd;
    @Column(name = "date_of_birth")
    private String dateOfBirth;
    @Column(name = "sex")
    private Integer sex;
    @Column(name = "nation")
    private String nation;
    @Column(name = "religion")
    private String religion;
    @Column(name = "area")
    private String area;
    @Column(name = "path_avatar")
    private String pathAvatar;
    @Column(name = "district")
    private String district;
    @Column(name = "province")
    private String province;
    @Column(name = "wards")
    private String wards;
    @Column(name = "address_contact")
    private String addressContact;
    @Column(name = "school")
    private String school;
    @Column(name = "faculty")
    private String faculty;
    @Column(name = "year_grade")
    private Integer yearGrade;
    @Column(name = "class_user")
    private String classUser;
    @Column(name = "email_contact")
    private String emailContact;
    @Column(name = "address")
    private String address;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "time_created")
    private Long timeCreated;
    @Column(name = "time_modified")
    private Long timeModified;
    @Column(name = "is_actived")
    private Integer isActived;
    @Column(name = "status_register_room")
    private Integer statusRegisterRoom;
    @Column(name = "code_major")
    private String codeMajor;
    @Column(name = "title_major")
    private String titleMajor;
    @Column(name = "type_login")
    private String typeLogin;
    @Column(name = "name_father")
    private String nameFather;
    @Column(name = "year_father")
    private Integer yearFather;
    @Column(name = "phone_number_father")
    private String phoneNumberFather;
    @Column(name = "address_father")
    private String addressFather;
    @Column(name = "name_mother")
    private String nameMother;
    @Column(name = "year_mother")
    private Integer yearMother;
    @Column(name = "phone_number_mother")
    private String phoneNumberMother;
    @Column(name = "address_mother")
    private String addressMother;
    @Column(name = "code_user")
    private String codeUser;

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
