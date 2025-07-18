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
import teamit.hust.ktxcdshustbe.dto.registerRoom.AcceptStudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.user.UserRegisterRoomDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.Room;
import teamit.hust.ktxcdshustbe.entity.StudentRegisterRoom;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.studentRegisterRoom.StudentRegisterRoomRepository;
import teamit.hust.ktxcdshustbe.repository.user.KtxUserRepository;
import teamit.hust.ktxcdshustbe.request.registerRoom.ChangeRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.registerRoom.DeclareInformationRequest;
import teamit.hust.ktxcdshustbe.request.studentRegister.AcceptPaymentRequest;
import teamit.hust.ktxcdshustbe.request.studentRegister.CreateRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.ApprovedUserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.UserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.registerRoom.StudentRegisterRoomDetailResponse;
import teamit.hust.ktxcdshustbe.response.studentRegister.StudentRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.response.user.UserRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.service.room.RoomService;
import teamit.hust.ktxcdshustbe.service.studentRegisterRoom.StudentRegisterRoomService;
import teamit.hust.ktxcdshustbe.service.studentRoom.StudentRoomService;
import teamit.hust.ktxcdshustbe.service.timeHired.TimeHiredService;
import teamit.hust.ktxcdshustbe.service.user.KtxUserService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class StudentRegisterRoomServiceImpl implements StudentRegisterRoomService {

    @Autowired
    StudentRegisterRoomRepository studentRegisterRoomRepository;
    @Lazy
    @Autowired
    KtxUserRepository ktxUserRepository;
    @Lazy
    @Autowired
    RoomService roomService;
    @Lazy
    @Autowired
    StudentRoomService studentRoomService;
    @Autowired
    TimeHiredService timeHiredService;
    @Lazy
    @Autowired
    KtxUserService ktxUserService;
    @Override
    public Optional<StudentRegisterRoom> findStudentRegisterRoomById(Integer idStudentRegister) {
        Optional<StudentRegisterRoom> studentRegisterRoom = studentRegisterRoomRepository.findById(idStudentRegister);
        if (studentRegisterRoom.isEmpty()){
            throw new NotFoundException();
        }
        return studentRegisterRoom;
    }

    @Override
    public StudentRegisterRoomDetailResponse findStudentRegisterRoomDetailByRegisterId(Integer registerId) {
        return studentRegisterRoomRepository.getStudentDetailRegisterRoomByRegisterId(registerId);
    }

    @Override
    public void approvedStudentRegisterRoom(StudentRegisterRoom room) {
        studentRegisterRoomRepository.save(room);
    }

    @Transactional
    @Override
    public void changeRegisterRoomStudent(ChangeRegisterRoomRequest request) {

        Optional<StudentRegisterRoom> studentRegisterRoom = studentRegisterRoomRepository.findStudentRegisterRoomById(request.getStudentRegisterRoomId());
        if (studentRegisterRoom.isEmpty()){
            throw new NotFoundException();
        }
//        validateChangeRegisterRoom(request,studentRegisterRoom.get());
//        updateQuantityRegisterOriginRoom(studentRegisterRoom.get().getRoomId());
//        updateQuantityRegisterDestinationRoom(request.getDestinationRoomId());
//        studentRegisterRoom.get().setRoomId(request.getDestinationRoomId());
//        log.debug("Student " + studentRegisterRoom.get().getUserId() + " changed room " + studentRegisterRoom.get().getRoomId() + " success!") ;
        studentRegisterRoomRepository.save(studentRegisterRoom.get());
    }

//    private void validateChangeRegisterRoom(ChangeRegisterRoomRequest request, StudentRegisterRoom registerRoom) throws Exception {
//        if (Objects.isNull(request.getDestinationRoomId())){
//            throw new Exception("Validate data request!");
//        }
//        Optional<StudentRegisterRoom> studentRegisterRoom = studentRegisterRoomRepository.findByUserIdAndTimeIdHired(registerRoom.getUserId(), registerRoom.getTimeIdHired());
//        if (studentRegisterRoom.isPresent() && studentRegisterRoom.get().getRoomId().equals(request.getDestinationRoomId())) {
//            throw new Exception("You must to change new room!");
//        }
//        if (registerRoom.getStatus().equals(Constants.STATUS_SUCCESS_PAYMENT_STUDENT_ROOM_REGISTER)) {
//            throw new Exception("Paid, room cannot be changed!");
//        }
//        roomService.findRoomByCodeRoom(request.getCodeRoom());
//        timeHiredService.findTimeHiredById(registerRoom.getIdTimeHired());
//    }

    private void updateQuantityRegisterDestinationRoom(Integer destinationRoomId) {
        roomService.updateQuantityStudentRegisterDestinationRoom(destinationRoomId);
    }

    private void updateQuantityRegisterOriginRoom(Integer originRoomId){
        roomService.updateQuantityRegisterOriginRoom(originRoomId);
    }



    @Override
    public StudentRegisterRoom createStudentRegisterRoom(CreateRegisterRoomRequest request, OidcUser principal){
        KtxUser userDetails = (KtxUser) principal.getUserInfo().getClaim(Constants.CLAIMS_INFORMATION_USER);
        validateCreateRegisterRoom(request, userDetails.getIdKtxUser());
        updateQuantityRegisterRoom(request.getCodeRoom());
        updateStatusRegisterRoomUser(userDetails.getIdKtxUser(), Constants.STATUS_USER_REGISTER_ROOM);
        log.info("Student " + userDetails.getUsername() + " register room " + request.getCodeRoom() + " success!"); ;
        return saveStudentRegisterRoom(createNewStudentRegisterRoom(request, userDetails.getIdKtxUser()));
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
    public Optional<StudentRegisterRoomResponse> findStudentRegisterRoomResponseByUserId(Integer userId) {
        return studentRegisterRoomRepository.findStudentRegisterRoomByUserId(userId);
    }

    @Override
    public AcceptStudentRegisterRoomDto getAcceptStudentRegisterRoomDtoById(Integer studentRegisterRoomId) {
        return studentRegisterRoomRepository.getAcceptStudentRegisterRoomDtoById(studentRegisterRoomId);
    }


    @Override
    public void declareInformationStudent(OidcUser principal, DeclareInformationRequest request) {
        KtxUser ktxUser = (KtxUser) principal.getUserInfo().getClaim(Constants.CLAIMS_INFORMATION_USER);
        log.info("Start declare information ", ktxUser.getUsername());
        setDeclareInformationStudent(ktxUser, request);
        ktxUserService.save(ktxUser);
        log.info("End declare information ", ktxUser.getUsername());
//        validateDeclareInformationStudent(request);
    }

    private void setDeclareInformationStudent(KtxUser customUserDetails,DeclareInformationRequest request) {
        customUserDetails.setNameFather(request.getNameFather());
        customUserDetails.setYearFather(request.getYearFather());
        customUserDetails.setPhoneNumberFather(request.getPhoneNumberFather());
        customUserDetails.setAddressFather(request.getAddressFather());
        customUserDetails.setNameMother(request.getNameMother());
        customUserDetails.setYearMother(request.getYearMother());
        customUserDetails.setPhoneNumberMother(request.getPhoneNumberMother());
        customUserDetails.setAddressMother(request.getAddressMother());
        customUserDetails.setAddressContact(request.getAddressContact());
        customUserDetails.setAddress(request.getAddress());
        customUserDetails.setNation(request.getNation());
    }

    @Override
    public void declareInformationStudentV2(OidcUser principal, DeclareInformationRequest request) {
        KtxUser customUserDetails = (KtxUser) principal.getUserInfo().getClaim(Constants.CLAIMS_INFORMATION_USER);
        log.info("Start declare information ", customUserDetails.getUsername());
        setDeclareInformationStudentV2(customUserDetails, request);
        ktxUserService.save(customUserDetails);
        log.info("End declare information ", customUserDetails.getUsername());
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
            response.setFullName(dto.getFullName());
            response.setNumberStudent(dto.getNumberStudent());
            response.setPhoneNumber(dto.getPhoneNumber());
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

    private void setDeclareInformationStudentV2(KtxUser customUserDetails,DeclareInformationRequest request) {
        customUserDetails.setNameFather(request.getNameFather());
        customUserDetails.setYearFather(request.getYearFather());
        customUserDetails.setPhoneNumberFather(request.getPhoneNumberFather());
        customUserDetails.setAddressFather(request.getAddressFather());
        customUserDetails.setNameMother(request.getNameMother());
        customUserDetails.setYearMother(request.getYearMother());
        customUserDetails.setPhoneNumberMother(request.getPhoneNumberMother());
        customUserDetails.setAddressMother(request.getAddressMother());
        customUserDetails.setAddressContact(request.getAddressContact());
        customUserDetails.setAddress(request.getAddress());
        customUserDetails.setNation(request.getNation());
        customUserDetails.setStatusRegisterRoom(Constants.STATUS_USER_REGISTER_ROOM);
    }

    private void updateStatusRegisterRoomUser(Integer userId, Integer statusUserRegisterRoom){
        int rowUpdates = ktxUserService.updateStatusRegisterRoom(userId, statusUserRegisterRoom);
        if (rowUpdates == Constants.ROW_NOT_UPDATED){
            log.info("Don't update status register room user id " + userId + " with status " + statusUserRegisterRoom);
            throw new ValidParametersException();
        }
    }

    private StudentRegisterRoom saveStudentRegisterRoom(StudentRegisterRoom newStudentRegisterRoom) {
        return studentRegisterRoomRepository.save(newStudentRegisterRoom);
    }

    private StudentRegisterRoom createNewStudentRegisterRoom(CreateRegisterRoomRequest request, Integer userId) {
        Timestamp dateNow = new Timestamp(new Date().getTime());
        StudentRegisterRoom studentRegisterRoom = new StudentRegisterRoom();
        Optional<Room> roomOptional = roomService.findRoomByCodeRoom(request.getCodeRoom());
        studentRegisterRoom.setIdUser(userId);
        studentRegisterRoom.setTimeCreated(Long.valueOf(dateNow.toString()));
        studentRegisterRoom.setTimeModified(Long.valueOf(dateNow.toString()));
        studentRegisterRoom.setIdRoom(roomOptional.get().getIdRoom());
        studentRegisterRoom.setIdTimeHired(request.getHiredId());
        studentRegisterRoom.setStatus(Constants.STATUS_HOLD_STUDENT_ROOM_REGISTER);
        return studentRegisterRoom;
    }

    private void updateQuantityRegisterRoom(String codeRoom){
        roomService.updateQuantityStudentRegisterRoom(codeRoom);
    }

    /**
     * Cái này cần điều chỉnh này!
     * */
    private void validateCreateRegisterRoom(CreateRegisterRoomRequest request, Integer userId){
        if (StringUtils.isNotBlank(request.getCodeRoom())){
            throw new ValidParametersException();
        }
        Optional<StudentRegisterRoom> studentRegisterRoom = studentRegisterRoomRepository.findByUserIdAndTimeIdHired(userId, request.getHiredId());
        if (studentRegisterRoom.isPresent()) {
            throw new ExitsObjectException();
        }
//        roomService.findRoomStudentByIdRoom(request.getRoomId());
//        timeHiredService.findTimeHiredById(request.getHiredId());
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
//        StudentRegisterRoom studentRegisterRoom =  changeApprovedStudent(request,ktxUser.getIdKtxUser());
//        AcceptStudentRegisterRoomDto acceptStudentRegisterRoomDto = studentRegisterRoomService.getAcceptStudentRegisterRoomDtoById(studentRegisterRoom.ge());
//        acceptStudentRegisterRoomDto.setStatusAccept(request.getStatus());
//        if (request.getStatus().equals(Constants.STUDENT_REGISTER_ROOM_STATUS_NOT_ACCEPT)) {
//            roomService.updateQuantityAndRemainAmountCancelRegisterRoom(studentRegisterRoom.getIdRoom(),
//                    Constants.QUANTITY_UPDATE_ROOM_AND_REGISTER,
//                    ktxUser.getIdKtxUser());
//        }
//        if (request.getStatus().equals(Constants.STUDENT_REGISTER_ROOM_STATUS_ACCEPT)){
//            roomService.updateQuantityAndRemainAmountAcceptRegisterAndHiredRoom(studentRegisterRoom.getIdRoom(),
//                    Constants.QUANTITY_UPDATE_ROOM_AND_REGISTER,
//                    ktxUser.getIdKtxUser());
//            transformStudentToStudentHiredRoom(studentRegisterRoom,ktxUser.getCodeUser());
//            EmailUtil.getInstance().sendApprovedRoom(acceptStudentRegisterRoomDto);
//        }
    }


//    private StudentRegisterRoom changeApprovedStudent(ApprovedUserRegisterRoomRequest request, Integer userId){
//        StudentRegisterRoom studentRegisterRoom = findStudentRegisterRoomById(request.getIdUserRegister()).get();
//        Date timeNow = new Date();
//        studentRegisterRoom.setStatus(request.getStatus());
//        studentRegisterRoom.setTimeModified(timeNow.getTime());
//        studentRegisterRoom.setIdUserModified(userId);
//        approvedStudentRegisterRoom(studentRegisterRoom);
//        return studentRegisterRoom;
//    }

//    private void transformStudentToStudentHiredRoom(StudentRegisterRoom studentRegisterRoom,String userId ) {
//        KtxUser ktxUser = ktxUserRepository.findByCustomUserDetailId(studentRegisterRoom.getUserId()).get();
//        Date timeNow = new Date();
//        studentRoomService.saveStudentRoom(StudentRoom.builder()
//                .userId(customUserDetails.getId())
//                .userName(customUserDetails.getUsername())
//                .roomId(studentRegisterRoom.getRoomId())
//                .timeCreated(new Timestamp(timeNow.getTime()))
//                .timeModified(new Timestamp(timeNow.getTime()))
//                .timeIdHired(studentRegisterRoom.getTimeIdHired())
//                .userIdModified(userId)
//                .status(Constants.STATUS_STUDENT_HIRING_ROOM)
//                .build());
//    }
}
