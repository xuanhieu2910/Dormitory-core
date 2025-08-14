package teamit.hust.ktxcdshustbe.service.studentRegisterRoom.impl;

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
import teamit.hust.ktxcdshustbe.dto.registerRoom.AcceptStudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.registerRoom.StudentRegisterHoldingRoomDto;
import teamit.hust.ktxcdshustbe.dto.registerRoom.StudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.studentRoom.DataStudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.studentRoom.FindAllStudentHiredRoomDto;
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
import teamit.hust.ktxcdshustbe.request.studentRoom.ListStudentHiredRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.ApprovedUserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.UserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.studentRegister.StatisticStudentRegisterResponse;
import teamit.hust.ktxcdshustbe.response.studentRegister.StudentRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.response.user.UserRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.service.batchesRegistration.BatchesRegistrationService;
import teamit.hust.ktxcdshustbe.service.room.RoomService;
import teamit.hust.ktxcdshustbe.service.studentRegisterRoom.StudentRegisterRoomService;
import teamit.hust.ktxcdshustbe.service.studentRoom.StudentRoomService;
import teamit.hust.ktxcdshustbe.service.user.KtxUserService;
import teamit.hust.ktxcdshustbe.utility.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    private static final String SEPARATOR = File.separator;
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
        if (!studentRegisterRoomRepository.isHoldingRegisteredRoomInBatchesRegistrationCurrent()){
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
            response.setTimeStart(dto.getTimeHiredStarted());
            response.setTimeEnd(dto.getTimeHiredEnded());
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
        if (studentRegisterRoomRepository.isExistsRegisteredRoomAndPaymentSuccessInBatchesRegistrationCurrent()){
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
        studentRegisterRoom.setIdBatchesRegistrationSchedule(dataStudentRegisterRoomDto.getIdBatchesRegisterSchedule());
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
        response.setCodeOrders(dto.getCodeOrders());
        return response;
    }

    @Transactional
    @Override
    public void approvedStudentRegisterHiredRoom(ApprovedUserRegisterRoomRequest request){
        if (null == request.getIdStudentRegisterRoom() || null == request.getStatus()) {
            throw new ValidParametersException();
        }
        StudentRegisterRoom studentRegisterRoom = findDetailsStudentRegisterRoomById(request.getIdStudentRegisterRoom());
        if(!studentRegisterRoom.getStatus().equals(Constants.STATUS_SUCCESS_PAYMENT_STUDENT_ROOM_REGISTER)) {
            throw new ValidParametersException();
        }
        if (null == request.getIdStudentRegisterRoom() || null == request.getStatus()) {
            throw new ValidParametersException();
        }
        if (!request.getStatus().equals(Constants.STUDENT_REGISTER_ROOM_STATUS_ACCEPT) &&
                !request.getStatus().equals(Constants.STUDENT_REGISTER_ROOM_STATUS_NOT_ACCEPT)){
            throw new ValidParametersException();
        }
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        approvedStudentRegister(studentRegisterRoom,request,ktxUser);
    }

    private StudentRegisterRoom findDetailsStudentRegisterRoomById(Integer idStudentRegisterRoom) {
        Optional<StudentRegisterRoom> studentRegisterRoom = studentRegisterRoomRepository.findStudentRegisterRoomById(idStudentRegisterRoom);
        if (studentRegisterRoom.isEmpty()) {
            throw new NotFoundException();
        }
        return studentRegisterRoom.get();
    }


    public void approvedStudentRegister(StudentRegisterRoom studentRegisterRoom,ApprovedUserRegisterRoomRequest request,KtxUser ktxUser){
       changeApprovedStudent(studentRegisterRoom,request,ktxUser.getIdKtxUser());
        AcceptStudentRegisterRoomDto acceptStudentRegisterRoomDto = studentRegisterRoomRepository.getAcceptStudentRegisterRoomDtoById(studentRegisterRoom.getIdStudentRegisterRoom());
        acceptStudentRegisterRoomDto.setStatusAccept(request.getStatus());
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
            EmailUtil.getInstance().sendApprovedRoom(acceptStudentRegisterRoomDto);
        }
    }


    private StudentRegisterRoom changeApprovedStudent(StudentRegisterRoom studentRegisterRoom,ApprovedUserRegisterRoomRequest request, Integer userId){

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

    @Override
    public String downloadListStudentRegisterRoom(UserRegisterRoomRequest request) throws IOException {
        String fileExcel = PropertiesUtil.getProperty("hust.ktx.static.location.resources.static")
                + SEPARATOR
                + FileUtil.FOLDER_REGISTER_ROOM
                + SEPARATOR
                + "Template_List_Student_Register_Room.xlsx";

        List<UserRegisterRoomDto> studentList = studentRegisterRoomRepository.downloadListStudentRegisterRoom(request);

        try (FileInputStream fileInputStream = new FileInputStream(new File(fileExcel));
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Map<String, CellStyle> styles = createStyles(workbook);

            Sheet sheet = workbook.getSheetAt(0);

            writeDataInfoReport(sheet, styles);
            writeDataToStudentHiredRoomReport(sheet, studentList, styles);



            String root = PropertiesUtil.getProperty("hust.ktx.static.location.tomcat.webapp.ktx-be");
            String folder = root + FileUtil.SEPARATOR
                    + FileUtil.FOLDER_REGISTER_ROOM
                    + FileUtil.SEPARATOR
                    + FileUtil.getFolderInfo();
            FileUtil.createFolder(folder);
            String fileFinal = folder + FileUtil.SEPARATOR + "Template_List_Student_Register_Room" + new Date().getTime() + ".xlsx";
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

    @Override
    public boolean checkExistStudentInRegister(String codeUser, Integer idRoom) {
        return studentRegisterRoomRepository.checkExistStudentInRegister(codeUser,idRoom);
    }

    @Override
    public Integer updateStatusRoomWhenExpiresTime(Long timeCurrent) {
        return studentRegisterRoomRepository.updateStatusRoomWhenExpiresTime(timeCurrent);
    }

    @Override
    public List<StudentRegisterHoldingRoomDto> getStudentRegisterHoldingRoom(Long timeCurrent) {
        return studentRegisterRoomRepository.getListStudentHoldingRoom(timeCurrent);
    }

    @Override
    public StatisticStudentRegisterResponse getStatisticStudentRegister() {
        return studentRegisterRoomRepository.getStatisticStudentRegister();
    }

    @Override
    public StudentRegisterRoom findStudentRegisterRoomByCodeUserAndRoomAndStatus(String codeUser, Integer idRoom, Integer statusSuccessPaymentStudentRoomRegister) {
        Optional<StudentRegisterRoom> studentRegisterRoomOptional = studentRegisterRoomRepository.findStudentRegisterRoomByCodeUserAndRoomAndStatus(codeUser,idRoom,statusSuccessPaymentStudentRoomRegister);
        if (studentRegisterRoomOptional.isEmpty()){
            throw new NotFoundException();
        }
        return studentRegisterRoomOptional.get();
    }

    private void writeDataInfoReport(Sheet sheet, Map<String, CellStyle> styles) {
        String reportTitle = "DANH SÁCH SINH VIÊN ĐĂNG KÍ PHÒNG";
        String dateExport = "Ngày xuất báo cáo: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        writeValueCell(sheet, 0, 0, reportTitle, styles.get("header"));
        writeValueCell(sheet, 1, 0, dateExport, styles.get("normal"));
    }

    private void writeDataToStudentHiredRoomReport(Sheet sheet, List<UserRegisterRoomDto> studentList, Map<String, CellStyle> styles) throws JsonProcessingException {
        int rowStart = 4;
        if (studentList.isEmpty()) {
            return;
        }
//        int shiftSize = studentList.size();
//        if (sheet.getLastRowNum() >= rowStart) {
//            sheet.shiftRows(rowStart, sheet.getLastRowNum(), shiftSize, true, true);
//        }
        ObjectMapper objectMapper = new ObjectMapper();
        int stt = 1;
        for (UserRegisterRoomDto student : studentList) {
            Row row = sheet.createRow(rowStart);
            writeValueCell(row, 0, String.valueOf(stt), styles.get("normal"));
            HashMap<String, Object> dataStudent = objectMapper.readValue(
                    student.getValue() == null ? "{}" : student.getValue(),
                    new TypeReference<>() {}
            );

            writeValueCell(row, 1, ValueUtil.getStringByObject(dataStudent.get("full_name")), null);
            writeValueCell(row, 2, ValueUtil.getStringByObject(dataStudent.get("number_student")), null);
            writeValueCell(row, 3, ValueUtil.getStringByObject(dataStudent.get("number_phone")), null);
            writeValueCell(row, 4, formatTimestamp(student.getTimeRegister(), "dd/MM/yyyy HH:mm"), styles.get("normal"));
            writeValueCell(row, 5, ValueUtil.getStringByObject(student.getTitleDepartment()), styles.get("normal"));
            writeValueCell(row, 6, ValueUtil.getStringByObject(student.getTitleRoom()), styles.get("normal"));
            writeValueCell(row, 7, ValueUtil.getStringByObject(student.getTitleSemester()), styles.get("normal"));
            writeValueCell(row, 8, formatTimestamp(ValueUtil.getLongByObject(student.getTimeHiredStarted()), "dd/MM/yyyy HH:mm"), styles.get("normal"));
            writeValueCell(row, 9, formatTimestamp(ValueUtil.getLongByObject(student.getTimeHiredEnded()), "dd/MM/yyyy HH:mm"), styles.get("normal"));

            rowStart++;
            stt++;
        }
    }
    public static String formatTimestamp(Long timestampMillis, String formatPattern) {
        if (timestampMillis == null || timestampMillis == 0) {
            return "";
        }
        try {
            Instant instant = Instant.ofEpochMilli(timestampMillis);
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
            return dateTime.format(formatter);
        } catch (Exception e) {
            return "Invalid Date";
        }
    }
    private String createFileExportPath() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "DanhSachSinhVienDangKyPhong_" + timestamp + ".xlsx";

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
        styles.put("normal", normalStyle);

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
        return folder + SEPARATOR + "Student_Register_Room_Report_" + new Date().getTime() + "." + ExcelUtil.FILE_EXCEL[1];
    }
}
