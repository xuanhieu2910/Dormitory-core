package teamit.hust.ktxcdshustbe.utility;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AbsPagingResponse {

    protected  Long count;
    protected  Long pageNumber;
    protected  Long pageSize;
    protected  Long pageOffSet;
    protected  Long pageTotal;

}
