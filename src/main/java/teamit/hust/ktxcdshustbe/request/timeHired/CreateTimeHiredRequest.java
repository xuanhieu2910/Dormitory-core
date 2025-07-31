package teamit.hust.ktxcdshustbe.request.timeHired;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CreateTimeHiredRequest {
    @NonNull
    private Long timeStart;
    @NonNull
    private Long timeEnd;
    private Integer status;
}
