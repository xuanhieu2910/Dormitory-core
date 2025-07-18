package teamit.hust.ktxcdshustbe.dto.serviceRoom;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceRoomDto {

    private String codeRoom;
    private String codeService;
    private String title;
    private String description;
    private Integer status;
    private Integer idServiceRoom;
    private Integer idService;
    private Integer idRoom;
    private Long timeCreated;
    private Long timeModified;
    private Integer idUserCreated;
    private Integer idUserModified;


}
