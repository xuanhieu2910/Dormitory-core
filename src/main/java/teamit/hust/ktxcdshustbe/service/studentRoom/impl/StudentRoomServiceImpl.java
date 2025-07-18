package teamit.hust.ktxcdshustbe.service.studentRoom.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.dto.studentRoom.FindAllStudentHiredRoomDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.Room;
import teamit.hust.ktxcdshustbe.entity.StudentRoom;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.exception.ValidateFiledException;
import teamit.hust.ktxcdshustbe.repository.room.RoomRepository;
import teamit.hust.ktxcdshustbe.repository.studentRoom.StudentRoomRepository;
import teamit.hust.ktxcdshustbe.repository.timeHired.TimeHiredRepository;
import teamit.hust.ktxcdshustbe.request.studentRoom.ListStudentHiredRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRoom.RemoveStudentInRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRoom.StudentToRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRoom.TransferRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.StudentListRoomHiredRequest;
import teamit.hust.ktxcdshustbe.response.studentRegister.StudentRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.response.studentRoom.ListStudentHiredRoomResponse;
import teamit.hust.ktxcdshustbe.response.studentRoom.StudentSearchAddNewRoomResponse;
import teamit.hust.ktxcdshustbe.response.user.HiredRoomsResponse;
import teamit.hust.ktxcdshustbe.response.user.ListHiredRoomStudentResponse;
import teamit.hust.ktxcdshustbe.service.room.RoomService;
import teamit.hust.ktxcdshustbe.service.studentRegisterRoom.StudentRegisterRoomService;
import teamit.hust.ktxcdshustbe.service.studentRoom.StudentRoomService;
import teamit.hust.ktxcdshustbe.service.user.KtxUserService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class StudentRoomServiceImpl implements StudentRoomService {

    @Autowired
    StudentRoomRepository studentRoomRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    TimeHiredRepository timeHiredRepository;
    @Autowired
    StudentRegisterRoomService studentRegisterRoomService;
    @Autowired
    KtxUserService ktxUserService;
    @Autowired
    RoomService roomService;


    @Override
    public StudentRoom saveStudentRoom(StudentRoom room) {
        return studentRoomRepository.save(room);
    }


    @Override
    public List<HiredRoomsResponse> getRoomsHiredByUser(String codeUser) throws ValidateFiledException {
        validateListRoomHiredDetailRequest(codeUser);
        return studentRoomRepository.getRoomsHiredByUser(codeUser);
    }


    @Override
    public Page<ListHiredRoomStudentResponse> getListHiredRoomStudentResponse(OidcUser principal, StudentListRoomHiredRequest request) {
        KtxUser ktxUser = ktxUserService.findKtxUserByCodeUser(principal.getPreferredUsername().trim().toLowerCase());
        request.setUserId(ktxUser.getIdKtxUser());
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        return studentRoomRepository.getListHiredRoomStudentResponse(request,pageable);
    }

    @Override
    public Page<ListStudentHiredRoomResponse> getListStudentHiredRoomResponse(ListStudentHiredRoomRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllStudentHiredRoomDto> findAllStudentHiredRoomDtos = studentRoomRepository.getListStudentHiredRoomResponse(request, pageable);
        return new PageImpl<>(convertFindAllStudentHiredRoom(findAllStudentHiredRoomDtos.stream().collect(Collectors.toList())), pageable,findAllStudentHiredRoomDtos.getTotalElements());
    }

    private List<ListStudentHiredRoomResponse> convertFindAllStudentHiredRoom(List<FindAllStudentHiredRoomDto> collect) {
        List<ListStudentHiredRoomResponse> responses = new ArrayList<>();
        for (FindAllStudentHiredRoomDto findAllStudentHiredRoomDto : collect) {
            ListStudentHiredRoomResponse response = new ListStudentHiredRoomResponse();
            response.setIdStudentRoom(findAllStudentHiredRoomDto.getIdStudentRoom());
            response.setCodeUser(findAllStudentHiredRoomDto.getCodeUser());
            response.setFullName(findAllStudentHiredRoomDto.getFullName());
            response.setNumberStudent(findAllStudentHiredRoomDto.getNumberStudent());
            response.setPhoneNumber(findAllStudentHiredRoomDto.getPhoneNumber());
            response.setTimeHired(findAllStudentHiredRoomDto.getTimeHired());
            response.setCodeDepartment(findAllStudentHiredRoomDto.getCodeDepartment());
            response.setTitleDepartment(findAllStudentHiredRoomDto.getTitleDepartment());
            response.setCodeRoom(findAllStudentHiredRoomDto.getCodeRoom());
            response.setTitleRoom(findAllStudentHiredRoomDto.getTitleRoom());
            response.setCodeUserModified(findAllStudentHiredRoomDto.getCodeUserModified());
            response.setUserModified(findAllStudentHiredRoomDto.getUserModified());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public StudentRoom findStudentRoomByStudentRoomId(Integer studentRoomId) {
        Optional<StudentRoom> studentRoom = studentRoomRepository.findStudentRoomByStudentRoomId(studentRoomId);
        if (studentRoom.isEmpty()) {
            throw new NotFoundException();
        }
        return studentRoom.get();
    }

    @Override
    public StudentSearchAddNewRoomResponse searchStudentAddNewRoom(String numberStudent) throws Exception {
        Optional<StudentSearchAddNewRoomResponse> response = studentRoomRepository.searchStudentAddNewRoom(numberStudent);
        if (response.isEmpty()) {
            throw new NotFoundException();
        } else if (!response.get().getStatusRegisterRoom().equals(Constants.STATUS_USER_REGISTER_ROOM)){
            throw new ValidateFiledException("Student must declare information parents!");
        }
        validateStudentHiringRoom(response.get().getCodeUser());
        validateStudentRegisterRoom(response.get().getCodeUser());
        return response.get();
    }

    @Override
    public void addStudentToRoom(StudentToRoomRequest request) throws Exception {
        KtxUser student = validateStudentToRoom(request);
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Room> roomOptional = roomService.findRoomByCodeRoom(request.getCodeRoom());
        KtxUser ktxUserOptional = ktxUserService.findKtxUserByCodeUser(request.getCodeUser());
        studentRoomRepository.save(createStudentRoom(roomOptional.get().getIdRoom(),ktxUserOptional.getIdKtxUser(), ktxUser.getIdKtxUser()));
        updateQuantityRoom(roomOptional.get().getIdRoom(), ktxUser.getIdKtxUser());
        updateStatusStudentAddToRoom(student);
    }

    private void updateStatusStudentAddToRoom(KtxUser student){
        student.setStatusRegisterRoom(Constants.STATUS_USER_REGISTER_ROOM);
        ktxUserService.save(student);
    }

    @Override
    public void removeStudentRoom(RemoveStudentInRoomRequest request) throws Exception {
        validateRemoveStudentRoom(request);
        Optional<StudentRoom> studentRoom = studentRoomRepository.findStudentHiringRoomByCodeUser(request.getCodeUser());
        if (studentRoom.isEmpty()) {
            throw new NotFoundException();
        }
        studentRoomRepository.delete(studentRoom.get());
        updateRemainQuantityRoom(request.getCodeRoom());
    }


    private void validateRemoveStudentRoom(RemoveStudentInRoomRequest request){
        KtxUser customUserDetails = ktxUserService.findKtxUserByCodeUser(request.getCodeUser());
        Optional<Room> room = roomService.findRoomByCodeRoom(request.getCodeRoom());
        if (room.isEmpty()) {
            throw new NotFoundException();
        }
    }

    private void updateRemainQuantityRoom(String codeRoom) throws SQLException {
        KtxUser customUserDetails = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int rowUpdate = roomService.updateRemainQuantityRoomWhenToRemoveStudent(codeRoom, customUserDetails.getIdKtxUser());
        if (rowUpdate == Constants.ROW_NOT_UPDATED){
            throw new SQLException("Method remove new student can't update quantity remain amount!");
        }
    }

    private void updateQuantityRoom(Integer roomId, Integer userIdModified) throws SQLException {
        int rowUpdate = roomRepository.updateQuantityAndRemainAmountToAddNewStudent(roomId, userIdModified);
        if (rowUpdate == Constants.ROW_NOT_UPDATED){
            throw new SQLException("Method add new student can't update quantity remain amount!");
        }
    }

    private StudentRoom createStudentRoom(Integer idRoom, Integer idUser, Integer userIdModified) {
        StudentRoom studentRoom = new StudentRoom();
        studentRoom.setIdUser(idUser);
        studentRoom.setIdRoom(idRoom);
        var timeCurrent = Long.valueOf(new Date().getTime());
        studentRoom.setTimeCreated(timeCurrent);
        studentRoom.setTimeModified(timeCurrent);
        studentRoom.setIdTimeHired(timeHiredRepository.getTimeHiredActiveResponse().get().getIdTimeHired());
        studentRoom.setIdUserCreated(userIdModified);
        studentRoom.setIdUserModified(userIdModified);
        studentRoom.setStatus(Constants.STATUS_STUDENT_HIRING_ROOM);
        return studentRoom;
    }

    private KtxUser validateStudentToRoom(StudentToRoomRequest request) {
        KtxUser ktxUser = ktxUserService.findKtxUserByCodeUser(request.getCodeUser());
        Optional<Room> room = roomService.findRoomByCodeRoom(request.getCodeRoom());
        if (room.isEmpty()) {
            throw new NotFoundException();
        }
        return ktxUser;
    }

    private void validateStudentRegisterRoom(String codeUser) throws ValidateFiledException {
        Optional<StudentRegisterRoomResponse> studentRegisterRoomResponse = studentRegisterRoomService.findStudentRegisterRoomByUserCodeUser(codeUser);
        if (studentRegisterRoomResponse.isPresent()) {
            throw new ValidParametersException();
        }
    }

    private void validateStudentHiringRoom(String codeUser) throws ValidateFiledException {
        Optional<StudentRoom> studentRoom = studentRoomRepository.findStudentHiringRoomByCodeUser(codeUser);
        if (studentRoom.isPresent()) {
            throw new ValidParametersException();
        }
    }


    private void validateListRoomHiredDetailRequest(String codeUser) throws ValidateFiledException {
        if (Objects.isNull(codeUser)) {
            throw new ValidParametersException();
        }
    }

    @Override
    public void transferRoom(TransferRoomRequest request) {
        validateDataTransferRoomRequest(request);
        Optional<Room> originalRoom = roomService.findRoomByCodeRoom(request.getOriginalCodeRoom());
        if (originalRoom.isEmpty()){
            throw new NotFoundException();
        }
        Optional<Room> destinationRoom = roomService.findRoomByCodeRoom(request.getDestinationCodeRoom());
        if (destinationRoom.isEmpty()){
            throw new NotFoundException();
        }
        if (destinationRoom.get().getQuantityHired() < destinationRoom.get().getLimitAmountPeople()){
            updateTransferRoom(originalRoom.get() ,destinationRoom.get() , request.getCodeUser());
        } else {
            throw new ValidParametersException();
        }
    }

    private void validateDataTransferRoomRequest(TransferRoomRequest transferRoomRequest) {
        if(StringUtils.isBlank(transferRoomRequest.getCodeUser())
                || StringUtils.isBlank(transferRoomRequest.getOriginalCodeRoom())
                || StringUtils.isBlank(transferRoomRequest.getDestinationCodeRoom())){
            throw new ValidParametersException();
        }
    }

    private void updateTransferRoom(Room originalRoom, Room destinationRoom, String codeUser){
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        updateDestinationRoom(destinationRoom,ktxUser.getIdKtxUser());
        updateOriginalRoom(originalRoom,ktxUser.getIdKtxUser());
        updateStudentRoom(codeUser, originalRoom.getIdRoom(),destinationRoom.getIdRoom(),ktxUser.getIdKtxUser());
    }

    private void updateStudentRoom(String codeUser, Integer originalRoomId, Integer destinationRoomId, Integer userIdModified) {
        KtxUser ktxUser = ktxUserService.findKtxUserByCodeUser(codeUser);
        Optional<StudentRoom> studentRoom = studentRoomRepository.getStudentRoomIsActiveByCodeUserAndRoomIdRoom(codeUser,originalRoomId);
        if (studentRoom.isEmpty()) {
            throw new NotFoundException();
        }
        studentRoom.get().setStatus(Constants.STATUS_STUDENT_REFUND_ROOM);
        studentRoom.get().setTimeModified(new Date().getTime());
        studentRoom.get().setIdUserModified(userIdModified);
        studentRoomRepository.save(studentRoom.get());
        createStudentRoom(ktxUser.getIdKtxUser(),destinationRoomId,userIdModified, studentRoom.get().getIdTimeHired());
    }

    private void createStudentRoom(Integer userId, Integer destinationRoomId, Integer userIdModified,Integer timeHiredId) {
        Long currentTime = new Date().getTime();
        StudentRoom studentRoom = new StudentRoom();
        studentRoom.setIdUser(userId);
        studentRoom.setIdRoom(destinationRoomId);
        studentRoom.setTimeCreated(currentTime);
        studentRoom.setTimeModified(currentTime);
        studentRoom.setIdTimeHired(timeHiredId);
        studentRoom.setIdUserCreated(userIdModified);
        studentRoom.setIdUserModified(userIdModified);
        studentRoom.setStatus(Constants.STATUS_STUDENT_HIRING_ROOM);
        studentRoomRepository.save(studentRoom);
    }


    private void updateDestinationRoom(Room destinationRoom, Integer userIdModified) {
        if (destinationRoom.getLimitAmountPeople().equals(destinationRoom.getQuantityHired() + Constants.QUANTITY_UPDATE_HIRED_ROOM)) {
            destinationRoom.setQuantityHired(destinationRoom.getLimitAmountPeople());
            destinationRoom.setRemainAmount(Constants.DEFAULT_QUANTITY_HIRED);
            destinationRoom.setLimitAmountPeopleRegister(Constants.DEFAULT_QUANTITY_HIRED);
            destinationRoom.setRemainAmountRegister(Constants.DEFAULT_QUANTITY_HIRED);
            destinationRoom.setTimeModified(new Date().getTime());
            destinationRoom.setIdUserModified(userIdModified);
        } else {
            destinationRoom.setQuantityHired(destinationRoom.getQuantityHired() + Constants.QUANTITY_UPDATE_HIRED_ROOM);
            destinationRoom.setRemainAmount(destinationRoom.getRemainAmount() - Constants.QUANTITY_UPDATE_HIRED_ROOM);
            destinationRoom.setLimitAmountPeopleRegister(destinationRoom.getRemainAmount());
            if (destinationRoom.getQuantityRegistered() >= destinationRoom.getLimitAmountPeopleRegister()){
                destinationRoom.setRemainAmountRegister(Constants.DEFAULT_QUANTITY_HIRED);
            } else {
                destinationRoom.setRemainAmountRegister(destinationRoom.getRemainAmountRegister() - Constants.QUANTITY_UPDATE_HIRED_ROOM);
            }
            destinationRoom.setTimeModified(new Date().getTime());
            destinationRoom.setIdUserModified(userIdModified);
        }
        roomRepository.save(destinationRoom);

    }

    private void updateOriginalRoom(Room originalRoom, Integer userIdModified) {
        originalRoom.setQuantityHired(originalRoom.getQuantityHired() - Constants.QUANTITY_UPDATE_HIRED_ROOM);
        originalRoom.setRemainAmount(originalRoom.getLimitAmountPeople() - originalRoom.getQuantityHired());
        originalRoom.setLimitAmountPeopleRegister(originalRoom.getRemainAmount());
        originalRoom.setRemainAmountRegister(originalRoom.getLimitAmountPeopleRegister() - originalRoom.getQuantityRegistered());
        originalRoom.setTimeModified(new Date().getTime());
        originalRoom.setIdUserModified(userIdModified);
        roomRepository.save(originalRoom);
    }
}
