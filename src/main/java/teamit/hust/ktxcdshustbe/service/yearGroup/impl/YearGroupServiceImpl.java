package teamit.hust.ktxcdshustbe.service.yearGroup.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.entity.YearGroup;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.repository.yearGroup.YearGroupRepository;
import teamit.hust.ktxcdshustbe.service.yearGroup.YearGroupService;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class YearGroupServiceImpl implements YearGroupService {
    @Autowired
    YearGroupRepository yearGroupRepository;
    @Override
    public YearGroup findYearGroupByTitle(String titleYearGroup) {
        Optional<YearGroup> yearGroupOptional = yearGroupRepository.findYearGroupByTitle(titleYearGroup);
        if (yearGroupOptional.isEmpty()) {
            throw new NotFoundException();
        }
        return yearGroupOptional.get();
    }
}
