package teamit.hust.ktxcdshustbe.service.batchesRegistration.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.batchesRegistration.BatchesRegistrationDetailDto;
import teamit.hust.ktxcdshustbe.dto.batchesRegistration.FindAllBatchesRegistrationDto;
import teamit.hust.ktxcdshustbe.dto.batchesRegistration.FindAllDepartmentBatchesRegistrationDto;
import teamit.hust.ktxcdshustbe.dto.batchesRegistrationSchedule.BatchesRegistrationScheduleDto;
import teamit.hust.ktxcdshustbe.dto.batchesYearGroupRegistration.BatchesYearGroupRegistrationDto;
import teamit.hust.ktxcdshustbe.entity.*;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.batchesRegistration.BatchesRegistrationRepository;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.CreateBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.FindAllBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.FindAllDepartmentInBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.UpdateBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistrationRoom.CreateBatchesRegistrationRoomRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistrationSchedule.CreateBatchesRegistrationScheduleRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistrationSchedule.UpdateBatchesRegistrationScheduleRequest;
import teamit.hust.ktxcdshustbe.response.batchesRegistration.BatchesRegistrationDetailResponse;
import teamit.hust.ktxcdshustbe.response.batchesRegistration.FindAllBatchesRegistrationResponse;
import teamit.hust.ktxcdshustbe.response.batchesRegistration.FindAllDepartmentBatchesRegistrationResponse;
import teamit.hust.ktxcdshustbe.response.batchesRegistrationSchedule.BatchesRegistrationScheduleDetailResponse;
import teamit.hust.ktxcdshustbe.response.batchesYearGroupRegistration.BatchesYearGroupRegistrationResponse;
import teamit.hust.ktxcdshustbe.response.priorityGroup.PriorityGroupDetailResponse;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredDetailsResponse;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredResponse;
import teamit.hust.ktxcdshustbe.service.batchesRegistration.BatchesRegistrationService;
import teamit.hust.ktxcdshustbe.service.batchesRegistrationRoom.BatchesRegistrationRoomService;
import teamit.hust.ktxcdshustbe.service.batchesRegistrationSchedule.BatchesRegistrationScheduleService;
import teamit.hust.ktxcdshustbe.service.batchesYearGroupRegistration.BatchesYearGroupRegistrationService;
import teamit.hust.ktxcdshustbe.service.priorityGroup.PriorityGroupService;
import teamit.hust.ktxcdshustbe.service.room.RoomService;
import teamit.hust.ktxcdshustbe.service.semester.SemesterService;
import teamit.hust.ktxcdshustbe.service.timeHired.TimeHiredService;
import teamit.hust.ktxcdshustbe.service.yearGroup.YearGroupService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.*;

@Service
public class BatchesRegistrationServiceImpl implements BatchesRegistrationService {

    @Autowired
    BatchesRegistrationRepository batchesRegistrationRepository;
    @Autowired
    TimeHiredService timeHiredService;
    @Autowired
    SemesterService semesterService;
    @Autowired
    BatchesYearGroupRegistrationService batchesYearGroupRegistrationService;
    @Autowired
    YearGroupService yearGroupService;
    @Autowired
    BatchesRegistrationScheduleService batchesRegistrationScheduleService;
    @Autowired
    PriorityGroupService priorityGroupService;
    @Autowired
    BatchesRegistrationRoomService batchesRegistrationRoomService;
    @Autowired
    RoomService roomService;

    @Override
    public Page<FindAllBatchesRegistrationResponse> findAll(FindAllBatchesRegistrationRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllBatchesRegistrationDto> batchesRegistrationDtos = batchesRegistrationRepository.findAllBatchesRegistration(request,pageable);
        return new PageImpl<>(convertToFindAllBatchesRegistration(batchesRegistrationDtos.getContent()), pageable, batchesRegistrationDtos.getTotalElements());
    }

    @Override
    public BatchesRegistrationDetailResponse getDetailBatchesRegistration(String codeBatchesRegistration) {
        verifyGetDetailBatchesRegistration(codeBatchesRegistration);
        Optional<BatchesRegistrationDetailDto> registrationDetailDto = batchesRegistrationRepository.getDetailBatchesRegistration(codeBatchesRegistration);
        if (registrationDetailDto.isEmpty()){
            throw new NotFoundException();
        }
        return convertToGetDetailBatchesRegistration(registrationDetailDto.get());
    }

    @Transactional
    @Override
    public void createBatchesRegistration(CreateBatchesRegistrationRequest request) {
        verifyCreateBatchesRegistration(request);
        initializeBatchesRegistration(request);
    }

    @Transactional
    @Override
    public void updateBatchesRegistration(UpdateBatchesRegistrationRequest request) {
        verifyUpdateBatchesRegistration(request);
        updateFieldBatchesRegistration(request);
    }

    @Override
    public BatchesRegistration getBatchesRegistrationCurrentByIdYearGroupAndIdPriorityGroup(Long timeCurrent,
                                                                                            Integer idYearGroup,
                                                                                            Integer idPriorityGroup) {
        Optional<BatchesRegistration> batchesRegistration =
                batchesRegistrationRepository.getBatchesRegistrationCurrentByIdYearGroupAndIdPriorityGroup(timeCurrent,
                        idPriorityGroup, idYearGroup);
        if (batchesRegistration.isEmpty()){
            throw new NotFoundException();
        }
         return batchesRegistration.get();
    }

    @Override
    public Page<FindAllDepartmentBatchesRegistrationResponse>
    findAllDepartmentBatchesRegistration(FindAllDepartmentInBatchesRegistrationRequest request) {
        verifyFindAllDepartmentBatchesRegistration(request);
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllDepartmentBatchesRegistrationDto> dtos = batchesRegistrationRepository.findAllDepartmentBatchesRegistration(request, pageable);
        return new PageImpl<>(convertToFindAllDepartmentBatchesRegistrationResponse(dtos.getContent()), pageable, dtos.getTotalElements());
    }

    private List<FindAllDepartmentBatchesRegistrationResponse>
    convertToFindAllDepartmentBatchesRegistrationResponse(List<FindAllDepartmentBatchesRegistrationDto> content) {
        List<FindAllDepartmentBatchesRegistrationResponse> responses = new ArrayList<>();
        for (FindAllDepartmentBatchesRegistrationDto dto : content){
            FindAllDepartmentBatchesRegistrationResponse response = new FindAllDepartmentBatchesRegistrationResponse();
            response.setTitleDepartment(dto.getTitleDepartment());
            response.setCodeDepartment(dto.getCodeDepartment());
            response.setCodeBatchesRegistration(dto.getCodeBatchesRegistration());
            responses.add(response);
        }
        return responses;
    }

    private void verifyFindAllDepartmentBatchesRegistration(FindAllDepartmentInBatchesRegistrationRequest request) {
        if (StringUtils.isBlank(request.getCodeBatchesRegistration())){
            throw new ValidParametersException();
        }
    }

    private void updateFieldBatchesRegistration(UpdateBatchesRegistrationRequest request) {
        Optional<BatchesRegistration> batchesRegistration =
                batchesRegistrationRepository.findBatchesRegistrationByCodeBatchesRegistration(request.getCodeBatchesRegistration());
        verifyTimeUpdateBatchesRegistration(batchesRegistration);
        List<BatchesYearGroupRegistration> yearGroupRegistrationsOrigin =
                batchesYearGroupRegistrationService.findAllBatchesYearGroupByCodeBatchesRegistration(batchesRegistration.get().getCodeBatchesRegistration());
        List<BatchesRegistrationSchedule> batchesRegistrationSchedulesOrigin =
                batchesRegistrationScheduleService.findAllBatchesRegistrationScheduleByCodeBatchesRegistration(batchesRegistration.get().getCodeBatchesRegistration());
        List<BatchesRegistrationRoom>  batchesRegistrationRoomsOrigin =
                batchesRegistrationRoomService.findAllBatchesRegistrationRoomByCodeBatchesRegistration(batchesRegistration.get().getCodeBatchesRegistration());
        isAllowUpdateChangeParameter(batchesRegistration.get(), request,yearGroupRegistrationsOrigin);
        updateBatchesYearGroupRegistration(batchesRegistration.get().getIdBatchesRegistration(),
                                            request.getYearGroups(), yearGroupRegistrationsOrigin);
        updateBatchesRegistrationSchedule(batchesRegistration.get().getIdBatchesRegistration()
                                        ,request.getBatchesRegistrationSchedule(), batchesRegistrationSchedulesOrigin);
        updateBatchesRegistrationRoom(batchesRegistration.get().getIdBatchesRegistration(),
                                        request.getRooms(),
                                        batchesRegistrationRoomsOrigin);
    }

    private void updateBatchesRegistrationRoom(Integer idBatchesRegistration,
                                               List<CreateBatchesRegistrationRoomRequest> roomsRequest,
                                               List<BatchesRegistrationRoom> batchesRegistrationRoomsOrigin) {
        List<Integer> idsRoomOriginal = new ArrayList<>();
        batchesRegistrationRoomsOrigin.forEach(x->idsRoomOriginal.add(x.getIdBatchesRegistrationRoom()));
        List<Room> roomsOriginal = roomService.findAllRoomByListIdsRoom(idsRoomOriginal);
        handleBatchesRegistrationRoomDelete(roomsRequest, roomsOriginal , batchesRegistrationRoomsOrigin);
        handleBatchesRegistrationRoomCurrent(roomsRequest, roomsOriginal , batchesRegistrationRoomsOrigin);
        handleBatchesRegistrationRoomNew(idBatchesRegistration, roomsRequest, roomsOriginal);
    }

    private void handleBatchesRegistrationRoomNew(Integer idBatchesRegistration,
                                                  List<CreateBatchesRegistrationRoomRequest> roomsRequest,
                                                  List<Room> roomsOriginal) {
        List<String> codesRoomNew = new ArrayList<>();
        for (CreateBatchesRegistrationRoomRequest roomRequest : roomsRequest){
            if (roomsOriginal.stream().noneMatch(x->x.getCodeRoom().equals(roomRequest.getCodeRoom()))){
                codesRoomNew.add(roomRequest.getCodeRoom());
            }
        }
        if (!CollectionUtils.isEmpty(codesRoomNew)){
            List<BatchesRegistrationRoom> batchesRegistrationRooms = new ArrayList<>();
            List<Room> rooms = roomService.findAllRoomByListCodeRoom(codesRoomNew);
            Long timeCurrent = new Date().getTime();
            KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            for (String codeRoom : codesRoomNew){
                BatchesRegistrationRoom batchesRegistrationRoom = new BatchesRegistrationRoom();
                batchesRegistrationRoom.setIdBatchesRegistration(idBatchesRegistration);
                batchesRegistrationRoom.setIdRoom(rooms.stream().filter(x->x.getCodeRoom().equals(codeRoom)).findFirst().get().getIdRoom());
                batchesRegistrationRoom.setStatus(Constants.STATUS_BATCHES_REGISTRATION_ROOM_ACTIVE);
                batchesRegistrationRoom.setTimeCreated(timeCurrent);
                batchesRegistrationRoom.setTimeModified(timeCurrent);
                batchesRegistrationRoom.setIdUserCreated(ktxUser.getIdKtxUser());
                batchesRegistrationRoom.setIdUserModified(ktxUser.getIdKtxUser());
                batchesRegistrationRooms.add(batchesRegistrationRoom);
            }
            batchesRegistrationRoomService.saveAll(batchesRegistrationRooms);
        }
    }

    private void handleBatchesRegistrationRoomCurrent(List<CreateBatchesRegistrationRoomRequest> roomsRequest,
                                                      List<Room> roomsOriginal,
                                                      List<BatchesRegistrationRoom> batchesRegistrationRoomsOrigin) {
        boolean isCheckUpdate = false;
        Long timeCurrent = new Date().getTime();
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (Room room : roomsOriginal){
            if (roomsRequest.stream().anyMatch(x->x.getCodeRoom().equals(room.getCodeRoom()))){
                isCheckUpdate = true;
                batchesRegistrationRoomsOrigin.stream().filter(x->x.getIdRoom().equals(room.getIdRoom())).findFirst().ifPresent(x->{
                    x.setIdUserModified(ktxUser.getIdUserModified());
                    x.setTimeModified(timeCurrent);
                });
            }
        }
        if (isCheckUpdate) {
            batchesRegistrationRoomService.saveAll(batchesRegistrationRoomsOrigin);
        }
    }

    private void handleBatchesRegistrationRoomDelete(List<CreateBatchesRegistrationRoomRequest> roomsRequest,
                                                     List<Room> roomsOriginal,
                                                     List<BatchesRegistrationRoom> batchesRegistrationRoomsOrigin) {
        List<BatchesRegistrationRoom> batchesRegistrationRoomsDelete = new ArrayList<>();
        for (Room room : roomsOriginal){
            if (roomsRequest.stream().noneMatch(x->x.getCodeRoom().equals(room.getCodeRoom()))) {
                batchesRegistrationRoomsDelete.add(batchesRegistrationRoomsOrigin.stream()
                        .filter(x->x.getIdRoom().equals(room.getIdRoom()))
                        .findFirst()
                        .get());
            }
        }
        if (!CollectionUtils.isEmpty(batchesRegistrationRoomsDelete)){
            batchesRegistrationRoomService.deleteAll(batchesRegistrationRoomsDelete);
        }
    }

    private void updateBatchesYearGroupRegistration( Integer batchesRegistration,
                                                    List<String> yearGroupRequest,
                                                    List<BatchesYearGroupRegistration> yearGroupRegistrationsOrigin) {
        List<Integer> idsYearGroupOriginal = new ArrayList<>();
        yearGroupRegistrationsOrigin.forEach(x->idsYearGroupOriginal.add(x.getIdYearGroup()));
        List<YearGroup> yearGroups = yearGroupService.findYearGroupsByIds(idsYearGroupOriginal);
        handleBatchesYearGroupRegistrationDelete(yearGroupRequest, yearGroups , yearGroupRegistrationsOrigin);
        handleBatchesYearGroupRegistrationCurrent(yearGroupRequest, yearGroups , yearGroupRegistrationsOrigin);
        handleBatchesYearGroupRegistrationNew(batchesRegistration, yearGroupRequest, yearGroups);

    }

    private void handleBatchesYearGroupRegistrationDelete(List<String> yearGroupRequest,
                                                          List<YearGroup> yearGroups,
                                                          List<BatchesYearGroupRegistration> yearGroupRegistrationsOrigin) {
        List<BatchesYearGroupRegistration> yearGroupRegistrationsDelete = new ArrayList<>();
        for (YearGroup yearGroup : yearGroups){
            if (!yearGroupRequest.contains(yearGroup.getCodeYearGroup())){
                yearGroupRegistrationsDelete.add(yearGroupRegistrationsOrigin.stream()
                        .filter(x->x.getIdYearGroup()
                        .equals(yearGroup.getIdYearGroup()))
                        .findFirst().get());
            }
        }
        if (!CollectionUtils.isEmpty(yearGroupRegistrationsDelete)) {
            batchesYearGroupRegistrationService.deleteAll(yearGroupRegistrationsDelete);
        }
    }

    private void handleBatchesYearGroupRegistrationCurrent(List<String> yearGroupRequest,
                                                           List<YearGroup> yearGroups,
                                                           List<BatchesYearGroupRegistration>
                                                                   yearGroupRegistrationsOrigin) {
        Long timeCurrent = new Date().getTime();
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isCheckUpdate = false;
        for (YearGroup yearGroup : yearGroups){
            if (yearGroupRequest.contains(yearGroup.getCodeYearGroup())){
                isCheckUpdate = true;
                yearGroupRegistrationsOrigin.stream()
                        .filter(x->x.getIdYearGroup().equals(yearGroup.getIdYearGroup()))
                        .findFirst()
                        .ifPresent(x->{
                            x.setTimeModified(timeCurrent);
                            x.setIdUserModified(ktxUser.getIdKtxUser());
                        });
            }
        }
        if (isCheckUpdate) {
            batchesYearGroupRegistrationService.saveAll(yearGroupRegistrationsOrigin);
        }
    }

    private void handleBatchesYearGroupRegistrationNew(Integer idBatchesRegistration,
                                                                  List<String> codeYearGroupRequest,
                                                                  List<YearGroup> yearGroups) {
        List<String> codeYearGroupsNew = new ArrayList<>();

        for (String yearGroup : codeYearGroupRequest){
            if (yearGroups.stream().noneMatch(x->x.getCodeYearGroup().equals(yearGroup))){
                codeYearGroupsNew.add(yearGroup);
            }
        }
        if (!CollectionUtils.isEmpty(codeYearGroupsNew)) {
            List<BatchesYearGroupRegistration> batchesYearGroupRegistrationsNew = new ArrayList<>();
            List<YearGroup> yearGroupsNew = yearGroupService.findYearGroupsByListCode(codeYearGroupsNew);
            Long timeCurrent = new Date().getTime();
            KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            for (String codeYearGroup : codeYearGroupsNew) {
                BatchesYearGroupRegistration batchesYearGroupRegistration = new BatchesYearGroupRegistration();
                batchesYearGroupRegistration.setIdBatchesRegistration(idBatchesRegistration);
                batchesYearGroupRegistration.setIdYearGroup(yearGroupsNew.stream()
                        .filter(x -> x.getCodeYearGroup().equals(codeYearGroup))
                        .findFirst()
                        .get()
                        .getIdYearGroup());
                batchesYearGroupRegistration.setStatus(Constants.STATUS_BATCHES_YEAR_GROUP_REGISTRATION_ACTIVE);
                batchesYearGroupRegistration.setTimeCreated(timeCurrent);
                batchesYearGroupRegistration.setTimeModified(timeCurrent);
                batchesYearGroupRegistration.setIdUserCreated(ktxUser.getIdKtxUser());
                batchesYearGroupRegistration.setIdUserModified(ktxUser.getIdKtxUser());
                batchesYearGroupRegistrationsNew.add(batchesYearGroupRegistration);
            }
            batchesYearGroupRegistrationService.saveAll(batchesYearGroupRegistrationsNew);
        }
    }

    private void updateBatchesRegistrationSchedule(Integer idBatchesRegistration,
                                                   List<UpdateBatchesRegistrationScheduleRequest> batchesRegistrationScheduleRequest,
                                                   List<BatchesRegistrationSchedule> batchesRegistrationSchedulesOrigin) {
        List<Integer> idsPriorityGroup = new ArrayList<>();
        batchesRegistrationSchedulesOrigin.forEach(x->idsPriorityGroup.add(x.getIdPriorityGroup()));
        List<PriorityGroup> priorityGroups = priorityGroupService.findAllPriorityGroupByIdsPriorGroup(idsPriorityGroup);
        handleBatchesRegistrationScheduleDelete(batchesRegistrationScheduleRequest, priorityGroups , batchesRegistrationSchedulesOrigin);
        handleBatchesRegistrationScheduleCurrent(batchesRegistrationScheduleRequest, priorityGroups , batchesRegistrationSchedulesOrigin);
        handleBatchesRegistrationScheduleNew(idBatchesRegistration, batchesRegistrationScheduleRequest, priorityGroups);

    }

    private void handleBatchesRegistrationScheduleNew(Integer idBatchesRegistration,
                                                      List<UpdateBatchesRegistrationScheduleRequest> batchesRegistrationScheduleRequest,
                                                      List<PriorityGroup> priorityGroups) {
        List<UpdateBatchesRegistrationScheduleRequest> scheduleNewRequest = new ArrayList<>();
        for (UpdateBatchesRegistrationScheduleRequest scheduleRequest : batchesRegistrationScheduleRequest){
            if (priorityGroups.stream().noneMatch(x->x.getPriorityGroupCode().equals(scheduleRequest.getPriorityGroupCode()))){
                scheduleNewRequest.add(scheduleRequest);
            }
        }
        if (!CollectionUtils.isEmpty(scheduleNewRequest)){
            List<String> codePriorityGroupsNew = new ArrayList<>();
            scheduleNewRequest.forEach(x->codePriorityGroupsNew.add(x.getPriorityGroupCode()));
            List<PriorityGroup> priorityGroupsNew = priorityGroupService.findPriorityGroupsByListPriorityGroupCode(codePriorityGroupsNew);
            List<BatchesRegistrationSchedule> batchesRegistrationSchedulesNew = new ArrayList<>();
            Long timeCurrent = new Date().getTime();
            KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            for (UpdateBatchesRegistrationScheduleRequest  scheduleRequest : scheduleNewRequest){
                BatchesRegistrationSchedule batchesRegistrationSchedule = new BatchesRegistrationSchedule();
                batchesRegistrationSchedule.setIdBatchesRegistration(idBatchesRegistration);
                batchesRegistrationSchedule.setStatus(Constants.STATUS_BATCHES_REGISTRATION_SCHEDULE_ACTIVE);
                batchesRegistrationSchedule.setTimeCreated(timeCurrent);
                batchesRegistrationSchedule.setTimeModified(timeCurrent);
                batchesRegistrationSchedule.setIdUserCreated(ktxUser.getIdUserCreated());
                batchesRegistrationSchedule.setIdUserModified(ktxUser.getIdUserCreated());
                batchesRegistrationSchedule.setIdPriorityGroup(priorityGroupsNew.stream()
                        .filter(x->x.getPriorityGroupCode()
                                .equals(scheduleRequest.getPriorityGroupCode()))
                                .findFirst()
                                .get().getIdPriorityGroup());
                batchesRegistrationSchedule.setRegistrationStartTime(scheduleRequest.getRegistrationStartTime());
                batchesRegistrationSchedule.setRegistrationEndTime(scheduleRequest.getRegistrationEndTime());
                batchesRegistrationSchedulesNew.add(batchesRegistrationSchedule);
            }
            batchesRegistrationScheduleService.saveAll(batchesRegistrationSchedulesNew);
        }
    }

    private void handleBatchesRegistrationScheduleCurrent(List<UpdateBatchesRegistrationScheduleRequest> batchesRegistrationScheduleRequest,
                                                          List<PriorityGroup> priorityGroups,
                                                          List<BatchesRegistrationSchedule> batchesRegistrationSchedulesOrigin) {
        Long timeCurrent = new Date().getTime();
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isCheckUpdate = false;
        for (PriorityGroup priorityGroup : priorityGroups){
            Optional<UpdateBatchesRegistrationScheduleRequest> scheduleRequest = batchesRegistrationScheduleRequest.stream().
                    filter(x->x.getPriorityGroupCode().equals(priorityGroup.getPriorityGroupCode())).findFirst();
            if (scheduleRequest.isPresent()){
                isCheckUpdate = true;
                batchesRegistrationSchedulesOrigin.stream()
                        .filter(x->x.getIdPriorityGroup().equals(priorityGroup.getIdPriorityGroup()))
                        .findFirst()
                        .ifPresent(x->{
                        x.setTimeModified(timeCurrent);
                        x.setIdUserModified(ktxUser.getIdKtxUser());
                        x.setRegistrationStartTime(scheduleRequest.get().getRegistrationStartTime());
                        x.setRegistrationEndTime(scheduleRequest.get().getRegistrationEndTime());
                });
            }
        }
        if (isCheckUpdate){
            batchesRegistrationScheduleService.saveAll(batchesRegistrationSchedulesOrigin);
        }
    }

    private void handleBatchesRegistrationScheduleDelete(List<UpdateBatchesRegistrationScheduleRequest> batchesRegistrationScheduleRequest,
                                                         List<PriorityGroup> priorityGroups,
                                                         List<BatchesRegistrationSchedule> batchesRegistrationSchedulesOrigin) {
        List<BatchesRegistrationSchedule> batchesRegistrationScheduleDelete = new ArrayList<>();
        for (PriorityGroup priorityGroup : priorityGroups){
            if (batchesRegistrationScheduleRequest.stream().noneMatch(x->x.getPriorityGroupCode().equals(priorityGroup.getPriorityGroupCode()))){
                batchesRegistrationScheduleDelete.add(batchesRegistrationSchedulesOrigin.stream()
                        .filter(x->x.getIdPriorityGroup()
                                .equals(priorityGroup.getIdPriorityGroup()))
                        .findFirst().get());
            }
        }
        if (!CollectionUtils.isEmpty(batchesRegistrationScheduleDelete)) {
            batchesRegistrationScheduleService.deleteAll(batchesRegistrationScheduleDelete);
        }
    }

    private void isAllowUpdateChangeParameter(BatchesRegistration batchesRegistrationOriginal,
                                            UpdateBatchesRegistrationRequest request,
                                            List<BatchesYearGroupRegistration>batchesYearGroupRegistrationsOrigin){
        boolean isCheck = false;
        if (!batchesRegistrationOriginal.getStartTime().equals(request.getStartTime())
            || !batchesRegistrationOriginal.getEndTime().equals(request.getEndTime())){
            isCheck = true;
        }
        List<Integer> idsYearGroupOriginal = new ArrayList<>();
        batchesYearGroupRegistrationsOrigin.forEach(x->idsYearGroupOriginal.add(x.getIdYearGroup()));

        List<YearGroup> yearGroups = yearGroupService.findYearGroupsByIds(idsYearGroupOriginal);
        if (yearGroups.size() != request.getYearGroups().size()){
            isCheck = true;
        }
        for (YearGroup yearGroup : yearGroups){
            if (!request.getYearGroups().contains(yearGroup.getCodeYearGroup())){
                isCheck = true;
            }
        }
        if (isCheck) {
            if (!batchesRegistrationRepository.checkNotExitsBatchesRegistration(request.getYearGroups(),
                    request.getStartTime(), request.getEndTime())) {
                throw new ExitsObjectException();
            }
        }
    }

    private void verifyTimeUpdateBatchesRegistration(Optional<BatchesRegistration> batchesRegistration) {
        if (batchesRegistration.isEmpty()){
            throw new NotFoundException();
        }
        long timeCurrent = new Date().getTime();
        if (timeCurrent >= batchesRegistration.get().getStartTime() && timeCurrent <= batchesRegistration.get().getEndTime()){
            throw new ValidParametersException();
        }
    }

    private void verifyUpdateBatchesRegistration(UpdateBatchesRegistrationRequest request) {
        if (StringUtils.isBlank(request.getTitleBatchesRegistration())
                || StringUtils.isBlank(request.getCodeSemester())
                || ObjectUtils.isEmpty(request.getStartTime())
                || ObjectUtils.isEmpty(request.getEndTime())
                ||  ObjectUtils.isEmpty(request.getIdTimeHired())
                || CollectionUtils.isEmpty(request.getYearGroups())
                || CollectionUtils.isEmpty(request.getBatchesRegistrationSchedule())
                || CollectionUtils.isEmpty(request.getRooms())) {
            throw new ValidParametersException();
        }
        if (hasDuplicationYearGroupCreateBatchesRegistration(request.getYearGroups())){
            throw new ValidParametersException();
        }
        Set<String> seenSchedule = new HashSet<>();
        for (UpdateBatchesRegistrationScheduleRequest scheduleRequest : request.getBatchesRegistrationSchedule()){
            if (StringUtils.isBlank(scheduleRequest.getPriorityGroupCode())
                    || ObjectUtils.isEmpty(scheduleRequest.getRegistrationStartTime())
                    || ObjectUtils.isEmpty(scheduleRequest.getRegistrationEndTime())){
                throw new ValidParametersException();
            }
            if (!seenSchedule.add(scheduleRequest.getPriorityGroupCode())) {
                throw new ValidParametersException();
            }
        }
        Set<String> seenRoom = new HashSet<>();
        for (CreateBatchesRegistrationRoomRequest roomRequest : request.getRooms()){
            if (StringUtils.isBlank(roomRequest.getCodeRoom())) {
                throw new ValidParametersException();
            }
            if (!seenRoom.add(roomRequest.getCodeRoom())) {
                throw new ValidParametersException();
            }
        }
        timeHiredService.findTimeHiredById(request.getIdTimeHired());
    }

    private void initializeBatchesRegistration(CreateBatchesRegistrationRequest request) {
        BatchesRegistration batchesRegistration = storeBatchesRegistration(constructionBatchesRegistration(request));
        List<BatchesYearGroupRegistration> batchesYearGroupRegistration =
                batchesYearGroupRegistrationService.saveAll(constructionBatchesYearGroupRegistrations(batchesRegistration,request));
        List<BatchesRegistrationSchedule> batchesRegistrationSchedules =
                batchesRegistrationScheduleService.saveAll(constructionBatchesRegistrationSchedule(batchesRegistration, request));
        List<BatchesRegistrationRoom> batchesRegistrationRooms =
                batchesRegistrationRoomService.saveAll(constructionBatchesRegistrationRoom(batchesRegistration, request));
    }

    private List<BatchesRegistrationRoom> constructionBatchesRegistrationRoom(BatchesRegistration batchesRegistration, CreateBatchesRegistrationRequest request) {
        List<String> codesRoom = new ArrayList<>();
        request.getRooms().forEach(x->codesRoom.add(x.getCodeRoom()));
        List<Room> rooms = roomService.findAllRoomByListCodeRoom(codesRoom);
        Long timeCurrent = new Date().getTime();
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<BatchesRegistrationRoom> batchesRegistrationRooms = new ArrayList<>();
        for (CreateBatchesRegistrationRoomRequest roomRequest : request.getRooms()){
            BatchesRegistrationRoom batchesRegistrationRoom = new BatchesRegistrationRoom();
            batchesRegistrationRoom.setIdBatchesRegistration(batchesRegistration.getIdBatchesRegistration());
            batchesRegistrationRoom.setIdRoom(rooms.stream().filter(x->x.getCodeRoom().equals(roomRequest.getCodeRoom())).findFirst().get().getIdRoom());
            batchesRegistrationRoom.setStatus(Constants.STATUS_BATCHES_REGISTRATION_ROOM_ACTIVE);
            batchesRegistrationRoom.setTimeCreated(timeCurrent);
            batchesRegistrationRoom.setTimeModified(timeCurrent);
            batchesRegistrationRoom.setIdUserCreated(ktxUser.getIdKtxUser());
            batchesRegistrationRoom.setIdUserModified(ktxUser.getIdKtxUser());
            batchesRegistrationRooms.add(batchesRegistrationRoom);
        }
        return batchesRegistrationRooms;
    }

    private List<BatchesRegistrationSchedule> constructionBatchesRegistrationSchedule(BatchesRegistration batchesRegistration,
                                                                                      CreateBatchesRegistrationRequest request) {
        List<BatchesRegistrationSchedule> batchesRegistrationSchedules = new ArrayList<>();
        List<String> codesPriorityGroup = new ArrayList<>();
        request.getBatchesRegistrationSchedule().forEach(x->codesPriorityGroup.add(x.getPriorityGroupCode()));
        List<PriorityGroup> priorityGroups = priorityGroupService.findPriorityGroupsByListPriorityGroupCode(codesPriorityGroup);
        Long timeCurrent = new Date().getTime();
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        for (CreateBatchesRegistrationScheduleRequest scheduleRequest : request.getBatchesRegistrationSchedule()){
            BatchesRegistrationSchedule batchesRegistrationSchedule = new BatchesRegistrationSchedule();
            batchesRegistrationSchedule.setIdBatchesRegistration(batchesRegistration.getIdBatchesRegistration());
            batchesRegistrationSchedule.setIdPriorityGroup(
                    priorityGroups
                            .stream()
                            .filter(x->x.getPriorityGroupCode()
                            .equals(scheduleRequest.getPriorityGroupCode()))
                            .findFirst().get().getIdPriorityGroup());
            batchesRegistrationSchedule.setStatus(Constants.STATUS_BATCHES_REGISTRATION_SCHEDULE_ACTIVE);
            batchesRegistrationSchedule.setTimeCreated(timeCurrent);
            batchesRegistrationSchedule.setTimeModified(timeCurrent);
            batchesRegistrationSchedule.setIdUserCreated(ktxUser.getIdKtxUser());
            batchesRegistrationSchedule.setIdUserModified(ktxUser.getIdUserModified());
            batchesRegistrationSchedule.setRegistrationStartTime(scheduleRequest.getRegistrationStartTime());
            batchesRegistrationSchedule.setRegistrationEndTime(scheduleRequest.getRegistrationEndTime());
            batchesRegistrationSchedules.add(batchesRegistrationSchedule);
        }
        return batchesRegistrationSchedules;
    }

    private List<BatchesYearGroupRegistration> constructionBatchesYearGroupRegistrations(BatchesRegistration batchesRegistration,
                                                                                  CreateBatchesRegistrationRequest request) {
        List<YearGroup> yearGroups = yearGroupService.findYearGroupsByListCode(request.getYearGroups());
        List<BatchesYearGroupRegistration> yearGroupRegistrations = new ArrayList<>();
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long timeCurrent = new Date().getTime();
        for (YearGroup yearGroup : yearGroups){
            BatchesYearGroupRegistration batchesYearGroupRegistration = new BatchesYearGroupRegistration();
            batchesYearGroupRegistration.setIdBatchesRegistration(batchesRegistration.getIdBatchesRegistration());
            batchesYearGroupRegistration.setIdYearGroup(yearGroup.getIdYearGroup());
            batchesYearGroupRegistration.setStatus(Constants.STATUS_BATCHES_YEAR_GROUP_REGISTRATION_ACTIVE);
            batchesYearGroupRegistration.setTimeCreated(timeCurrent);
            batchesYearGroupRegistration.setTimeModified(timeCurrent);
            batchesYearGroupRegistration.setIdUserCreated(ktxUser.getIdKtxUser());
            batchesYearGroupRegistration.setIdUserModified(ktxUser.getIdKtxUser());
            yearGroupRegistrations.add(batchesYearGroupRegistration);
        }
        return batchesYearGroupRegistrationService.saveAll(yearGroupRegistrations);
    }

    private BatchesRegistration storeBatchesRegistration(BatchesRegistration batchesRegistration) {
        return batchesRegistrationRepository.save(batchesRegistration);
    }

    private BatchesRegistration constructionBatchesRegistration(CreateBatchesRegistrationRequest request) {
        Semester semester = semesterService.findSemesterByCode(request.getCodeSemester());
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long timeCurrent = new Date().getTime();
        BatchesRegistration batchesRegistration = new BatchesRegistration();
        batchesRegistration.setTitle(request.getTitleBatchesRegistration());
        batchesRegistration.setCodeBatchesRegistration(UUID.nameUUIDFromBytes(request.getTitleBatchesRegistration().getBytes()).toString());
        batchesRegistration.setIdTimeHired(request.getIdTimeHired());
        batchesRegistration.setIdSemester(semester.getIdSemester());
        batchesRegistration.setDescription(request.getNotes());
        batchesRegistration.setNotes(request.getNotes());
        batchesRegistration.setTimeCreated(timeCurrent);
        batchesRegistration.setTimeModified(timeCurrent);
        batchesRegistration.setIdUserCreated(ktxUser.getIdKtxUser());
        batchesRegistration.setIdUserModified(ktxUser.getIdKtxUser());
        batchesRegistration.setStartTime(request.getStartTime());
        batchesRegistration.setEndTime(request.getEndTime());
        return batchesRegistration;
    }

    private void verifyCreateBatchesRegistration(CreateBatchesRegistrationRequest request) {
        if (StringUtils.isBlank(request.getTitleBatchesRegistration())
            || StringUtils.isBlank(request.getCodeSemester())
            || ObjectUtils.isEmpty(request.getStartTime())
            || ObjectUtils.isEmpty(request.getEndTime())
            ||  ObjectUtils.isEmpty(request.getIdTimeHired())
            || CollectionUtils.isEmpty(request.getYearGroups())
            || CollectionUtils.isEmpty(request.getBatchesRegistrationSchedule())
            || CollectionUtils.isEmpty(request.getRooms())) {
            throw new ValidParametersException();
        }

        if (new Date().getTime() >= request.getStartTime()){
            throw new ValidParametersException();
        }

        if (hasDuplicationYearGroupCreateBatchesRegistration(request.getYearGroups())){
            throw new ValidParametersException();
        }
        Set<String> seenSchedule = new HashSet<>();
        for (CreateBatchesRegistrationScheduleRequest scheduleRequest : request.getBatchesRegistrationSchedule()){
            if (StringUtils.isBlank(scheduleRequest.getPriorityGroupCode())
                || ObjectUtils.isEmpty(scheduleRequest.getRegistrationStartTime())
                || ObjectUtils.isEmpty(scheduleRequest.getRegistrationEndTime())){
                throw new ValidParametersException();
            }
            if (!seenSchedule.add(scheduleRequest.getPriorityGroupCode())) {
                throw new ValidParametersException();
            }
        }
        Set<String> seenRoom = new HashSet<>();
        for (CreateBatchesRegistrationRoomRequest roomRequest : request.getRooms()){
            if (StringUtils.isBlank(roomRequest.getCodeRoom())) {
                throw new ValidParametersException();
            }
            if (!seenRoom.add(roomRequest.getCodeRoom())) {
                throw new ValidParametersException();
            }
        }
        if (!batchesRegistrationRepository.checkNotExitsBatchesRegistration(request.getYearGroups(),
                request.getStartTime(), request.getEndTime())){
            throw new ExitsObjectException();
        }
        timeHiredService.findTimeHiredById(request.getIdTimeHired());
    }

    private boolean hasDuplicationYearGroupCreateBatchesRegistration(List<String> yearGroups){
        Set<String> seen = new HashSet<>();
        for (String item : yearGroups) {
            if (!seen.add(item)) {
                return true;
            }
        }
        return false;
    }


    private BatchesRegistrationDetailResponse convertToGetDetailBatchesRegistration(BatchesRegistrationDetailDto detailDto) {
        Long timeCurrent = new Date().getTime();
        BatchesRegistrationDetailResponse response = new BatchesRegistrationDetailResponse();
        response.setCodeBatchesRegistration(detailDto.getCodeBatchesRegistration());
        response.setTitleBatchesRegistration(detailDto.getTitleBatchesRegistration());
        response.setTitleSemester(detailDto.getTitleSemester());
        response.setCodeSemester(detailDto.getCodeSemester());
        response.setStartTime(detailDto.getStartTime());
        response.setEndTime(detailDto.getEndTime());
        if (timeCurrent >= response.getStartTime() && timeCurrent <= response.getEndTime()) {
            response.setStatus(Constants.STATUS_BATCHES_REGISTRATION_OPENING);
        } else if (timeCurrent <= response.getStartTime()) {
            response.setStatus(Constants.STATUS_BATCHES_REGISTRATION_NOT_YET_OPEN);
        } else if (timeCurrent >= response.getEndTime()) {
            response.setStatus(Constants.STATUS_BATCHES_REGISTRATION_CLOSED);
        }
        response.setNotes(detailDto.getNotes());
        response.setDescription(detailDto.getDescription());

        TimeHiredResponse timeHiredResponse = new TimeHiredResponse();
        timeHiredResponse.setIdTimeHired(detailDto.getTimeHiredCurrentDto().getIdTimeHired());
        timeHiredResponse.setCodeTimeHired(detailDto.getTimeHiredCurrentDto().getCodeTimeHired());
        timeHiredResponse.setTitleTimeHired(detailDto.getTimeHiredCurrentDto().getTitleTimeHired());
        timeHiredResponse.setTimeHiredStarted(detailDto.getTimeHiredCurrentDto().getTimeStarted());
        timeHiredResponse.setTimeHiredEnded(detailDto.getTimeHiredCurrentDto().getTimeEnded());

        response.setTimeHiredResponse(timeHiredResponse);

        List<BatchesYearGroupRegistrationResponse> yearGroupRegistrationResponses = new ArrayList<>();
        for (BatchesYearGroupRegistrationDto batchesYearGroupRegistrationDto : detailDto.getBatchesYearGroupRegistrationDtos()){
            BatchesYearGroupRegistrationResponse yearGroupRegistrationResponse = new BatchesYearGroupRegistrationResponse();
            yearGroupRegistrationResponse.setIdBatchesYearGroupRegistration(batchesYearGroupRegistrationDto.getIdYearGroup());
            yearGroupRegistrationResponse.setIdYearGroup(batchesYearGroupRegistrationDto.getIdYearGroup());
            yearGroupRegistrationResponse.setTitleYearGroup(batchesYearGroupRegistrationDto.getTitleYearGroup());
            yearGroupRegistrationResponse.setStatus(batchesYearGroupRegistrationDto.getStatus());
            yearGroupRegistrationResponse.setCodeYearGroup(batchesYearGroupRegistrationDto.getCodeYearGroup());
            yearGroupRegistrationResponses.add(yearGroupRegistrationResponse);
        }

        List<BatchesRegistrationScheduleDetailResponse> batchesRegistrationSchedules = new ArrayList<>();
        for (BatchesRegistrationScheduleDto dto : detailDto.getRegistrationScheduleDtos()){
            BatchesRegistrationScheduleDetailResponse registrationSchedule = new BatchesRegistrationScheduleDetailResponse();
            registrationSchedule.setIdBatchesRegistrationSchedule(dto.getIdBatchesRegistrationSchedule());
            registrationSchedule.setStatus(dto.getStatus());
            registrationSchedule.setTimeCreated(dto.getTimeCreated());
            registrationSchedule.setTimeModified(dto.getTimeModified());
            registrationSchedule.setRegistrationStartTime(dto.getRegistrationStartTime());
            registrationSchedule.setRegistrationEndTime(dto.getRegistrationEndTime());
            PriorityGroupDetailResponse priorityGroupDetailResponse = new PriorityGroupDetailResponse();
            priorityGroupDetailResponse.setTitlePriorityGroup(dto.getPriorityGroupDto().getTitle());
            priorityGroupDetailResponse.setPriorityGroupCode(dto.getPriorityGroupDto().getCodePriorityGroup());
            registrationSchedule.setPriorityGroupDetailResponse(priorityGroupDetailResponse);
            batchesRegistrationSchedules.add(registrationSchedule);
        }

        response.setBatchesYearGroupRegistrationResponseList(yearGroupRegistrationResponses);
        response.setBatchesRegistrationScheduleDetailResponses(batchesRegistrationSchedules);
        return response;
    }

    private void verifyGetDetailBatchesRegistration(String codeBatchesRegistration) {
        if (StringUtils.isBlank(codeBatchesRegistration)){
            throw new ValidParametersException();
        }
    }

    private List<FindAllBatchesRegistrationResponse> convertToFindAllBatchesRegistration(List<FindAllBatchesRegistrationDto> content) {
        List<FindAllBatchesRegistrationResponse> responses = new ArrayList<>();
        Long timeCurrent = new Date().getTime();
        for (FindAllBatchesRegistrationDto dto : content) {
            List<BatchesYearGroupRegistrationResponse> yearGroupRegistrationResponses = new ArrayList<>();
            FindAllBatchesRegistrationResponse response = new FindAllBatchesRegistrationResponse();
            response.setTitleBatchesRegistration(dto.getTitleBatchesRegistration());
            response.setCodeBatchesRegistration(dto.getCodeBatchesRegistration());
            response.setTitleSemester(dto.getTitleSemester());
            response.setCodeSemester(dto.getCodeSemester());
            response.setStartTime(dto.getStartTime());
            response.setEndTime(dto.getEndTime());
            if (timeCurrent >= response.getStartTime() && timeCurrent <= response.getEndTime()) {
                response.setStatus(Constants.STATUS_BATCHES_REGISTRATION_OPENING);
            } else if (timeCurrent <= response.getStartTime()) {
                response.setStatus(Constants.STATUS_BATCHES_REGISTRATION_NOT_YET_OPEN);
            } else if (timeCurrent >= response.getEndTime()) {
                response.setStatus(Constants.STATUS_BATCHES_REGISTRATION_CLOSED);
            }

            TimeHiredResponse timeHiredResponse = new TimeHiredResponse();
            timeHiredResponse.setIdTimeHired(dto.getTimeHiredCurrentDto().getIdTimeHired());
            timeHiredResponse.setCodeTimeHired(dto.getTimeHiredCurrentDto().getCodeTimeHired());
            timeHiredResponse.setTimeHiredStarted(dto.getTimeHiredCurrentDto().getTimeStarted());
            timeHiredResponse.setTimeHiredEnded(dto.getTimeHiredCurrentDto().getTimeEnded());
            timeHiredResponse.setTitleTimeHired(dto.getTimeHiredCurrentDto().getTitleTimeHired());
            response.setTimeHiredResponse(timeHiredResponse);
            for (BatchesYearGroupRegistrationDto batchesYearGroupRegistrationDto : dto.getBatchesYearGroupRegistrationDtos()) {
                BatchesYearGroupRegistrationResponse groupRegistrationResponse = new BatchesYearGroupRegistrationResponse();
                groupRegistrationResponse.setIdBatchesYearGroupRegistration(batchesYearGroupRegistrationDto.getIdBatchesYearGroupRegistration());
                groupRegistrationResponse.setIdYearGroup(batchesYearGroupRegistrationDto.getIdYearGroup());
                groupRegistrationResponse.setTitleYearGroup(batchesYearGroupRegistrationDto.getTitleYearGroup());
                groupRegistrationResponse.setStatus(batchesYearGroupRegistrationDto.getStatus());
                yearGroupRegistrationResponses.add(groupRegistrationResponse);
            }
            response.setBatchesYearGroupRegistrationResponseList(yearGroupRegistrationResponses);
            responses.add(response);
        }
        return responses;
    }
}
