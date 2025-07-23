package teamit.hust.ktxcdshustbe.service.yearGroup.impl;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.dto.yearGroup.FindAllYearGroupsDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.YearGroup;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.yearGroup.YearGroupRepository;
import teamit.hust.ktxcdshustbe.request.yearGroup.CreateYearGroupRequest;
import teamit.hust.ktxcdshustbe.request.yearGroup.FindAllYearGroupsRequest;
import teamit.hust.ktxcdshustbe.request.yearGroup.UpdateYearGroupRequest;
import teamit.hust.ktxcdshustbe.response.yearGroup.FindAllYearGroupsResponse;
import teamit.hust.ktxcdshustbe.response.yearGroup.YearGroupDetailResponse;
import teamit.hust.ktxcdshustbe.service.yearGroup.YearGroupService;
import teamit.hust.ktxcdshustbe.utility.PageUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Log4j2
@Service
public class YearGroupServiceImpl implements YearGroupService {

    @Autowired
    private YearGroupRepository yearGroupRepository;

    @Override
    public Page<FindAllYearGroupsResponse> findAllYearGroup(FindAllYearGroupsRequest request) {

        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllYearGroupsDto> yearGroupDtosPage = yearGroupRepository.findAllYearGroups(request, pageable);

        List<FindAllYearGroupsResponse> responses = convertToFindAllYearGroupsResponse(yearGroupDtosPage.getContent());

        return new PageImpl<>(responses, pageable, yearGroupDtosPage.getTotalElements());
    }

    private List<FindAllYearGroupsResponse> convertToFindAllYearGroupsResponse(List<FindAllYearGroupsDto> dtos) {
        List<FindAllYearGroupsResponse> responses = new ArrayList<>();
        for (FindAllYearGroupsDto dto : dtos) {
            FindAllYearGroupsResponse response = new FindAllYearGroupsResponse();
            response.setCodeYearGroup(dto.getCodeYearGroup());
            response.setTitle(dto.getTitle());
            response.setDescription(dto.getDescription());
            response.setTimeCreated(dto.getTimeCreated());
            response.setTimeModified(dto.getTimeModified());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public YearGroupDetailResponse findYearGroupDetailsByCode(String code) {
        if (StringUtils.isBlank(code)) {
            throw new ValidParametersException("Code is required");
        }

        FindAllYearGroupsDto dto = yearGroupRepository.findYearGroupDetailsByCode(code)
                .orElseThrow(() -> new NotFoundException("Year group not found with code: " + code));

        return convertToDetailResponse(dto);
    }

    private YearGroupDetailResponse convertToDetailResponse(FindAllYearGroupsDto dto) {
        YearGroupDetailResponse response = new YearGroupDetailResponse();
        response.setCodeYearGroup(dto.getCodeYearGroup());
        response.setTitle(dto.getTitle());
        response.setDescription(dto.getDescription());
        response.setTimeCreated(dto.getTimeCreated());
        response.setTimeModified(dto.getTimeModified());
        response.setUserNameCreated(dto.getUserNameCreated());
        response.setFullNameCreated(dto.getFullNameCreated());
        response.setUserNameModified(dto.getUserNameModified());
        response.setFullNameModified(dto.getFullNameModified());
        return response;
    }

    @Override
    public void createYearGroup(CreateYearGroupRequest request) {
        if (StringUtils.isBlank(request.getTitle())) {
            throw new ValidParametersException("Title is required and cannot be blank.");
        }

        yearGroupRepository.findByTitle(request.getTitle()).ifPresent(yg -> {
            throw new ExitsObjectException("A year group with title '" + request.getTitle() + "' already exists.");
        });

        Integer currentUserId = 1;
        try {
            KtxUser currentUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            currentUserId = currentUser.getIdKtxUser();
        } catch (Exception e) {
            log.warn("Cannot get current user from SecurityContext. Using default user ID.");
        }


        YearGroup yearGroup = new YearGroup();
        long currentTime = new Date().getTime();

        yearGroup.setCodeYearGroup(UUID.randomUUID().toString());
        yearGroup.setTitle(request.getTitle());
        yearGroup.setDescription(request.getDescription());
        yearGroup.setTimeCreated(currentTime);
        yearGroup.setTimeModified(currentTime);
        yearGroup.setIdUserCreated(currentUserId);
        yearGroup.setIdUserModified(currentUserId);

        yearGroupRepository.save(yearGroup);
    }

    @Override
    public void updateYearGroup(UpdateYearGroupRequest request) {
        if (StringUtils.isBlank(request.getCodeYearGroup()) || StringUtils.isBlank(request.getTitle())) {
            throw new ValidParametersException("codeYearGroup and title are required.");
        }

        YearGroup existingYearGroup = yearGroupRepository.findByCodeYearGroup(request.getCodeYearGroup())
                .orElseThrow(() -> new NotFoundException("Year group not found with code: " + request.getCodeYearGroup()));

        if (!existingYearGroup.getTitle().equals(request.getTitle())) {
            yearGroupRepository.findByTitle(request.getTitle()).ifPresent(yg -> {
                throw new ExitsObjectException("Another year group with title '" + request.getTitle() + "' already exists.");
            });
        }

        Integer currentUserId = 1;
        try {
            KtxUser currentUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            currentUserId = currentUser.getIdKtxUser();
        } catch (Exception e) {
            log.warn("Cannot get current user from SecurityContext. Using default user ID: {}.", currentUserId);
        }

        existingYearGroup.setTitle(request.getTitle());
        existingYearGroup.setDescription(request.getDescription());
        existingYearGroup.setTimeModified(new Date().getTime());
        existingYearGroup.setIdUserModified(currentUserId);

        yearGroupRepository.save(existingYearGroup);
    }

    @Override
    public void deleteYearGroup(String code) {
        if (StringUtils.isBlank(code)) {
            throw new ValidParametersException("Code is required to delete a year group.");
        }

        YearGroup yearGroupToDelete = yearGroupRepository.findByCodeYearGroup(code)
                .orElseThrow(() -> new NotFoundException("Cannot delete. Year group not found with code: " + code));

        yearGroupRepository.delete(yearGroupToDelete);
    }
}