package teamit.hust.ktxcdshustbe.request.registerRoom;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeclareInformationRequest {

    @NotNull
    @JsonProperty("nameFather")
    private String nameFather;
    @NotNull
    @JsonProperty("yearFather")
    private Integer yearFather;
    @NotNull
    @JsonProperty("phoneNumberFather")
    private String phoneNumberFather;
    @NotNull
    @JsonProperty("addressFather")
    private String addressFather;
    @NotNull
    @JsonProperty("nameMother")
    private String nameMother;
    @NotNull
    @JsonProperty("yearMother")
    private Integer yearMother;
    @NotNull
    @JsonProperty("phoneNumberMother")
    private String phoneNumberMother;
    @NotNull
    @JsonProperty("addressMother")
    private String addressMother;
    @NotNull
    @JsonProperty("addressContact")
    private String addressContact;
    @JsonProperty("nation")
    private String nation;
    @JsonProperty("address")
    private String address;

}
