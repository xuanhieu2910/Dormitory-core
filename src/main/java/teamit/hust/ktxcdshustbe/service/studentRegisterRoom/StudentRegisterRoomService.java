package teamit.hust.ktxcdshustbe.service.studentRegisterRoom;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import teamit.hust.ktxcdshustbe.dto.registerRoom.AcceptStudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.entity.StudentRegisterRoom;
import teamit.hust.ktxcdshustbe.request.registerRoom.ChangeRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.registerRoom.DeclareInformationRequest;
import teamit.hust.ktxcdshustbe.request.studentRegister.AcceptPaymentRequest;
import teamit.hust.ktxcdshustbe.request.studentRegister.CreateRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.ApprovedUserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.UserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.registerRoom.StudentRegisterRoomDetailResponse;
import teamit.hust.ktxcdshustbe.response.studentRegister.StudentRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.response.user.UserRegisterRoomResponse;

import java.util.Optional;

public interface StudentRegisterRoomService {

    Optional<StudentRegisterRoom> findStudentRegisterRoomById(Integer idStudentRegister);

    StudentRegisterRoomDetailResponse findStudentRegisterRoomDetailByRegisterId(Integer registerId);

    void approvedStudentRegisterRoom(StudentRegisterRoom room);

    void changeRegisterRoomStudent(ChangeRegisterRoomRequest request) throws Exception;

    StudentRegisterRoom createStudentRegisterRoom(CreateRegisterRoomRequest request, OidcUser principal) throws Exception;

    void acceptPaymentRegisterRoom(AcceptPaymentRequest request);

    int getBadgeRegisterRoom(OidcUser principal);

    StudentRegisterRoomResponse getInformationRegisterRoom(OidcUser principal);

    Optional<StudentRegisterRoomResponse> findStudentRegisterRoomResponseByUserId(Integer userId);

    AcceptStudentRegisterRoomDto getAcceptStudentRegisterRoomDtoById(Integer studentRegisterRoomId);

    void declareInformationStudent(OidcUser principal,DeclareInformationRequest request);
    void declareInformationStudentV2(OidcUser principal,DeclareInformationRequest request);

    Optional<StudentRegisterRoomResponse> findStudentRegisterRoomByUserCodeUser(String codeUser);

    Page<UserRegisterRoomResponse> findAllUserRegisterRoom(UserRegisterRoomRequest request);

    void approvedStudentRegisterHiredRoom(ApprovedUserRegisterRoomRequest request);

    Optional<StudentRegisterRoom> findStudentRegisterRoomByCode(String codeUser);
}
