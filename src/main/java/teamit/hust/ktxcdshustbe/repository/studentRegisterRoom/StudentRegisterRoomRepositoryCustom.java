package teamit.hust.ktxcdshustbe.repository.studentRegisterRoom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.registerRoom.StudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.studentRoom.DataStudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.user.UserRegisterRoomDto;
import teamit.hust.ktxcdshustbe.entity.StudentRegisterRoom;
import teamit.hust.ktxcdshustbe.request.studentRegister.FindAllSearchRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.UserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.studentRegister.StudentRegisterRoomResponse;

import java.util.List;
import java.util.Optional;

public interface StudentRegisterRoomRepositoryCustom {

    int getBadgeRegisterRoomByUserId(Integer userId);
    StudentRegisterRoomResponse getInformationRegisterRoom(Integer userId);
    Optional<StudentRegisterRoomResponse> findStudentRegisterRoomByCodeUser(String codeUser);
    Page<UserRegisterRoomDto> findAllUserRegisterRoomDto(UserRegisterRoomRequest request, Pageable pageable);
    Optional<StudentRegisterRoom> findByCode(String codeUser);
    Optional<StudentRegisterRoom> findStudentRegisterRoomById(Integer studentRegisterRoomId);
    boolean isAllowRegisterBatchesRegistration();
    boolean isExistsRegisteredRoomInBatchesRegistrationCurrent();
    Optional<StudentRegisterRoomDto> getInformationRegisterRoomCurrent();
    boolean isAllowRegisterRoomByCodeRoom(String codeRoom);
    Optional<DataStudentRegisterRoomDto> getDataStudentToRegisterRoomByCodeRoom(String codeRoom);

    Optional<StudentRegisterRoom> findStudentRegisterRoomByIdStudentRegisterRoom(Integer idStudentRegisterRoom);

    Optional<StudentRegisterRoom> findStudentRegisterRoomByIdOrder(Integer idOrder);

    List<StudentRegisterRoom> findListStudentRegisterRoomByCodeRoomAndStatus(String codeRoom, Integer status);

    Page<UserRegisterRoomDto> findAllInfoAnUserRegisterRoomDto(UserRegisterRoomRequest request, Pageable pageable);

    List<UserRegisterRoomDto> downloadListStudentRegisterRoom(UserRegisterRoomRequest request);
    List<UserRegisterRoomDto> findListUserRegisterInRoom(Integer status);

    boolean checkExistStudentInRegister(String codeUser, Integer idRoom);
}
