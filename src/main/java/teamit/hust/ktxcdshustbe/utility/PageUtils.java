package teamit.hust.ktxcdshustbe.utility;

import jakarta.persistence.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;


public class PageUtils {


    public static Pageable buildPage(Integer pageNo, Integer pageSize) {
        return buildPageRequest(pageNo, pageSize);
    }

    /**
     * Build page
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    private static Pageable buildPageRequest(Integer pageNo, Integer pageSize) {
        return PageRequest.of(pageNo, pageSize);
    }

    /**
     * Create response infor
     *
     * @param response
     * @return
     */
    public static HttpHeaders returnHttpHeaders(AbsPagingResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(PagingHeaders.COUNT.getName(), String.valueOf(response.getCount()));
        headers.set(PagingHeaders.PAGE_SIZE.getName(), String.valueOf(response.getPageSize()));
        headers.set(PagingHeaders.PAGE_OFFSET.getName(), String.valueOf(response.getPageOffSet()));
        headers.set(PagingHeaders.PAGE_NUMBER.getName(), String.valueOf(response.getPageNumber()));
        headers.set(PagingHeaders.PAGE_TOTAL.getName(), String.valueOf(response.getPageTotal()));
        return headers;
    }

    public static Query buildQuery(Pageable pageable, Query query) {
        if (pageable.getPageSize() > 0) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }

        return query;
    }
}

