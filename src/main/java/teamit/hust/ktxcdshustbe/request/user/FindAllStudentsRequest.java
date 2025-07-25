package teamit.hust.ktxcdshustbe.request.user;

import com.azure.core.annotation.Get;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
@NoArgsConstructor
public class FindAllStudentsRequest extends RequestPageBase {

    private boolean statusHired = false;

}
