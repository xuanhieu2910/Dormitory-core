package teamit.hust.ktxcdshustbe.repository.studentRegisterRoom;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.registerRoom.AcceptStudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.user.UserRegisterRoomDto;
import teamit.hust.ktxcdshustbe.entity.StudentRegisterRoom;
import teamit.hust.ktxcdshustbe.request.user.UserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.registerRoom.StudentRegisterRoomDetailResponse;
import teamit.hust.ktxcdshustbe.response.studentRegister.StudentRegisterRoomResponse;

import java.util.Optional;

public interface StudentRegisterRoomRepositoryCustom {

    StudentRegisterRoomDetailResponse getStudentDetailRegisterRoomByRegisterId(Integer registerId);

    int getBadgeRegisterRoomByUserId(Integer userId);

    StudentRegisterRoomResponse getInformationRegisterRoom(Integer userId);

    Optional<StudentRegisterRoomResponse> findStudentRegisterRoomByUserId(Integer userId);

    AcceptStudentRegisterRoomDto getAcceptStudentRegisterRoomDtoById(Integer studentRegisterRoomId);


    Optional<Integer> findDepartmentIdByRegisterId(Integer registerId);

    Optional<StudentRegisterRoomResponse> findStudentRegisterRoomByCodeUser(String codeUser);

    Page<UserRegisterRoomDto> findAllUserRegisterRoomDto(UserRegisterRoomRequest request, Pageable pageable);

    Optional<StudentRegisterRoom> findByCode(String codeUser);
    Optional<StudentRegisterRoom> findByUserIdAndTimeIdHired(Integer userId, Integer timeIdHired);

    boolean existsBySemesterId(Integer semesterId);

    Optional<StudentRegisterRoom> findStudentRegisterRoomById(Integer studentRegisterRoomId);
}
