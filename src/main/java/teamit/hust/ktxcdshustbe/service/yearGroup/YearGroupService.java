package teamit.hust.ktxcdshustbe.service.yearGroup;

import teamit.hust.ktxcdshustbe.entity.YearGroup;

public interface YearGroupService {
    YearGroup findYearGroupByTitle(String titleYearGroup);

    YearGroup findYearGroupByIdYearGroup(Integer idYearGroup);
}
