package teamit.hust.ktxcdshustbe.repository.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.room.FindAllRoomsDto;
import teamit.hust.ktxcdshustbe.dto.room.StudentHiredRoomDto;
import teamit.hust.ktxcdshustbe.entity.Room;
import teamit.hust.ktxcdshustbe.request.department.FindAllDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.room.FindAllRoomsForRentRequest;
import teamit.hust.ktxcdshustbe.request.room.FindAllRoomsRequest;
import teamit.hust.ktxcdshustbe.request.room.SearchRoomToTranferRequest;
import teamit.hust.ktxcdshustbe.request.room.StudentsHiredRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRegister.StudentRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.room.RoomsForStudentRentResponse;
import teamit.hust.ktxcdshustbe.response.room.SearchRoomResponse;

import java.util.List;
import java.util.Optional;

public interface RoomRepositoryCustom {

    List<StudentHiredRoomDto> findStudentsHiredRoom(Integer id,StudentsHiredRoomRequest request);

    int updateQuantityAndRemainAmountCancelRegisterRoom(Integer idRoom, Integer quantity, Integer userIdModified);

    int updateQuantityAndRemainAmountAcceptRegisterAndHiredRoom(Integer idRoom, Integer quantity, Integer userIdModified);

    int updateQuantityAndRemainAmountToAddNewStudent(Integer idRoom, Integer userIdModified);

    int updateQuantityStudentRegisterRoom(String codeRoom);

    Optional<SearchRoomResponse> searchRoomToTranfer(String titleDepartment, String titleRoom, Integer sex);


    int updateQuantityRegisterOriginRoom(Integer originRoomId);

    int updateQuantityStudentRegisterDestinationRoom(Integer roomId);

    int updateRemainQuantiyRoomWhenToRemoveStudent(Integer roomId, Integer id);

    Page<FindAllRoomsDto> findAllRoomsForRent(FindAllRoomsForRentRequest request, Pageable pageable);
    int updateRemainQuantityRoomWhenToRemoveStudent(String codeRoom, Integer id);


    Optional<Room> findByIdWithDepartment(Integer roomId);

    Optional<Room> findRoomByCodeRoom(String codeRoom);
    Page<FindAllRoomsDto> findAllRooms(FindAllRoomsRequest request, Pageable pageable);
    Optional<Room> findRoomByTitleRoom(String title);
    Optional<Room> findRoomByTitleRoomAndCodeDepartment(String title, String codeDepartment);

    Optional<Room> findRoomByIdRoom(Integer idRoom);
}
