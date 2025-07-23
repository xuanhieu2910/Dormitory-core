package teamit.hust.ktxcdshustbe.repository.studentRoom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.studentRoom.FindAllStudentHiredRoomDto;
import teamit.hust.ktxcdshustbe.entity.StudentRoom;
import teamit.hust.ktxcdshustbe.request.studentRoom.ListStudentHiredRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.StudentListRoomHiredRequest;
import teamit.hust.ktxcdshustbe.response.studentRoom.ListStudentHiredRoomResponse;
import teamit.hust.ktxcdshustbe.response.studentRoom.StudentSearchAddNewRoomResponse;
import teamit.hust.ktxcdshustbe.response.user.HiredRoomsResponse;
import teamit.hust.ktxcdshustbe.response.user.ListHiredRoomStudentResponse;
import teamit.hust.ktxcdshustbe.response.user.UserHiredRoomDetailResponse;

import java.util.List;
import java.util.Optional;

public interface StudentRoomRepositoryCustom {

    List<HiredRoomsResponse> getRoomsHiredByUser(String codeUser);

    Page<ListHiredRoomStudentResponse> getListHiredRoomStudentResponse(StudentListRoomHiredRequest request, Pageable pageable);

    Page<FindAllStudentHiredRoomDto> getListStudentHiredRoomResponse(ListStudentHiredRoomRequest request, Pageable pageable);

    Optional<StudentRoom> findStudentRoomByStudentRoomId(Integer studentRoomId);

    Optional<StudentSearchAddNewRoomResponse> searchStudentAddNewRoom(String numberStudent);

    Optional<StudentRoom> findStudentHiringRoomByStudentId (Integer studentId);

    Optional<StudentRoom> findStudentHiringRoomByCodeUser(String codeUser);
    Optional<StudentRoom> getStudentRoomIsActiveByCodeUserAndRoomIdRoom(String codeUser, Integer idRoom);

    List<FindAllStudentHiredRoomDto> findAllStudentsForExport(ListStudentHiredRoomRequest request);
}
