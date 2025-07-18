package teamit.hust.ktxcdshustbe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Data
@Setter
@Getter
public class ApiPageDto<T> {

    private T results;

    @JsonProperty("total_elements")
    private long totalElements;

    @JsonProperty("total_pages")
    private int totalPages;

    private int page;

    @JsonProperty("page_size")
    private int pageSize;

    public static ApiPageDto<?> build() {
        return new ApiPageDto<>();
    }

    public static <T> ApiPageDto<?> build(Page<T> page) {
        return new ApiPageDto<>().withData(page.getContent())
                .withPage(page.getPageable().getPageNumber() + 1)
                .withTotalPages(page.getTotalPages())
                .withPageSize(page.getSize())
                .withTotalElement(page.getTotalElements());
    }

    public ApiPageDto<?> withData(T object) {
        this.results = object;
        return this;
    }

    public ApiPageDto<?> withPage(int page) {
        this.page = page;
        return this;
    }

    public ApiPageDto<?> withTotalElement(long totalElement) {
        this.totalElements = totalElement;
        return this;
    }

    public ApiPageDto<?> withTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public ApiPageDto<?> withPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}

