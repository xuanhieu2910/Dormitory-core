package teamit.hust.ktxcdshustbe.request.timeHired;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CreateTimeHiredRequest {
    @NonNull
    private String timeStart;
    @NonNull
    private String timeEnd;
    private Integer status;
}
