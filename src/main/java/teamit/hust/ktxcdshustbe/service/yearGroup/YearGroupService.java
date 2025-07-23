package teamit.hust.ktxcdshustbe.service.yearGroup;

import teamit.hust.ktxcdshustbe.entity.YearGroup;

import java.util.List;

public interface YearGroupService {
    YearGroup findYearGroupByTitle(String titleYearGroup);

    YearGroup findYearGroupByIdYearGroup(Integer idYearGroup);

    List<YearGroup> findYearGroupsByListCode(List<String> codes);

    List<YearGroup> findYearGroupsByIds(List<Integer> idsYearGroup);
}
