package teamit.hust.ktxcdshustbe.service.priorityGroup.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.entity.PriorityGroup;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.repository.priorityGroup.PriorityGroupRepository;
import teamit.hust.ktxcdshustbe.service.priorityGroup.PriorityGroupService;

import java.util.Optional;

@Service
public class PriorityGroupServiceImpl implements PriorityGroupService {
    @Autowired
    PriorityGroupRepository priorityGroupRepository;
    @Override
    public PriorityGroup findPriorGroupByTitle(String titlePriorityGroup) {
        Optional<PriorityGroup> priorityGroupOptional = priorityGroupRepository.findPriorityGroupByTitle(titlePriorityGroup);
        if (priorityGroupOptional.isEmpty()) {
            throw new NotFoundException();
        }
        return priorityGroupOptional.get();
    }

    @Override
    public PriorityGroup findPriorGroupByIdPriorGroup(Integer idPriorityGroup) {
        Optional<PriorityGroup> priorityGroupOptional = priorityGroupRepository.findPriorityGroupByIdPriorityGroup(idPriorityGroup);
        if (priorityGroupOptional.isEmpty()) {
            throw new NotFoundException();
        }
        return priorityGroupOptional.get();
    }
}
