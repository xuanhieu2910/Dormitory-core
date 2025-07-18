package teamit.hust.ktxcdshustbe.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPageBase {
    private int page;
    private int size;
    private String keyword;
    private String sortBy;
    private String sortOrder;
}
