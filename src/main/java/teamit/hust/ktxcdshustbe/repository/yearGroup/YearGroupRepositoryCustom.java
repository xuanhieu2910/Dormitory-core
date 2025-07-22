package teamit.hust.ktxcdshustbe.repository.yearGroup;

import teamit.hust.ktxcdshustbe.entity.YearGroup;

import java.util.Optional;

public interface YearGroupRepositoryCustom {
    Optional<YearGroup> findYearGroupByTitle(String titleYearGroup);

    Optional<YearGroup> findYearGroupByIdYearGroup(Integer idYearGroup);
}
