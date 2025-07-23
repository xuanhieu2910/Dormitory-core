package teamit.hust.ktxcdshustbe.repository.yearGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.yearGroup.FindAllYearGroupsDto;
import teamit.hust.ktxcdshustbe.entity.YearGroup;
import teamit.hust.ktxcdshustbe.request.yearGroup.FindAllYearGroupsRequest;

import java.util.Optional;

public interface YearGroupRepositoryCustom {
    Page<FindAllYearGroupsDto> findAllYearGroups(FindAllYearGroupsRequest request, Pageable pageable);

    Optional<FindAllYearGroupsDto> findYearGroupDetailsByCode(String code);
    Optional<YearGroup> findByTitle(String title);

    Optional<YearGroup> findByCodeYearGroup(String code);
}
