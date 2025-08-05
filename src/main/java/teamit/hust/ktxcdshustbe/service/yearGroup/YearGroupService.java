package teamit.hust.ktxcdshustbe.service.yearGroup;

import org.springframework.data.domain.Page;
import teamit.hust.ktxcdshustbe.entity.YearGroup;
import teamit.hust.ktxcdshustbe.request.yearGroup.CreateYearGroupRequest;
import teamit.hust.ktxcdshustbe.request.yearGroup.FindAllYearGroupsRequest;
import teamit.hust.ktxcdshustbe.request.yearGroup.UpdateYearGroupRequest;
import teamit.hust.ktxcdshustbe.response.yearGroup.FindAllYearGroupsResponse;
import teamit.hust.ktxcdshustbe.response.yearGroup.YearGroupDetailResponse;

import java.util.List;

public interface YearGroupService {

    YearGroup findYearGroupByTitle(String titleYearGroup);

    YearGroup findYearGroupByIdYearGroup(Integer idYearGroup);

    List<YearGroup> findYearGroupsByListCode(List<String> codes);

    List<YearGroup> findYearGroupsByIds(List<Integer> idsYearGroup);

    Page<FindAllYearGroupsResponse> findAllYearGroup(FindAllYearGroupsRequest request);

    YearGroupDetailResponse findYearGroupDetailsByCode(String code);

    void createYearGroup(CreateYearGroupRequest request);

    void updateYearGroup(UpdateYearGroupRequest request);

    void deleteYearGroup(String code);

    List<YearGroup> getAllTYearGroup();
}
