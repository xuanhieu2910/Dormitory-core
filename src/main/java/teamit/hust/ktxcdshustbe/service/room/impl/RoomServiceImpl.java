package teamit.hust.ktxcdshustbe.service.room.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.room.FindAllRoomsDto;
import teamit.hust.ktxcdshustbe.dto.room.SearchInformationRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.room.StudentSearchRoomDto;
import teamit.hust.ktxcdshustbe.dto.serviceRoom.ServiceRoomDto;
import teamit.hust.ktxcdshustbe.entity.Department;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.Room;
import teamit.hust.ktxcdshustbe.entity.ServiceRoom;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.exception.IsBlankException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.room.RoomRepository;
import teamit.hust.ktxcdshustbe.request.room.*;
import teamit.hust.ktxcdshustbe.request.serviceRoom.CreateNewServiceRoomRequest;
import teamit.hust.ktxcdshustbe.request.serviceRoom.EditServiceRoomRequest;
import teamit.hust.ktxcdshustbe.response.room.*;
import teamit.hust.ktxcdshustbe.service.customService.CustomServicesService;
import teamit.hust.ktxcdshustbe.service.department.DepartmentService;
import teamit.hust.ktxcdshustbe.service.room.RoomService;
import teamit.hust.ktxcdshustbe.service.serviceRoom.ServiceRoomService;
import teamit.hust.ktxcdshustbe.service.studentRoom.StudentRoomService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;

import java.sql.SQLException;
import java.util.*;

@Log4j2
@Service
public class RoomServiceImpl implements RoomService {


    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ServiceRoomService serviceRoomService;

    @Autowired
    CustomServicesService customServicesService;

    @Lazy
    @Autowired
    DepartmentService departmentService;

    @Lazy
    @Autowired
    StudentRoomService studentRoomService;


    @Override
    public void changeActiveRoomByCodeRoom(String codeRoom) {
        if (StringUtils.isBlank(codeRoom)){
            throw new ValidParametersException();
        }
        Optional<Room> room = roomRepository.findRoomByCodeRoom(codeRoom);
        if (room.isEmpty()){
            throw new NotFoundException();
        }
        if (room.get().getIsActive().equals(Constants.STATUS_ROOM_IS_ACTIVED)) {
            room.get().setIsActive(Constants.STATUS_ROOM_UN_ACTIVED);
        } else {
            room.get().setIsActive(Constants.STATUS_ROOM_IS_ACTIVED);
        }
        roomRepository.save(room.get());
    }

    @Override
    public void changeSexRoomByCodeRoom(String codeRoom) {
        if (StringUtils.isBlank(codeRoom)){
            throw new ValidParametersException();
        }
        Optional<Room> room = roomRepository.findRoomByCodeRoom(codeRoom);
        if (room.isEmpty()){
            throw new NotFoundException();
        }
        if (room.get().getSexRoom().equals(Constants.FEMALE)) {
            room.get().setSexRoom(Constants.MALE);
        } else {
            room.get().setSexRoom(Constants.FEMALE);
        }
        roomRepository.save(room.get());
    }

    @Override
    public RoomDetailResponse findRoomDetailByCodeRoom(String codeRoom) {
        if (StringUtils.isBlank(codeRoom)) {
            throw new ValidParametersException();
        }
        Optional<Room> room = roomRepository.findRoomByCodeRoom(codeRoom);
        if (room.isEmpty()) {
            throw new NotFoundException();
        }
        Department department = departmentService.findDepartmentById(room.get().getIdDepartment());
        List<ServiceRoomDto> serviceRooms = serviceRoomService.findAllServicesRoomByRoomId(room.get().getIdRoom());
        return convertToRoomDetailResponse(room.get(),convertToServiceRoomResponse(serviceRooms), department);
    }

    @Override
    public void createNewRoom(CreateNewRoomRequest request){
        verifyCreateNewRoom(request);
        Department department = departmentService.findDepartmentByCodeDepartment(request.getCodeDepartment());
        List<teamit.hust.ktxcdshustbe.entity.Service> services = handleGetListServiceByCodeService(request.getServicesRoom());
        Optional<Room> previousRoom = roomRepository.findRoomByTitleRoomAndCodeDepartment(request.getTitle(), department.getCodeDepartment());
        if (previousRoom.isPresent()){
            throw new ExitsObjectException();
        }
        storeNewRoom(request, department, services);
    }

    private List<teamit.hust.ktxcdshustbe.entity.Service> handleGetListServiceByCodeService(List<CreateNewServiceRoomRequest> servicesRoom) {
        List<String> listCodeService = new ArrayList<>();
        servicesRoom.forEach(x->listCodeService.add(x.getCodeService()));
        return customServicesService.findAllServiceByCodesService(listCodeService);
    }

    private void verifyCreateNewRoom(CreateNewRoomRequest request) {
        if (StringUtils.isBlank(request.getCodeDepartment()) || StringUtils.isBlank(request.getTitle())
        || ObjectUtils.isEmpty(request.getSexRoom()) || StringUtils.isBlank(request.getPrice())
        || ObjectUtils.isEmpty(request.getLimitAmountPeople()) || ObjectUtils.isEmpty(request.getStatus())
        || CollectionUtils.isEmpty(request.getServicesRoom())){
            throw new ValidParametersException();
        }
        if (!request.getSexRoom().equals(Constants.FEMALE) && request.getSexRoom().equals(Constants.MALE)){
            throw new ValidParametersException();
        }
        for (CreateNewServiceRoomRequest serviceRoomRequest : request.getServicesRoom()){
            if (StringUtils.isBlank(serviceRoomRequest.getCodeService()) || ObjectUtils.isEmpty(serviceRoomRequest.getStatus())){
                throw new ValidParametersException();
            }
        }
    }

    @Override
    public void updateQuantityAndRemainAmountCancelRegisterRoom(Integer idRoom, Integer quantity, Integer userIdModified){
        int rowUpdate = roomRepository.updateQuantityAndRemainAmountCancelRegisterRoom(idRoom, quantity, userIdModified);
        if (rowUpdate == Constants.ROW_NOT_UPDATED) {
            log.info("Can't update quantity when cancel remain amount by " + idRoom);
            throw new ValidParametersException();
        }
    }

    @Override
    public void updateQuantityAndRemainAmountAcceptRegisterAndHiredRoom(Integer idRoom, Integer quantity, Integer userIdModified){
       int rowUpdate = roomRepository.updateQuantityAndRemainAmountAcceptRegisterAndHiredRoom(idRoom,quantity,userIdModified);
       if (rowUpdate == Constants.ROW_NOT_UPDATED){
           log.info("Can't update quantity remain hen accept amount by " + idRoom);
           throw new ValidParametersException();
       }
    }

    @Transactional
    @Override
    public void editRoom(EditRoomRequest request){
        verifyEditRoom(request);
        Optional<Room> room = roomRepository.findRoomByCodeRoom(request.getCodeRoom());
        if (room.isEmpty()){
            throw new NotFoundException();
        }
        Department rootDepartment = departmentService.findDepartmentById(room.get().getIdDepartment());
        if (!rootDepartment.getCodeDepartment().equals(request.getCodeDepartment())){
            Optional<Room> roomOther = roomRepository.findRoomByTitleRoomAndCodeDepartment(request.getTitle(),
                    request.getCodeDepartment());
            if (roomOther.isPresent()){
                throw new ExitsObjectException();
            }
        } else {
            if (!room.get().getTitle().equals(request.getTitle())){
                Optional<Room> roomOptional = roomRepository.findRoomByTitleRoomAndCodeDepartment(request.getTitle(),
                        rootDepartment.getCodeDepartment());
                if (roomOptional.isPresent()){
                    throw new ExitsObjectException();
                }
            }
        }
        if (request.getLimitAmountPeople() < Math.max(room.get().getQuantityRegistered(), room.get().getQuantityHired())){
            throw new ValidParametersException();
        }
        Department departmentUpdate = departmentService.findDepartmentByCodeDepartment(request.getCodeDepartment());
        updateEditRoom(request, room.get(), departmentUpdate);
        updateEditServiceRoom(request.getEditServiceRoomRequest(), room.get());
    }

    private void verifyEditRoom(EditRoomRequest request) {
        if (StringUtils.isBlank(request.getCodeDepartment()) || StringUtils.isBlank(request.getCodeRoom())
                || StringUtils.isBlank(request.getPrice()) || Objects.isNull(request.getSexRoom())
                || Objects.isNull(request.getLimitAmountPeople())
                || Objects.isNull(request.getStatus())
                || CollectionUtils.isEmpty(request.getEditServiceRoomRequest())) {
            throw new ValidParametersException();
        }
        if (!request.getSexRoom().equals(Constants.FEMALE) && request.getSexRoom().equals(Constants.MALE)){
            throw new ValidParametersException();
        }
        for (EditServiceRoomRequest serviceRoomRequest : request.getEditServiceRoomRequest()){
            if (StringUtils.isBlank(serviceRoomRequest.getCodeService()) || ObjectUtils.isEmpty(serviceRoomRequest.getStatus())){
                throw new ValidParametersException();
            }
        }
    }

    private List<Room> findRoomsByIds(List<Integer>ids){
        List<Room> rooms = roomRepository.findAllById(ids);
        if (rooms.isEmpty()) {
            throw new NotFoundException();
        }
        return rooms;
    }


    @Override
    public Page<RoomsForStudentRentResponse> getRoomsForStudentRent(FindAllRoomsForRentRequest request) {
        validateStudentRegisterRoomRequest(request);
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllRoomsDto> findAllRoomsDtos = roomRepository.findAllRoomsForRent(request, pageable);
        return new PageImpl<>(convertToRoomsForStudentRent(findAllRoomsDtos.getContent()), pageable,
                findAllRoomsDtos.getTotalElements());
    }

    private List<RoomsForStudentRentResponse> convertToRoomsForStudentRent(List<FindAllRoomsDto> contents) {
        List<RoomsForStudentRentResponse> responses = new ArrayList<>();
        for (FindAllRoomsDto dto : contents){
            RoomsForStudentRentResponse response = new RoomsForStudentRentResponse();
            response.setCodeRoom(dto.getCodeRoom());
            response.setTitleRoom(dto.getTitle());
            response.setPrice(dto.getPrice());
            response.setLimitAmountPeopleRegister(dto.getLimitAmountPeopleRegister());
            response.setQuantityRegister(dto.getQuantityRegistered());
            response.setGender(dto.getSexRoom().equals(Constants.FEMALE) ? Constants.TITLE_SEX[0] : Constants.TITLE_SEX[1]);
            response.setRemainAmountRegister(dto.getRemainAmountRegister());
            response.setTimeCreated(dto.getTimeCreated());
            response.setTimeModified(dto.getTimeModified());
            response.setCodeDepartment(dto.getCodeDepartment());
            response.setTitleDepartment(dto.getTitleDepartment());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public void updateQuantityStudentRegisterRoom(String codeRoom) {
        int rowEffect = roomRepository.updateQuantityStudentRegisterRoom(codeRoom);
        if (rowEffect == Constants.ROW_NOT_UPDATED) {
            log.info("Can't register room, room unavailable by room id " + codeRoom);
            throw new ValidParametersException();
        }
    }

    @Override
    public SearchRoomResponse searchRoomToTranfer(SearchRoomToTranferRequest searchRoom){
        validateSearchRoom(searchRoom);
        Optional<SearchRoomResponse> response = roomRepository.searchRoomToTranfer(searchRoom.getCodeDepartment(),
                searchRoom.getCodeRoom(), searchRoom.getSexUser());
        if (response.isEmpty()){
            throw new NotFoundException();
        }
        return response.get();
    }

    @Override
    public void updateQuantityRegisterOriginRoom(Integer originRoomId) {
        int rowUpdates = roomRepository.updateQuantityRegisterOriginRoom(originRoomId);
        if (rowUpdates == Constants.ROW_NOT_UPDATED){
            throw new ValidParametersException();
        }
    }

    @Override
    public void updateQuantityStudentRegisterDestinationRoom(Integer roomId) {
        int rowUpdates = roomRepository.updateQuantityStudentRegisterDestinationRoom(roomId);
        if (rowUpdates == Constants.ROW_NOT_UPDATED){
            throw new ValidParametersException();
        }
    }


    @Override
    public int updateRemainQuantityRoomWhenToRemoveStudent(String codeRoom, Integer idKtxUser) {
        return roomRepository.updateRemainQuantityRoomWhenToRemoveStudent(codeRoom, idKtxUser);
    }

    @Override
    public Optional<Room> findRoomByCodeRoom(String codeRoom) {
        Optional<Room> roomOptional = roomRepository.findRoomByCodeRoom(codeRoom);
        if (roomOptional.isEmpty()) {
            throw new NotFoundException();
        }
        return roomOptional;
    }

    @Override
    public Page<FindAllRoomsResponse> findAllRoom(FindAllRoomsRequest request) {
        verifyFindAllRoom(request);
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllRoomsDto> findAllRoomsDtos = roomRepository.findAllRooms(request, pageable);
        return new PageImpl<>(convertToFindAllRoomsResponse(findAllRoomsDtos.getContent()), pageable, findAllRoomsDtos.getTotalElements());
    }

    @Override
    public Page<StudentSearchRoomResponse> studentSearchRoom(StudentSearchRoomRequest request) {
        verifyStudentSearchRoom(request);
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<StudentSearchRoomDto> studentSearchRoom = roomRepository.findAllRoomStudentSearch(request, pageable);
        return new PageImpl<>(convertToStudentSearchRoom(studentSearchRoom.getContent()), pageable, studentSearchRoom.getTotalElements());
    }

    private List<StudentSearchRoomResponse> convertToStudentSearchRoom(List<StudentSearchRoomDto> content) {
        List<StudentSearchRoomResponse> responses = new ArrayList<>();
        for (StudentSearchRoomDto dto : content){
            StudentSearchRoomResponse response = new StudentSearchRoomResponse();
            response.setCodeDepartment(dto.getCodeDepartment());
            response.setTitleDepartment(dto.getTitleDepartment());
            response.setCodeRoom(dto.getCodeRoom());
            response.setTitleRoom(dto.getTitleRoom());
            response.setPrice(dto.getPrice());
            response.setLimitAmountPeopleRegister(dto.getLimitAmountPeopleRegister());
            response.setSex(dto.getSex().equals(Constants.FEMALE) ? Constants.TITLE_SEX[0] : Constants.TITLE_SEX[1]);
            response.setRemainAmountRegister(dto.getRemainAmountRegister());
            responses.add(response);
        }
        return responses;
    }

    private void verifyStudentSearchRoom(StudentSearchRoomRequest request) {
        if (StringUtils.isBlank(request.getCodeDepartment()) || ObjectUtils.isEmpty(request.getGender())){
            throw new ValidParametersException();
        }
    }

    @Override
    public Page<SearchInformationRegisterRoomResponse> SearchInformationRegisterRoom(SearchInformationRegisterRoomRequest request){
        verifySearchInformationRegisterRoom(request);
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<SearchInformationRegisterRoomDto> searchInformationRegisterRoomDtos = roomRepository.findInformationRegisterRoom(request, pageable);
        return new PageImpl<>(convertToSearchInformationRegisterRoomResponse(searchInformationRegisterRoomDtos.getContent()), pageable, searchInformationRegisterRoomDtos.getTotalElements());
    }

    private List<SearchInformationRegisterRoomResponse> convertToSearchInformationRegisterRoomResponse(List<SearchInformationRegisterRoomDto> dtos) {
        List<SearchInformationRegisterRoomResponse> responses = new ArrayList<>();
        for (SearchInformationRegisterRoomDto dto : dtos){
            SearchInformationRegisterRoomResponse response = new SearchInformationRegisterRoomResponse();
            response.setCodeUser(dto.getCodeUser());
            response.setUserName(dto.getUserName());
            response.setTitleRoom(dto.getTitleRoom());
            response.setTitleSemester(dto.getTitleSemester());
            response.setTimeStarted(dto.getTimeStarted());
            response.setCodeDepartment(dto.getCodeDepartment());
            responses.add(response);
        }
        return responses;
    }
    private void verifySearchInformationRegisterRoom(SearchInformationRegisterRoomRequest request) {
        if (StringUtils.isBlank(request.getCodeDepartment())
        || StringUtils.isBlank(request.getTitleRoom())
        || StringUtils.isBlank(request.getTitleSemester())){
            throw new ValidParametersException();
        }
    }

    @Override
    public Optional<Room> findRoomByIdRoom(Integer idRoom) {
        Optional<Room> roomOptional = roomRepository.findRoomByIdRoom(idRoom);
        if (roomOptional.isEmpty()) {
            throw new NotFoundException();
        }
        return roomOptional;
    }

    private List<FindAllRoomsResponse> convertToFindAllRoomsResponse(List<FindAllRoomsDto> content) {
        List<FindAllRoomsResponse> responses = new ArrayList<>();
        for (FindAllRoomsDto dto : content){
            FindAllRoomsResponse response = new FindAllRoomsResponse();
            response.setTitle(dto.getTitle());
            response.setPrice(dto.getPrice());
            response.setLimitAmountPeopleHired(dto.getLimitAmountPeople());
            response.setQuantityHired(dto.getQuantityHired());
            response.setSex(dto.getSexRoom().equals(Constants.FEMALE) ? Constants.TITLE_SEX[0] : Constants.TITLE_SEX[1]);
            response.setRemainAmount(dto.getRemainAmount());
            response.setIsActive(dto.getIsActive());
            response.setCodeRoom(dto.getCodeRoom());
            responses.add(response);
        }
        return responses;
    }

    private void verifyFindAllRoom(FindAllRoomsRequest request) {
        if (StringUtils.isBlank(request.getCodeDepartment())){
            throw new ValidParametersException();
        }
    }

    private void validateSearchRoom(SearchRoomToTranferRequest searchRoom)  {
        if(StringUtils.isBlank(searchRoom.getCodeDepartment()) || StringUtils.isBlank(searchRoom.getCodeRoom())
            || Objects.isNull(searchRoom.getSexUser())) {
            throw new ValidParametersException();
        }
    }

    private void validateStudentRegisterRoomRequest(FindAllRoomsForRentRequest request) {
        if (StringUtils.isBlank(request.getCodeDepartment())) {
            throw new IsBlankException();
        }
    }


    private void updateEditServiceRoom(List<EditServiceRoomRequest> serviceRoomRequests, Room room){
        List<String> codesService = new ArrayList<>();
        serviceRoomRequests.forEach(x->codesService.add(x.getCodeService()));
        List<ServiceRoomDto> serviceRoomsDtos =
                serviceRoomService.findServicesRoomByCodeRoomAndCodesService(room.getCodeRoom(), codesService);
        List<ServiceRoom> serviceRooms = new ArrayList<>();
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication();
        for (ServiceRoomDto serviceRoomDto : serviceRoomsDtos){
            ServiceRoom serviceRoom = new ServiceRoom();
            serviceRoom.setIdServiceRoom(serviceRoomDto.getIdServiceRoom());
            serviceRoom.setIdService(serviceRoomDto.getIdService());
            serviceRoom.setIdRoom(serviceRoomDto.getIdRoom());
            serviceRoom.setTimeCreated(serviceRoomDto.getTimeCreated());
            serviceRoom.setTimeModified(new Date().getTime());
            serviceRoom.setStatus(serviceRoomRequests.stream().filter(x->x.getCodeService().equals(serviceRoomDto.getCodeService())).findFirst().get().getStatus());
            serviceRoom.setIdUserCreated(serviceRoomDto.getIdUserCreated());
            serviceRoom.setIdUserModified(ktxUser.getIdKtxUser());
            serviceRooms.add(serviceRoom);
        }
        serviceRoomService.storedServiceRooms(serviceRooms);
    }

    private void updateEditRoom(EditRoomRequest request, Room room, Department department){
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!request.getLimitAmountPeople().equals(room.getLimitAmountPeople())){
            room.setRemainAmount(request.getLimitAmountPeople() - room.getQuantityHired());
            /**
             *
             * Update after
             *
             * */
        }
        room.setIdDepartment(department.getIdDepartment());
        room.setSexRoom(request.getSexRoom());
        room.setPrice(request.getPrice());
        room.setTitle(request.getTitle());
        room.setLimitAmountPeople(request.getLimitAmountPeople());
        room.setIsActive(request.getStatus());
        room.setTimeModified(new Date().getTime());
        room.setIdUserModified(ktxUser.getIdKtxUser());
        room.setLimitAmountPeopleRegister(request.getLimitAmountPeople());
        roomRepository.save(room);
    }


    private void storeNewRoom(CreateNewRoomRequest request,
                              Department department,
                              List<teamit.hust.ktxcdshustbe.entity.Service> services){
        Room room = roomRepository.save(initializeRoom(request, department));
        serviceRoomService.storedServiceRooms(initializeServiceRoom(room, services, request.getServicesRoom() ));
    }

    private List<ServiceRoom> initializeServiceRoom(Room room,
                                                    List<teamit.hust.ktxcdshustbe.entity.Service> services,
                                                    List<CreateNewServiceRoomRequest> serviceRoomRequests){
        List<ServiceRoom> serviceRooms = new ArrayList<>();
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long timeCurrent = new Date().getTime();
        for (teamit.hust.ktxcdshustbe.entity.Service ser: services){
            ServiceRoom serviceRoom = new ServiceRoom();
            serviceRoom.setIdService(ser.getIdService());
            serviceRoom.setIdRoom(room.getIdRoom());
            serviceRoom.setTimeCreated(timeCurrent);
            serviceRoom.setTimeModified(timeCurrent);
            serviceRoom.setStatus(serviceRoomRequests.stream().filter(x->x.getCodeService().equals(ser.getCodeService())).findFirst().get().getStatus());
            serviceRoom.setIdUserCreated(ktxUser.getIdKtxUser());
            serviceRoom.setIdUserModified(ktxUser.getIdKtxUser());
            serviceRooms.add(serviceRoom);
        }
        return serviceRooms;
    }

    private Room initializeRoom(CreateNewRoomRequest request, Department department){
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication();
        Room room = new Room();
        Long timeCurrent = new Date().getTime();
        room.setTitle(request.getTitle());
        room.setIdDepartment(department.getIdDepartment());
        room.setSexRoom(request.getSexRoom());
        room.setPrice(request.getPrice());
        room.setTimeCreated(timeCurrent);
        room.setTimeModified(timeCurrent);
        room.setIdUserCreated(ktxUser.getIdKtxUser());
        room.setIdUserModified(ktxUser.getIdKtxUser());
        room.setIsActive(request.getStatus());
        room.setLimitAmountPeople(request.getLimitAmountPeople());
        room.setQuantityHired(Constants.DEFAULT_QUANTITY_HIRED);
        room.setRemainAmount(request.getLimitAmountPeople());
        room.setLimitAmountPeopleRegister(request.getLimitAmountPeople());
        room.setQuantityRegistered(Constants.DEFAULT_QUANTITY_REGISTER);
        room.setRemainAmountRegister(request.getLimitAmountPeople());
        room.setCodeRoom(UUID.nameUUIDFromBytes(request.getTitle().getBytes()).toString());
        return room;
    }


    private RoomDetailResponse convertToRoomDetailResponse(Room room,
                                                           List<ServiceRoomResponse> serviceRoomResponses,
                                                           Department department) {
        RoomDetailResponse roomDetailResponse = new RoomDetailResponse();
        roomDetailResponse.setCodeRoom(room.getCodeRoom());
        roomDetailResponse.setTitle(room.getTitle());
        roomDetailResponse.setPrice(room.getPrice());
        roomDetailResponse.setLimitAmountPeopleHired(room.getLimitAmountPeople());
        roomDetailResponse.setQuantityHired(room.getQuantityHired());
        roomDetailResponse.setSex(room.getSexRoom().equals(Constants.FEMALE) ? Constants.TITLE_SEX[0] : Constants.TITLE_SEX[1]);
        roomDetailResponse.setRemainAmount(room.getRemainAmount());
        roomDetailResponse.setIsActive(room.getIsActive());
        roomDetailResponse.setCodeDepartment(department.getCodeDepartment());
        roomDetailResponse.setTitleDepartment(department.getTitle());
        roomDetailResponse.setServiceRoomResponses(serviceRoomResponses);
        return   roomDetailResponse;
    }

    private List<ServiceRoomResponse> convertToServiceRoomResponse(List<ServiceRoomDto> serviceRoomDtos){
        List<ServiceRoomResponse> res = new ArrayList<>();
        for (ServiceRoomDto dto : serviceRoomDtos){
            ServiceRoomResponse serviceRoomResponse = new ServiceRoomResponse();
            serviceRoomResponse.setCodeService(dto.getCodeService());
            serviceRoomResponse.setDescription(dto.getDescription());
            serviceRoomResponse.setTitle(dto.getTitle());
            serviceRoomResponse.setStatus(dto.getStatus());
            res.add(serviceRoomResponse);
        }
        return res;
    }

}
