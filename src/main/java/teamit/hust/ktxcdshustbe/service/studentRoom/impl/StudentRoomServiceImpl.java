package teamit.hust.ktxcdshustbe.service.studentRoom.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamit.hust.ktxcdshustbe.dto.studentRoom.FindAllStudentHiredRoomDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.Room;
import teamit.hust.ktxcdshustbe.entity.StudentRegisterRoom;
import teamit.hust.ktxcdshustbe.entity.StudentRoom;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
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
import teamit.hust.ktxcdshustbe.utility.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Log4j2
@Service
public class StudentRoomServiceImpl implements StudentRoomService {

    @Autowired
    StudentRoomRepository studentRoomRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    StudentRegisterRoomService studentRegisterRoomService;
    @Autowired
    KtxUserService ktxUserService;
    @Autowired
    RoomService roomService;

    private static final String SEPARATOR = File.separator;

    @Override
    public void saveStudentRoom(StudentRoom room) {
        studentRoomRepository.save(room);
    }


    @Override
    public List<HiredRoomsResponse> getRoomsHiredByUser(String codeUser) throws ValidateFiledException {
        validateListRoomHiredDetailRequest(codeUser);
        return studentRoomRepository.getRoomsHiredByUser(codeUser);
    }


    @Override
    public Page<ListHiredRoomStudentResponse> getListHiredRoomStudentResponse(OidcUser principal, StudentListRoomHiredRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        return studentRoomRepository.getListHiredRoomStudentResponse(request,pageable);
    }

    @Override
    public Page<ListStudentHiredRoomResponse> getListStudentHiredRoomResponse(ListStudentHiredRoomRequest request) {
//        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        request.setListDepartmentOriginal(ktxUser.getListDepartmentCurrent());
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
            response.setValueUser(findAllStudentHiredRoomDto.getValueUser());
            response.setTimeHired(findAllStudentHiredRoomDto.getTimeHired());
            response.setCodeDepartment(findAllStudentHiredRoomDto.getCodeDepartment());
            response.setTitleDepartment(findAllStudentHiredRoomDto.getTitleDepartment());
            response.setCodeRoom(findAllStudentHiredRoomDto.getCodeRoom());
            response.setTitleRoom(findAllStudentHiredRoomDto.getTitleRoom());
            response.setCodeUserModified(findAllStudentHiredRoomDto.getCodeUserModified());
            response.setValueUserModified(findAllStudentHiredRoomDto.getValueUserModified());
            response.setStatus(findAllStudentHiredRoomDto.getStatus());
            response.setSex(findAllStudentHiredRoomDto.getSex());
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
            throw new ValidateFiledException();
        }
        validateStudentHiringRoom(response.get().getCodeUser());
        validateStudentRegisterRoom(response.get().getCodeUser());
        return response.get();
    }

    @Transactional
    @Override
    public void addStudentToRoom(StudentToRoomRequest request) throws Exception {
        validateStudentToRoom(request);
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Room> roomOptional = roomService.findRoomByCodeRoom(request.getCodeRoom());
        KtxUser ktxUserOptional = ktxUserService.findKtxUserByCodeUser(request.getCodeUser());
        if(studentRegisterRoomService.checkExistStudentHoldInRegister(request.getCodeUser())){
            throw new ExitsObjectException();
        }
        if(studentRegisterRoomService.checkExistStudentPendingInRegister(request.getCodeRoom())){
            throw new ExitsObjectException();
        }
        else {
            updateQuantityRoom(roomOptional.get(), ktxUser.getIdKtxUser());
            studentRoomRepository.save(createStudentRoomNew(roomOptional.get().getIdRoom(),ktxUserOptional.getIdKtxUser(), ktxUser.getIdKtxUser(),request.getIdTimeHired()));

        }

    }



    @Transactional
    @Override
    public void removeStudentRoom(RemoveStudentInRoomRequest request) throws Exception {
        validateRemoveStudentRoom(request);
        Optional<StudentRoom> studentRoom = studentRoomRepository.findStudentHiringRoomByCodeUser(request.getCodeUser());
        if (studentRoom.isEmpty()) {
            throw new NotFoundException();
        }
//        studentRoomRepository.delete(studentRoom.get());

//        if(studentRegisterRoomService.checkExistStudentInRegister(request.getCodeUser(),studentRoom.get().getIdRoom())){
//            updateRemainQuantityRoomRemoveStudentWithRegister(request.getCodeRoom());
//        }
//        else {
//
//        }
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Room> roomOptional = roomService.findRoomByCodeRoom(request.getCodeRoom());
        if(studentRegisterRoomService.checkExistStudentPendingInRegister(request.getCodeRoom())){
            throw new ExitsObjectException();
        }
        else{
            if(studentRegisterRoomService.checkExistStudentInRegister(request.getCodeUser(),roomOptional.get().getIdRoom())
                    && studentRegisterRoomService.checkExistStudentRemoveInRegister(request.getCodeUser(),roomOptional.get().getIdRoom()) ) {
                updateOriginalRoomWithRegistered(roomOptional.get(),ktxUser.getIdKtxUser());
                updateInfoStudentRegisterRoomWhenRemove(roomOptional.get(),request.getCodeUser(),ktxUser.getIdKtxUser());
            }
            else {
                updateOriginalRoomWithOutRegistered(roomOptional.get(),ktxUser.getIdKtxUser());
            }
        }
        studentRoom.get().setStatus(Constants.STATUS_STUDENT_REFUND_ROOM);
        studentRoomRepository.save(studentRoom.get());
    }

    private void updateInfoStudentRegisterRoomWhenRemove(Room room, String codeUser, Integer idKtxUser) {

        StudentRegisterRoom studentRegisterRoomOriginal = studentRegisterRoomService.findStudentRegisterRoomByCodeUserAndRoomAndStatus(codeUser,room.getIdRoom(),Constants.STUDENT_REGISTER_ROOM_STATUS_ACCEPT);
        createStudentRegisterRoom(studentRegisterRoomOriginal.getIdTimeHired() ,room.getIdRoom(),codeUser,idKtxUser,Constants.STATUS_REMOVE_STUDENT_ROOM_REGISTER);

    }

    private void updateRemainQuantityRoomRemoveStudentWithRegister(String codeRoom) throws SQLException {
        KtxUser customUserDetails = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int rowUpdate = roomService.updateRemainQuantityRoomWhenToRemoveStudent(codeRoom, customUserDetails.getIdKtxUser());
        if (rowUpdate == Constants.ROW_NOT_UPDATED){
            throw new SQLException("Method remove new student can't update quantity remain amount!");
        }
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

    private void updateQuantityRoom(Room room, Integer userIdModified) throws SQLException {
        if((room.getRemainAmount() > room.getRemainAmountRegister()) ){
            int rowUpdateWhenHiredLargerRegister = roomRepository.updateQuantityAndRemainAmountToAddNewStudentWhenHiredLargerRegister(room.getIdRoom(), userIdModified);
            if (rowUpdateWhenHiredLargerRegister == Constants.ROW_NOT_UPDATED){
                throw new SQLException("Method add new student can't update quantity remain amount!");
            }
        }
        else {
            int rowUpdate = roomRepository.updateQuantityAndRemainAmountToAddNewStudent(room.getIdRoom(), userIdModified);
            if (rowUpdate == Constants.ROW_NOT_UPDATED){
                throw new SQLException("Method add new student can't update quantity remain amount!");
            }
        }

    }

    private StudentRoom createStudentRoomNew(Integer idRoom, Integer idUser, Integer userIdModified,Integer idTimeHired) {
        StudentRoom studentRoom = new StudentRoom();
        studentRoom.setIdUser(idUser);
        studentRoom.setIdRoom(idRoom);
        var timeCurrent = Long.valueOf(new Date().getTime());
        studentRoom.setTimeCreated(timeCurrent);
        studentRoom.setTimeModified(timeCurrent);
        studentRoom.setIdTimeHired(idTimeHired);
        studentRoom.setIdUserCreated(userIdModified);
        studentRoom.setIdUserModified(userIdModified);
        studentRoom.setStatus(Constants.STATUS_STUDENT_HIRING_ROOM);
        return studentRoom;
    }

    private void validateStudentToRoom(StudentToRoomRequest request) {
        KtxUser ktxUser = ktxUserService.findKtxUserByCodeUser(request.getCodeUser());
        if (ktxUser == null) {
            throw new NotFoundException();
        }
        Optional<Room> room = roomService.findRoomByCodeRoom(request.getCodeRoom());
        if (room.isEmpty()) {
            throw new NotFoundException();
        }
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

    @Transactional
    @Lazy
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
        if ((destinationRoom.get().getQuantityHired() < destinationRoom.get().getLimitAmountPeople()) && (destinationRoom.get().getRemainAmount() > 0) ){
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
        if(studentRegisterRoomService.checkExistStudentPendingInRegister(destinationRoom.getCodeRoom()) ||  studentRegisterRoomService.checkExistStudentPendingInRegister(originalRoom.getCodeRoom()) ){
            throw new ExitsObjectException();
        }
        else{
            // kiểm tra xem sinh viên đã đăng ký online k ?
            if(studentRegisterRoomService.checkExistStudentInRegister(codeUser,originalRoom.getIdRoom()) && studentRegisterRoomService.checkExistStudentRemoveInRegister(codeUser,originalRoom.getIdRoom()) ) {
                updateOriginalRoomWithRegistered(originalRoom,ktxUser.getIdKtxUser());
                updateDestinationRoomWithRegistered(destinationRoom,ktxUser.getIdKtxUser());

                updateInfoStudentRegisterRoom(originalRoom,destinationRoom,codeUser,ktxUser.getIdKtxUser());
            }


            else {
                updateOriginalRoomWithOutRegistered(originalRoom,ktxUser.getIdKtxUser());
                updateDestinationRoomWithOutRegistered(destinationRoom,ktxUser.getIdKtxUser());

            }
            updateStudentRoom(codeUser, originalRoom.getIdRoom(),destinationRoom.getIdRoom(),ktxUser.getIdKtxUser());
        }

    }

    private void updateInfoStudentRegisterRoom(Room originalRoom, Room destinationRoom,String codeUser, Integer idKtxUser) {
        StudentRegisterRoom studentRegisterRoomOriginal = studentRegisterRoomService.findStudentRegisterRoomByCodeUserAndRoomAndStatus(codeUser,originalRoom.getIdRoom(),Constants.STUDENT_REGISTER_ROOM_STATUS_ACCEPT);
        studentRegisterRoomOriginal.setStatus(Constants.STATUS_TRANSFER_STUDENT_ROOM_REGISTER);
        studentRegisterRoomOriginal.setIdUserModified(idKtxUser);
        studentRegisterRoomOriginal.setTimeModified(new Date().getTime());
        studentRegisterRoomService.saveStudentRoomRegisterRoom(studentRegisterRoomOriginal);

        createStudentRegisterRoom(studentRegisterRoomOriginal.getIdTimeHired() ,destinationRoom.getIdRoom(),codeUser,idKtxUser,Constants.STUDENT_REGISTER_ROOM_STATUS_ACCEPT);


    }

    private void createStudentRegisterRoom(Integer idTimeHired, Integer idRoom, String codeUser, Integer idKtxUser,Integer status) {
        KtxUser ktxUser = ktxUserService.findKtxUserByCodeUser(codeUser);
        StudentRegisterRoom studentRegisterRoom = new StudentRegisterRoom();
        studentRegisterRoom.setIdRoom(idRoom);
        studentRegisterRoom.setIdUser(ktxUser.getIdKtxUser());
        studentRegisterRoom.setIdTimeHired(idTimeHired);
        studentRegisterRoom.setIdUserCreated(idKtxUser);
        studentRegisterRoom.setIdUserModified(idKtxUser);
        studentRegisterRoom.setTimeCreated(new Date().getTime());
        studentRegisterRoom.setTimeModified(new Date().getTime());
        studentRegisterRoom.setStatus(status);
        studentRegisterRoomService.saveStudentRoomRegisterRoom(studentRegisterRoom);

    }


    private void updateDestinationRoomWithOutRegistered(Room destinationRoom, Integer idKtxUser) {
        if((destinationRoom.getRemainAmount() > destinationRoom.getRemainAmountRegister()) ){
            destinationRoom.setRemainAmount(destinationRoom.getRemainAmount() - Constants.QUANTITY_UPDATE_HIRED_ROOM);
            destinationRoom.setQuantityHired(destinationRoom.getQuantityHired() + Constants.QUANTITY_UPDATE_HIRED_ROOM);

        } else  {
            destinationRoom.setRemainAmount(destinationRoom.getRemainAmount() - Constants.QUANTITY_UPDATE_HIRED_ROOM);
            destinationRoom.setQuantityHired(destinationRoom.getQuantityHired() + Constants.QUANTITY_UPDATE_HIRED_ROOM);
            destinationRoom.setLimitAmountPeopleRegister(destinationRoom.getLimitAmountPeopleRegister() - Constants.QUANTITY_UPDATE_HIRED_ROOM);
            destinationRoom.setRemainAmountRegister(destinationRoom.getRemainAmountRegister() - Constants.QUANTITY_UPDATE_HIRED_ROOM);
        }

        destinationRoom.setTimeModified(new Date().getTime());
        destinationRoom.setIdUserModified(idKtxUser);
        roomRepository.save(destinationRoom);
    }

    private void updateDestinationRoomWithRegistered(Room destinationRoom, Integer idKtxUser) {

        if((destinationRoom.getLimitAmountPeople() > destinationRoom.getLimitAmountPeopleRegister()) && destinationRoom.getRemainAmountRegister().equals(Constants.QUANTITY_REMAIN_AMOUNT_REGISTER) ){

            destinationRoom.setRemainAmount(destinationRoom.getRemainAmount() - Constants.QUANTITY_UPDATE_HIRED_ROOM);
            destinationRoom.setQuantityHired(destinationRoom.getQuantityHired() + Constants.QUANTITY_UPDATE_HIRED_ROOM);

            destinationRoom.setLimitAmountPeopleRegister(destinationRoom.getLimitAmountPeopleRegister() + Constants.QUANTITY_UPDATE_HIRED_ROOM);
            destinationRoom.setQuantityRegistered(destinationRoom.getQuantityRegistered() + Constants.QUANTITY_UPDATE_HIRED_ROOM);
            destinationRoom.setTimeModified(new Date().getTime());
            destinationRoom.setIdUserModified(idKtxUser);
            roomRepository.save(destinationRoom);
        }

        else if((destinationRoom.getRemainAmountRegister() > Constants.QUANTITY_REMAIN_AMOUNT_REGISTER)){

            destinationRoom.setRemainAmount(destinationRoom.getRemainAmount() - Constants.QUANTITY_UPDATE_HIRED_ROOM);
            destinationRoom.setQuantityHired(destinationRoom.getQuantityHired() + Constants.QUANTITY_UPDATE_HIRED_ROOM);
            destinationRoom.setQuantityRegistered(destinationRoom.getQuantityRegistered() + Constants.QUANTITY_UPDATE_HIRED_ROOM);
            destinationRoom.setRemainAmountRegister(destinationRoom.getRemainAmountRegister() - Constants.QUANTITY_UPDATE_HIRED_ROOM);

            destinationRoom.setTimeModified(new Date().getTime());
            destinationRoom.setIdUserModified(idKtxUser);
            roomRepository.save(destinationRoom);
        }
        else {
            throw new ValidParametersException();
        }



    }

    private void updateOriginalRoomWithOutRegistered(Room originalRoom, Integer idKtxUser) {
        if(originalRoom.getQuantityHired() > 0 ){
            originalRoom.setQuantityHired(originalRoom.getQuantityHired() - Constants.QUANTITY_UPDATE_HIRED_ROOM);
            originalRoom.setRemainAmount(originalRoom.getRemainAmount() + Constants.QUANTITY_UPDATE_HIRED_ROOM);

            originalRoom.setTimeModified(new Date().getTime());
            originalRoom.setIdUserModified(idKtxUser);
        }
        else {
            throw new ValidParametersException();
        }
        roomRepository.save(originalRoom);
    }

    private void updateOriginalRoomWithRegistered(Room originalRoom, Integer idKtxUser) {

        originalRoom.setQuantityHired(originalRoom.getQuantityHired() - Constants.QUANTITY_UPDATE_HIRED_ROOM);
        originalRoom.setRemainAmount(originalRoom.getRemainAmount() + Constants.QUANTITY_UPDATE_HIRED_ROOM);


        originalRoom.setRemainAmountRegister(originalRoom.getRemainAmountRegister()  + Constants.QUANTITY_UPDATE_HIRED_ROOM);
        originalRoom.setQuantityRegistered(originalRoom.getQuantityRegistered() - Constants.QUANTITY_UPDATE_HIRED_ROOM);


        originalRoom.setTimeModified(new Date().getTime());
        originalRoom.setIdUserModified(idKtxUser);
        roomRepository.save(originalRoom);
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
            destinationRoom.setRemainAmountRegister(Constants.DEFAULT_QUANTITY_HIRED);
            destinationRoom.setTimeModified(new Date().getTime());
            destinationRoom.setIdUserModified(userIdModified);
        } else {
            destinationRoom.setQuantityHired(destinationRoom.getQuantityHired() + Constants.QUANTITY_UPDATE_HIRED_ROOM);
            destinationRoom.setRemainAmount(destinationRoom.getRemainAmount() - Constants.QUANTITY_UPDATE_HIRED_ROOM);
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
        originalRoom.setRemainAmount(originalRoom.getRemainAmount() + Constants.QUANTITY_UPDATE_HIRED_ROOM);
        originalRoom.setRemainAmountRegister(originalRoom.getRemainAmountRegister()  + Constants.QUANTITY_UPDATE_HIRED_ROOM);
        originalRoom.setQuantityRegistered(originalRoom.getQuantityRegistered() - Constants.QUANTITY_UPDATE_HIRED_ROOM);
        originalRoom.setTimeModified(new Date().getTime());
        originalRoom.setIdUserModified(userIdModified);
        roomRepository.save(originalRoom);
    }

    @Override
    public String downloadStudentHiredRoomList(ListStudentHiredRoomRequest request) throws IOException {
        String fileExcel = PropertiesUtil.getProperty("hust.ktx.static.location.resources.static")
                + SEPARATOR
                + FileUtil.FOLDER_HIRED_ROOM
                + SEPARATOR
                + "Template_List_Student_Hired_Room.xlsx";
        List<FindAllStudentHiredRoomDto> studentList = studentRoomRepository.findAllStudentsForExport(request);

        try (FileInputStream fileInputStream = new FileInputStream(new File(fileExcel));
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Map<String, CellStyle> styles = createStyles(workbook);

            Sheet sheet = workbook.getSheetAt(0);

//            writeDataInfoReport(sheet, styles);
            writeDataToStudentHiredRoomReport(sheet, studentList, styles);


            String root = PropertiesUtil.getProperty("hust.ktx.static.location.tomcat.webapp.ktx-be");
            String folder = root + FileUtil.SEPARATOR
                    + FileUtil.FOLDER_HIRED_ROOM
                    + FileUtil.SEPARATOR
                    + FileUtil.getFolderInfo();
            FileUtil.createFolder(folder);
            String fileFinal = folder + FileUtil.SEPARATOR + "Sample_List_Student_Hired_Room" + new Date().getTime() + ".xlsx";
            File filePathOutput = new File(fileFinal);
            if (!filePathOutput.exists()) {
                if (filePathOutput.createNewFile()) {
                    log.info("Create file success!");
                }
            }
            String fileReturn = fileFinal.replace(root, PropertiesUtil.getProperty("hust.ktx.static.location.static.files"));
            log.info("File return: " + fileReturn);
            FileOutputStream fileOut = new FileOutputStream(filePathOutput);
            workbook.write(fileOut);
            workbook.close();
            return fileReturn;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error during Excel file generation: " + e.getMessage(), e);
        }
    }


    private void writeDataInfoReport(Sheet sheet, Map<String, CellStyle> styles) {
        String reportTitle = "DANH SÁCH SINH VIÊN THUÊ PHÒNG";
        String dateExport = "Ngày xuất báo cáo: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        writeValueCell(sheet, 0, 0, reportTitle, styles.get("header"));
        writeValueCell(sheet, 1, 0, dateExport, styles.get("normal"));
    }

    private void writeDataToStudentHiredRoomReport(Sheet sheet, List<FindAllStudentHiredRoomDto> studentList, Map<String, CellStyle> styles) throws JsonProcessingException {
        int rowStart = 6;
        if (studentList.isEmpty()) {
            return;
        }
//        int shiftSize = studentList.size();
//        if (sheet.getLastRowNum() >= rowStart) {
//            sheet.shiftRows(rowStart, sheet.getLastRowNum(), shiftSize, true, true);
//        }
        ObjectMapper objectMapper = new ObjectMapper();
        int stt = 1;
        for (FindAllStudentHiredRoomDto student : studentList) {
            Row row = sheet.createRow(rowStart);
            writeValueCell(row, 0, String.valueOf(stt), styles.get("normal"));
            HashMap<String, Object> dataStudent = objectMapper.readValue(
                    student.getValueUser() == null ? "{}" : student.getValueUser(),
                    new TypeReference<>() {}
            );

            writeValueCell(row, 1, ValueUtil.getStringByObject(dataStudent.get("full_name")), styles.get("normal"));
            writeValueCell(row, 2, ValueUtil.getStringByObject(dataStudent.get("number_student")), styles.get("normal"));
            writeValueCell(row, 3, ValueUtil.getStringByObject(dataStudent.get("number_phone")), styles.get("normal"));
            writeValueCell(row, 4, ValueUtil.getStringByObject(student.getTimeHired()), styles.get("normal"));
            writeValueCell(row, 5, ValueUtil.getStringByObject(student.getTitleDepartment()), styles.get("normal"));
            writeValueCell(row, 6, ValueUtil.getStringByObject(student.getTitleRoom()), styles.get("normal"));

            Integer status = student.getStatus();
            String statusText = (status != null && status == 1) ? "Đang thuê" : "Đã trả phòng";
            writeValueCell(row, 7, statusText, styles.get("normal"));

            rowStart++;
            stt++;
        }
    }

    private String createFileExportPath() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "DanhSachSinhVienThuePhong_" + timestamp + ".xlsx";

        return PropertiesUtil.getProperty("hust.ktx.static.location.tomcat.webapp.ktxbe")
                + SEPARATOR
                + FileUtil.FOLDER_NAME_REPORT
                + SEPARATOR
                + fileName;
    }

    private void writeValueCell(Sheet sheet, int rowIndex, int colIndex, String content, CellStyle style) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(content);
        cell.setCellStyle(style);
    }
    private Map<String, CellStyle> createStyles(Workbook workbook) {
        Map<String, CellStyle> styles = new HashMap<>();

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        styles.put("header", headerStyle);

        CellStyle normalStyle = workbook.createCellStyle();
        Font normalFont = workbook.createFont();
        normalFont.setBold(false);
        normalStyle.setFont(normalFont);
        normalStyle.setBorderBottom(BorderStyle.THIN);
        normalStyle.setBorderTop(BorderStyle.THIN);
        normalStyle.setBorderLeft(BorderStyle.THIN);
        normalStyle.setBorderRight(BorderStyle.THIN);
        styles.put("normal", normalStyle);

        CellStyle centeredStyle = workbook.createCellStyle();
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);
        centeredStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        styles.put("centered", centeredStyle);

        CellStyle noneStyle = workbook.createCellStyle();
        Font noneFont = workbook.createFont();
        noneFont.setBold(false);
        noneStyle.setFont(noneFont);
        styles.put("none", noneStyle);
        return styles;
    }

    private void writeValueCell(Row row, int colIndex, String content, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(content);
        cell.setCellStyle(style);
    }

    private String createFileExportInventoryReport() {
        String root = PropertiesUtil.getProperty("hust.ktx.static.location.tomcat.webapp.csvcbe");
        String folder = root + SEPARATOR + FileUtil.FOLDER_NAME_REPORT + SEPARATOR + FileUtil.getFolderInfo();
        FileUtil.createFolder(folder);
        return folder + SEPARATOR + "Student_Hired_Room_Report_" + new Date().getTime() + "." + ExcelUtil.FILE_EXCEL[1];
    }
}
