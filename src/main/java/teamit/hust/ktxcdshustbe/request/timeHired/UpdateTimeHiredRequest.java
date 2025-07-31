package teamit.hust.ktxcdshustbe.request.timeHired;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTimeHiredRequest {
    @NonNull
    private String codeTimeHired;
    private Long timeStart;
    private Long timeEnd;
    private Integer status;
}
