package teamit.hust.ktxcdshustbe.service.studentRegisterRoom.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamit.hust.ktxcdshustbe.dto.registerRoom.StudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.studentRoom.DataStudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.user.UserRegisterRoomDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.Room;
import teamit.hust.ktxcdshustbe.entity.StudentRegisterRoom;
import teamit.hust.ktxcdshustbe.entity.StudentRoom;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.studentRegisterRoom.StudentRegisterRoomRepository;
import teamit.hust.ktxcdshustbe.request.registerRoom.ChangeRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRegister.AcceptPaymentRequest;
import teamit.hust.ktxcdshustbe.request.studentRegister.CreateRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.ApprovedUserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.UserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.studentRegister.StudentRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.response.user.UserRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.service.batchesRegistration.BatchesRegistrationService;
import teamit.hust.ktxcdshustbe.service.room.RoomService;
import teamit.hust.ktxcdshustbe.service.studentRegisterRoom.StudentRegisterRoomService;
import teamit.hust.ktxcdshustbe.service.studentRoom.StudentRoomService;
import teamit.hust.ktxcdshustbe.service.user.KtxUserService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.PropertiesUtil;

import java.util.*;

@Log4j2
@Service
public class StudentRegisterRoomServiceImpl implements StudentRegisterRoomService {

    @Autowired
    StudentRegisterRoomRepository studentRegisterRoomRepository;
    @Lazy
    @Autowired
    RoomService roomService;
    @Lazy
    @Autowired
    StudentRoomService studentRoomService;


    @Override
    public void saveInfoApprovedStudentRegisterRoom(StudentRegisterRoom room) {
        studentRegisterRoomRepository.save(room);
    }

    @Transactional
    @Override
    public void changeRegisterRoomStudent(ChangeRegisterRoomRequest request) {
        verifyChangeRegisterRoomStudent(request);
        Optional<StudentRegisterRoomDto> dto = studentRegisterRoomRepository.getInformationRegisterRoomCurrent();
        if (dto.isEmpty() || new Date().getTime() > dto.get().getExpiresAt()){
            throw new ValidParametersException();
        }

        Optional<StudentRegisterRoom> studentRegisterRoom = 
                studentRegisterRoomRepository.findStudentRegisterRoomById(dto.get().getIdStudentRegisterRoom());
        if (studentRegisterRoom.isEmpty()){
            throw new NotFoundException();
        }
        updateInformationStudentRegisterRoom(studentRegisterRoom.get(), request.getCodeRoom());
    }

    private void updateInformationStudentRegisterRoom(StudentRegisterRoom studentRegisterRoom, String codeRoom) {
        Optional<DataStudentRegisterRoomDto> dataStudentRegisterRoomDto =
                studentRegisterRoomRepository.getDataStudentToRegisterRoomByCodeRoom(codeRoom);
        if (dataStudentRegisterRoomDto.isEmpty()){
            throw new ValidParametersException();
        }
        roomService.updateRemainQuantityRegisterRoomByIdRoom(dataStudentRegisterRoomDto.get().getIdRoom());
        roomService.updateRemainQuantityRegisterRoomWhenStudentChangeRoom(studentRegisterRoom.getIdRoom());
        studentRegisterRoomRepository.save(updateFieldStudentRegisterRoom(studentRegisterRoom,
                dataStudentRegisterRoomDto.get().getIdRoom()));
    }

    private StudentRegisterRoom updateFieldStudentRegisterRoom(StudentRegisterRoom studentRegisterRoom, Integer idRoom) {
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long timeCurrent = new Date().getTime();
        studentRegisterRoom.setIdRoom(idRoom);
        studentRegisterRoom.setTimeModified(new Date().getTime());
        studentRegisterRoom.setExpiresAt(timeCurrent + Long.parseLong(PropertiesUtil.getProperty("time-holding.register-room")));
        studentRegisterRoom.setIdUserModified(ktxUser.getIdKtxUser());
        return studentRegisterRoom;
    }

    private void verifyChangeRegisterRoomStudent(ChangeRegisterRoomRequest request) {
        if (StringUtils.isBlank(request.getCodeRoom())){
            throw new ValidParametersException();
        }
        if (!studentRegisterRoomRepository.isAllowRegisterBatchesRegistration()){
            throw new ValidParametersException();
        }
        if (studentRegisterRoomRepository.isExistsRegisteredRoomInBatchesRegistrationCurrent()){
            throw new ValidParametersException();
        }
        if (!studentRegisterRoomRepository.isAllowRegisterRoomByCodeRoom(request.getCodeRoom())){
            throw new ValidParametersException();
        }
    }


    @Override
    public void acceptPaymentRegisterRoom(AcceptPaymentRequest request) {
        // Tìm đơn đăng ký như cũ
        StudentRegisterRoom studentRegisterRoom = studentRegisterRoomRepository
                .findById(request.getStudentRegisterRoomId()) // Dùng findById có sẵn
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public int getBadgeRegisterRoom(OidcUser principal) {
        KtxUser customUserDetails =  principal.getUserInfo().getClaim(Constants.CLAIMS_INFORMATION_USER);
        return studentRegisterRoomRepository.getBadgeRegisterRoomByUserId(customUserDetails.getIdKtxUser());
    }

    @Override
    public StudentRegisterRoomResponse getInformationRegisterRoom(OidcUser principal) {
        KtxUser customUserDetails = principal.getUserInfo().getClaim(Constants.CLAIMS_INFORMATION_USER);
        return studentRegisterRoomRepository.getInformationRegisterRoom(customUserDetails.getIdKtxUser());
    }


    @Override
    public Optional<StudentRegisterRoomResponse> findStudentRegisterRoomByUserCodeUser(String codeUser) {
        return studentRegisterRoomRepository.findStudentRegisterRoomByCodeUser(codeUser);
    }

    @Override
    public Page<UserRegisterRoomResponse> findAllUserRegisterRoom(UserRegisterRoomRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<UserRegisterRoomDto> userRegisterRoomRequests = studentRegisterRoomRepository.findAllUserRegisterRoomDto(request,pageable);
        List<UserRegisterRoomResponse> responses = convertToUserRegisterRoomResponse(userRegisterRoomRequests.stream().toList());
        return new PageImpl<>(responses,pageable, userRegisterRoomRequests.getTotalElements());
    }

    private List<UserRegisterRoomResponse> convertToUserRegisterRoomResponse(List<UserRegisterRoomDto> userRegisterRoomDto) {
        List<UserRegisterRoomResponse> responses = new ArrayList<>();
        for (UserRegisterRoomDto dto : userRegisterRoomDto){
            UserRegisterRoomResponse response = new UserRegisterRoomResponse();
            response.setIdRegisterRoom(dto.getIdRegisterRoom());
            response.setCodeUser(dto.getCodeUser());
            response.setValue(dto.getValue());
            response.setTimeRegister(dto.getTimeRegister());
            response.setCodeDepartment(dto.getCodeDepartment());
            response.setTitleDepartment(dto.getTitleDepartment());
            response.setCodeRoom(dto.getCodeRoom());
            response.setTitleRoom(dto.getTitleRoom());
            response.setTimeHiredId(dto.getIdTimeHired());
            response.setCodeSemester(dto.getCodeSemester());
            response.setTitleSemester(dto.getTitleSemester());
            response.setStatusRegisterInformation(dto.getStatusInformationRegister());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public Optional<StudentRegisterRoom> findStudentRegisterRoomByCode(String codeUser) {
        Optional<StudentRegisterRoom> studentRegisterRoomOptional = studentRegisterRoomRepository.findByCode(codeUser);
        if (studentRegisterRoomOptional.isEmpty()){
            throw new NotFoundException();
        }
        return studentRegisterRoomOptional;
    }

    @Override
    public void verifyRegisterRoom() {
        if (!studentRegisterRoomRepository.isAllowRegisterBatchesRegistration()){
            throw new ValidParametersException();
        }
        if (studentRegisterRoomRepository.isExistsRegisteredRoomInBatchesRegistrationCurrent()){
            throw new ValidParametersException();
        }
    }

    @Override
    public StudentRegisterRoomResponse getRegisterRoomCurrent() {
        Optional<StudentRegisterRoomDto> dto = studentRegisterRoomRepository.getInformationRegisterRoomCurrent();
        if (dto.isEmpty() || new Date().getTime() > dto.get().getExpiresAt()){
            return new StudentRegisterRoomResponse();
        }
        return convertToStudentRegisterRoomResponse(dto.get());
    }

    @Override
    public StudentRegisterRoom findStudentRegisterRoomById(Integer idStudentRegisterRoom) {
        Optional<StudentRegisterRoom> studentRegisterRoom =
                studentRegisterRoomRepository.findStudentRegisterRoomByIdStudentRegisterRoom(idStudentRegisterRoom);
        if (studentRegisterRoom.isEmpty()){
            throw new NotFoundException();
        }
        return studentRegisterRoom.get();
    }


    @Transactional
    @Override
    public void createStudentRegisterRoom(CreateRegisterRoomRequest request) {
        verifyCreateStudentRegisterRoom(request);
        Optional<DataStudentRegisterRoomDto> dataStudentRegisterRoomDto =
                studentRegisterRoomRepository.getDataStudentToRegisterRoomByCodeRoom(request.getCodeRoom());
        if (dataStudentRegisterRoomDto.isEmpty()){
            throw new ValidParametersException();
        }
        initializeStudentRegisterRoom(dataStudentRegisterRoomDto.get());
    }

    @Override
    public StudentRegisterRoom saveStudentRoomRegisterRoom(StudentRegisterRoom studentRegisterRoom) {
        return studentRegisterRoomRepository.save(studentRegisterRoom);
    }

    @Override
    public StudentRegisterRoom getStudentRegisterRoomByIdOrder(Integer idOrder) {
        Optional<StudentRegisterRoom> studentRegisterRoomOptional = studentRegisterRoomRepository.findStudentRegisterRoomByIdOrder(idOrder);
        if (studentRegisterRoomOptional.isEmpty()){
            throw new NotFoundException();
        }
        return studentRegisterRoomOptional.get();
    }

    @Override
    public List<StudentRegisterRoom> findListStudentRegisterRoomByCodeRoomAndStatus(String codeRoom, Integer status) {
        List<StudentRegisterRoom> studentRegisterRooms = studentRegisterRoomRepository.findListStudentRegisterRoomByCodeRoomAndStatus(codeRoom,status);
        if (studentRegisterRooms.isEmpty()){
            throw new NotFoundException();
        }
        return studentRegisterRooms;
    }

    @Override
    public Page<UserRegisterRoomResponse> findAllInfoAnUserRegisterRoomByCode(UserRegisterRoomRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<UserRegisterRoomDto> userRegisterRoomRequests = studentRegisterRoomRepository.findAllInfoAnUserRegisterRoomDto(request,pageable);
        List<UserRegisterRoomResponse> responses = convertToUserRegisterRoomResponse(userRegisterRoomRequests.stream().toList());
        return new PageImpl<>(responses,pageable, userRegisterRoomRequests.getTotalElements());
    }

    private void initializeStudentRegisterRoom(DataStudentRegisterRoomDto dataStudentRegisterRoomDto) {
        // update quantity register room
        roomService.updateRemainQuantityRegisterRoomByIdRoom(dataStudentRegisterRoomDto.getIdRoom());
        storeStudentRegisterRoom(constructionStudentRegisterRoom(dataStudentRegisterRoomDto));
    }

    private StudentRegisterRoom storeStudentRegisterRoom(StudentRegisterRoom studentRegisterRoom) {
        return studentRegisterRoomRepository.save(studentRegisterRoom);
    }

    private void updateRemainQuantityRegisterRoom(Integer idRoom) {
        Optional<Room> room = roomService.findRoomByIdRoom(idRoom);
        if (room.isEmpty() || room.get().getRemainAmountRegister() <= Constants.QUANTITY_REMAIN_AMOUNT_REGISTER){
            throw new ValidParametersException();
        }
    }

    private StudentRegisterRoom constructionStudentRegisterRoom(DataStudentRegisterRoomDto dataStudentRegisterRoomDto) {
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long timeCurrent = new Date().getTime();
        StudentRegisterRoom studentRegisterRoom = new StudentRegisterRoom();
        studentRegisterRoom.setIdUser(ktxUser.getIdKtxUser());
        studentRegisterRoom.setIdRoom(dataStudentRegisterRoomDto.getIdRoom());
        studentRegisterRoom.setIdTimeHired(dataStudentRegisterRoomDto.getIdTimeHired());
        studentRegisterRoom.setStatus(Constants.STATUS_HOLD_STUDENT_ROOM_REGISTER);
        studentRegisterRoom.setTimeCreated(timeCurrent);
        studentRegisterRoom.setTimeModified(timeCurrent);
        studentRegisterRoom.setIdUserModified(ktxUser.getIdKtxUser());
        studentRegisterRoom.setIdUserCreated(ktxUser.getIdUserCreated());
        studentRegisterRoom.setIdBatchesRegistration(dataStudentRegisterRoomDto.getIdBatchesRegister());
        studentRegisterRoom.setExpiresAt(timeCurrent + Long.parseLong(PropertiesUtil.getProperty("time-holding.register-room")));
        return studentRegisterRoom;
    }

    private void verifyCreateStudentRegisterRoom(CreateRegisterRoomRequest request) {
        if (StringUtils.isBlank(request.getCodeRoom())){
            throw new ValidParametersException();
        }
        if (!studentRegisterRoomRepository.isAllowRegisterBatchesRegistration()){
            throw new ValidParametersException();
        }
        if (studentRegisterRoomRepository.isExistsRegisteredRoomInBatchesRegistrationCurrent()){
            throw new ValidParametersException();
        }
        if (!studentRegisterRoomRepository.isAllowRegisterRoomByCodeRoom(request.getCodeRoom())){
            throw new ValidParametersException();
        }
    }

    private StudentRegisterRoomResponse convertToStudentRegisterRoomResponse(StudentRegisterRoomDto dto) {
        StudentRegisterRoomResponse response = new StudentRegisterRoomResponse();
        response.setIdStudentRegisterRoom(dto.getIdStudentRegisterRoom());
        response.setCodeRoom(dto.getCodeRoom());
        response.setTitleRoom(dto.getTitleRoom());
        response.setCodeDepartment(dto.getCodeDepartment());
        response.setTitleDepartment(dto.getTitleDepartment());
        response.setTimeCreated(dto.getTimeCreated());
        response.setTimeHiredStarted(dto.getTimeHiredStarted());
        response.setTimeHiredEnded(dto.getTimeHiredEnded());
        response.setPrice(dto.getPrice());
        response.setStatusStudentRegisterRoom(dto.getStatusStudentRegisterRoom());
        response.setExpiresAt(dto.getExpiresAt());
        return response;
    }

    @Transactional
    @Override
    public void approvedStudentRegisterHiredRoom(ApprovedUserRegisterRoomRequest request){
        if (null == request.getCodeUserRegister() || null == request.getStatus()) {
            throw new ValidParametersException();
        }
        if (!(request.getStatus().equals(Constants.STUDENT_REGISTER_ROOM_STATUS_ACCEPT) ||
                request.getStatus().equals(Constants.STUDENT_REGISTER_ROOM_STATUS_NOT_ACCEPT))){
            throw new ValidParametersException();
        }
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        approvedStudentRegister(request,ktxUser);
    }


    public void approvedStudentRegister(ApprovedUserRegisterRoomRequest request,KtxUser ktxUser){
        StudentRegisterRoom studentRegisterRoom =  changeApprovedStudent(request,ktxUser.getIdKtxUser());
//        AcceptStudentRegisterRoomDto acceptStudentRegisterRoomDto = studentRegisterRoomService.getAcceptStudentRegisterRoomDtoById(studentRegisterRoom.ge());
//        acceptStudentRegisterRoomDto.setStatusAccept(request.getStatus());
        if (request.getStatus().equals(Constants.STUDENT_REGISTER_ROOM_STATUS_NOT_ACCEPT)) {
            roomService.updateQuantityAndRemainAmountCancelRegisterRoom(studentRegisterRoom.getIdRoom(),
                    Constants.QUANTITY_UPDATE_ROOM_AND_REGISTER,
                    ktxUser.getIdKtxUser());
        }
        if (request.getStatus().equals(Constants.STUDENT_REGISTER_ROOM_STATUS_ACCEPT)){
            Optional<Room> roomOptional = roomService.findRoomByIdRoom(studentRegisterRoom.getIdRoom());
            if(roomOptional.get().getRemainAmount() > Constants.QUANTITY_REMAIN_AMOUNT_REGISTER) {
                roomService.updateQuantityAndRemainAmountAcceptRegisterAndHiredRoom(studentRegisterRoom.getIdRoom(),
                        Constants.QUANTITY_UPDATE_ROOM_AND_REGISTER,
                        ktxUser.getIdKtxUser());
            }

            transformStudentToStudentHiredRoom(studentRegisterRoom,ktxUser.getCodeUser());
//            EmailUtil.getInstance().sendApprovedRoom(acceptStudentRegisterRoomDto);
        }
    }


    private StudentRegisterRoom changeApprovedStudent(ApprovedUserRegisterRoomRequest request, Integer userId){
        StudentRegisterRoom studentRegisterRoom = findStudentRegisterRoomByCode(request.getCodeUserRegister()).get();
        Date timeNow = new Date();
        studentRegisterRoom.setStatus(request.getStatus());
        studentRegisterRoom.setTimeModified(timeNow.getTime());
        studentRegisterRoom.setIdUserModified(userId);
        saveInfoApprovedStudentRegisterRoom(studentRegisterRoom);
        return studentRegisterRoom;
    }

    private void transformStudentToStudentHiredRoom(StudentRegisterRoom studentRegisterRoom,String userId ) {
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long timeCurrently = new Date().getTime();
        StudentRoom studentRoom = new StudentRoom();
        studentRoom.setIdRoom(studentRegisterRoom.getIdRoom());
        studentRoom.setIdUser(studentRegisterRoom.getIdUser());
        studentRoom.setIdTimeHired(studentRegisterRoom.getIdTimeHired());
        studentRoom.setStatus(Constants.STATUS_STUDENT_HIRING_ROOM);
        studentRoom.setIdUserCreated(ktxUser.getIdKtxUser());
        studentRoom.setIdUserModified(ktxUser.getIdKtxUser());
        studentRoom.setTimeCreated(timeCurrently);
        studentRoom.setTimeModified(timeCurrently);
        studentRoomService.saveStudentRoom(studentRoom);
    }
}
