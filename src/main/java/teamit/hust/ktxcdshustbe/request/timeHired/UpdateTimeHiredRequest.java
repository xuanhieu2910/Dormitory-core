package teamit.hust.ktxcdshustbe.request.timeHired;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTimeHiredRequest {
    @NonNull
    private String codeTimeHired;
    private String timeStart;
    private String timeEnd;
    private Integer status;
}
