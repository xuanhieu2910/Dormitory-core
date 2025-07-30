package teamit.hust.ktxcdshustbe.service.priorityGroup.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.dto.priorityGroup.FindAllPriorityGroupDto;
import teamit.hust.ktxcdshustbe.dto.priorityGroup.FindPriorityGroupDetailDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.PriorityGroup;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.exception.IsBlankException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.repository.batchesRegistrationSchedule.BatchesRegistrationScheduleRepository;
import teamit.hust.ktxcdshustbe.repository.priorityGroup.PriorityGroupRepository;
import teamit.hust.ktxcdshustbe.repository.user.KtxUserRepository;
import teamit.hust.ktxcdshustbe.request.priorityGroup.CreatePriorityGroupRequest;
import teamit.hust.ktxcdshustbe.request.priorityGroup.FindAllPriorityGroupRequest;
import teamit.hust.ktxcdshustbe.request.priorityGroup.UpdatePriorityGroupRequest;
import teamit.hust.ktxcdshustbe.response.priorityGroup.FindAllPriorityGroupResponse;
import teamit.hust.ktxcdshustbe.response.priorityGroup.PriorityGroupDetailResponse;
import teamit.hust.ktxcdshustbe.service.priorityGroup.PriorityGroupService;
import teamit.hust.ktxcdshustbe.utility.PageUtils;

import java.util.*;


@Service
public class PriorityGroupServiceImpl implements PriorityGroupService {

    @Autowired
    PriorityGroupRepository priorityGroupRepository;
    @Autowired
    private BatchesRegistrationScheduleRepository batchesRegistrationScheduleRepository;
    @Autowired
    private KtxUserRepository ktxUserRepository;

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
    @Override
    public Page<FindAllPriorityGroupResponse> findAllPriorityGroup(FindAllPriorityGroupRequest request){
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllPriorityGroupDto> priorityGroupDtos = priorityGroupRepository.findAllPriorityGroup(request, pageable);
        return new PageImpl<>(convertToFindALlPriorityGroupResponse(priorityGroupDtos.getContent()), pageable, priorityGroupDtos.getTotalElements());
    }

    private List<FindAllPriorityGroupResponse> convertToFindALlPriorityGroupResponse(List<FindAllPriorityGroupDto> priorityGroupDtos){
        List<FindAllPriorityGroupResponse> responses = new ArrayList<>();
        for(FindAllPriorityGroupDto priorityGroupDto : priorityGroupDtos){
            FindAllPriorityGroupResponse response = new FindAllPriorityGroupResponse();
            response.setTitlePriorityGroup(priorityGroupDto.getTitlePriorityGroup());
            response.setPriorityGroupCode(priorityGroupDto.getPriorityGroupCode());
            response.setDescription(priorityGroupDto.getDescription());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public PriorityGroupDetailResponse getPriorityGroupDetail(String priorityGroupCode){
        if(StringUtils.isBlank(priorityGroupCode)){
            throw new IsBlankException();
        }
        FindPriorityGroupDetailDto dto = priorityGroupRepository.getPriorityGroupDetailByPriorityGroupCode(priorityGroupCode);
        return convertToPriorityGroupDetailResponse(dto);

    }

    @Override
    public List<PriorityGroup> findPriorityGroupsByListPriorityGroupCode(List<String> codesPriorityGroup) {
        Optional<List<PriorityGroup>> priorityGroups = priorityGroupRepository.findPriorityGroupByListPriorityGroupCode(codesPriorityGroup);
        if (priorityGroups.isEmpty() || priorityGroups.get().size() != codesPriorityGroup.size()){
            throw new NotFoundException();
        }
        return priorityGroups.get();
    }

    @Override
    public List<PriorityGroup> findAllPriorityGroupByIdsPriorGroup(List<Integer> idsPriorityGroup) {
        Optional<List<PriorityGroup>> priorityGroups = priorityGroupRepository.findPriorityGroupByIdsPriorityGroup(idsPriorityGroup);
        if (priorityGroups.isEmpty() || priorityGroups.get().size() != idsPriorityGroup.size()){
            throw new NotFoundException();
        }
        return priorityGroups.get();
    }

    private PriorityGroupDetailResponse convertToPriorityGroupDetailResponse(FindPriorityGroupDetailDto dto) {
        PriorityGroupDetailResponse response = new PriorityGroupDetailResponse();
        response.setTitlePriortityGroup(dto.getTitlePriorityGroup());
        response.setPriorityGroupCode(dto.getPriorityGroupCode());
        response.setDescription(dto.getDescription());
        return response;
    }

    @Override
    public PriorityGroup create(CreatePriorityGroupRequest request){
        verifyCreatePriorityGroupRequest(request);
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PriorityGroup priorityGroup = initializePriorityGroup(request, ktxUser);
        return priorityGroupRepository.save(priorityGroup);
    }
    private void verifyCreatePriorityGroupRequest(CreatePriorityGroupRequest request){
        if(StringUtils.isBlank(request.getTitlePriorityGroup())) {
            throw new IsBlankException();
        }
        if(priorityGroupRepository.existsByTitlePriorityGroup(request.getTitlePriorityGroup())) {
            throw new ExitsObjectException();
        }
//        if(priorityGroupRepository.existsByPriorityGroupCode(request.getPriorityGroupCode())){
//            throw new ExitsObjectException();
//        }
    }
    private PriorityGroup initializePriorityGroup(CreatePriorityGroupRequest request, KtxUser ktxUser){
        PriorityGroup priorityGroup = new PriorityGroup();
        priorityGroup.setPriorityGroupCode(UUID.nameUUIDFromBytes(request.getTitlePriorityGroup().getBytes()).toString());
        priorityGroup.setTitle(request.getTitlePriorityGroup());
        priorityGroup.setDescription(request.getDescription());
        priorityGroup.setIdUserCreated(ktxUser.getIdKtxUser());
        priorityGroup.setIdUserModified(ktxUser.getIdKtxUser());
        priorityGroup.setTimeCreated(new Date().getTime());
        priorityGroup.setTimeModified(new Date().getTime());
        return priorityGroup;
    }

    @Override
    public PriorityGroup update(UpdatePriorityGroupRequest request){
        verifyUpdatePriorityGroupRequest(request);
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PriorityGroup priorityGroup = updatePriorityGroup(request, ktxUser);
        return priorityGroupRepository.save(priorityGroup);
    }
    private void verifyUpdatePriorityGroupRequest(UpdatePriorityGroupRequest request){
        if(StringUtils.isBlank(request.getTitlePriorityGroup())) {
            throw new IsBlankException();
        }
        Optional<PriorityGroup> optionalPriorityGroup = priorityGroupRepository.findByPriorityGroupCode(request.getPriorityGroupCode());
        if(optionalPriorityGroup.isEmpty()) {
            throw new NotFoundException();
        }
    }
    private PriorityGroup updatePriorityGroup(UpdatePriorityGroupRequest request, KtxUser ktxUser){
        Optional<PriorityGroup> optionalPriorityGroup = priorityGroupRepository.findByPriorityGroupCode(request.getPriorityGroupCode());
        PriorityGroup priorityGroup = optionalPriorityGroup.get();
        priorityGroup.setTitle(request.getTitlePriorityGroup());
        priorityGroup.setDescription(request.getDescription());
        priorityGroup.setTimeModified(new Date().getTime());
        priorityGroup.setIdUserModified(ktxUser.getIdKtxUser());
        return priorityGroup;
    }

    @Override
    public void delete(String priorityGroupCode) {
        Optional<PriorityGroup> optionalPriorityGroup = priorityGroupRepository.findByPriorityGroupCode(priorityGroupCode);
        if(optionalPriorityGroup.isEmpty()) {
            throw new NotFoundException();
        }
        priorityGroupRepository.delete(optionalPriorityGroup.get());
    }

}
