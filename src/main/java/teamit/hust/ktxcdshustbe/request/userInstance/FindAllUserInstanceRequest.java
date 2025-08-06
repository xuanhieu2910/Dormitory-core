package teamit.hust.ktxcdshustbe.request.userInstance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindAllUserInstanceRequest extends RequestPageBase {
    @JsonProperty("is_error")
    private Integer isError;
}
