package teamit.hust.ktxcdshustbe.utility;


import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
@NoArgsConstructor
public class AbsPagingResponse {

    protected  Long count;
    protected  Long pageNumber;
    protected  Long pageSize;
    protected  Long pageOffSet;
    protected  Long pageTotal;

    public static void main(String[] args) {
        String merchantId = "d6d08860-4bee-41da-a8c5-2ab2a945c17c";
        String terminalId = "57ce473f-8cbd-4b8c-b2c2-f626d9f27114";
        String orderId = "1155";
        String secretKey = "48E722B255286C8B22FC2C6CAF02AF776379460F06E446BDBB4468C994236802";
        String value = merchantId + terminalId + orderId + secretKey;
        String signature = Hashing.sha256()
                .hashString(value, StandardCharsets.UTF_8)
                .toString();
        System.out.println(signature);
    }

}
