package teamit.hust.ktxcdshustbe.service.studentRegisterRoom;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import teamit.hust.ktxcdshustbe.dto.registerRoom.StudentRegisterHoldingRoomDto;
import teamit.hust.ktxcdshustbe.entity.StudentRegisterRoom;
import teamit.hust.ktxcdshustbe.request.registerRoom.ChangeRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRegister.AcceptPaymentRequest;
import teamit.hust.ktxcdshustbe.request.studentRegister.CreateRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRoom.ListStudentHiredRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.ApprovedUserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.UserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.studentRegister.StatisticStudentRegisterResponse;
import teamit.hust.ktxcdshustbe.response.studentRegister.StudentRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.response.user.UserRegisterRoomResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface StudentRegisterRoomService {

    void saveInfoApprovedStudentRegisterRoom(StudentRegisterRoom room);

    void changeRegisterRoomStudent(ChangeRegisterRoomRequest request) throws Exception;

    void acceptPaymentRegisterRoom(AcceptPaymentRequest request);

    int getBadgeRegisterRoom(OidcUser principal);

    StudentRegisterRoomResponse getInformationRegisterRoom(OidcUser principal);

    Optional<StudentRegisterRoomResponse> findStudentRegisterRoomByUserCodeUser(String codeUser);

    Page<UserRegisterRoomResponse> findAllUserRegisterRoom(UserRegisterRoomRequest request);

    void approvedStudentRegisterHiredRoom(ApprovedUserRegisterRoomRequest request);

    Optional<StudentRegisterRoom> findStudentRegisterRoomByCode(String codeUser);

    void verifyRegisterRoom();

    StudentRegisterRoomResponse getRegisterRoomCurrent();
    StudentRegisterRoom findStudentRegisterRoomById(Integer idStudentRegisterRoom);

    void createStudentRegisterRoom(CreateRegisterRoomRequest request);
    StudentRegisterRoom saveStudentRoomRegisterRoom(StudentRegisterRoom studentRegisterRoom);

    StudentRegisterRoom getStudentRegisterRoomByIdOrder(Integer idOrder);

    List<StudentRegisterRoom> findListStudentRegisterRoomByCodeRoomAndStatus(String codeRoom, List<Integer> statusSuccessPaymentStudentRoomRegister);
    Page<UserRegisterRoomResponse> findAllInfoAnUserRegisterRoomByCode(UserRegisterRoomRequest request);

    String downloadListStudentRegisterRoom(UserRegisterRoomRequest request) throws IOException;

    boolean checkExistStudentInRegister(String codeUser, Integer idRoom);

    Integer updateStatusRoomWhenExpiresTime(Long timeCurrent);
    List<StudentRegisterHoldingRoomDto> getStudentRegisterHoldingRoom(Long timeCurrent);

    StatisticStudentRegisterResponse getStatisticStudentRegister();

    StudentRegisterRoom findStudentRegisterRoomByCodeUserAndRoomAndStatus(String codeUser, Integer idRoom, Integer statusSuccessPaymentStudentRoomRegister);

    boolean checkExistStudentPendingInRegister(String codeRoom);

    boolean checkExistStudentRemoveInRegister(String codeUser, Integer idRoom);

    boolean checkExistStudentHoldInRegister(String codeRoom, String codeUser);
}
