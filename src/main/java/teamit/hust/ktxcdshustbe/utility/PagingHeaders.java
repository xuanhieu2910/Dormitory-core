package teamit.hust.ktxcdshustbe.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PagingHeaders {

    PAGE_SIZE("Size"),
    PAGE_NUMBER("Number"),
    PAGE_OFFSET("Offset"),
    PAGE_TOTAL("Total"),
    COUNT("Count");

    private final String name;
}
