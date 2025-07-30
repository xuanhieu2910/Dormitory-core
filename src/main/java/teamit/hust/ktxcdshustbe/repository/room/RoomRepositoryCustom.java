package teamit.hust.ktxcdshustbe.repository.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.room.FindAllRoomsDto;
import teamit.hust.ktxcdshustbe.dto.room.SearchInformationRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.room.StudentHiredRoomDto;
import teamit.hust.ktxcdshustbe.dto.room.StudentSearchRoomDto;
import teamit.hust.ktxcdshustbe.entity.Room;
import teamit.hust.ktxcdshustbe.request.department.FindAllDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.room.FindAllRoomsForRentRequest;
import teamit.hust.ktxcdshustbe.request.room.FindAllRoomsRequest;
import teamit.hust.ktxcdshustbe.request.room.SearchRoomToTranferRequest;
import teamit.hust.ktxcdshustbe.request.room.StudentsHiredRoomRequest;
import teamit.hust.ktxcdshustbe.request.room.*;
import teamit.hust.ktxcdshustbe.request.studentRegister.StudentRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.room.RoomsForStudentRentResponse;
import teamit.hust.ktxcdshustbe.response.room.SearchRoomResponse;

import java.util.List;
import java.util.Optional;

public interface RoomRepositoryCustom {

    int updateQuantityAndRemainAmountCancelRegisterRoom(Integer idRoom, Integer quantity, Integer userIdModified);

    int updateQuantityAndRemainAmountAcceptRegisterAndHiredRoom(Integer idRoom, Integer quantity, Integer userIdModified);

    int updateQuantityAndRemainAmountToAddNewStudent(Integer idRoom, Integer userIdModified);

    int updateQuantityStudentRegisterRoom(String codeRoom);

    Optional<SearchRoomResponse> searchRoomToTranfer(String titleDepartment, String titleRoom, Integer sex);

    int updateQuantityRegisterOriginRoom(Integer originRoomId);

    int updateQuantityStudentRegisterDestinationRoom(Integer roomId);

    Page<FindAllRoomsDto> findAllRoomsForRent(FindAllRoomsForRentRequest request, Pageable pageable);
    int updateRemainQuantityRoomWhenToRemoveStudent(String codeRoom, Integer id);
    Optional<Room> findRoomByCodeRoom(String codeRoom);
    Page<FindAllRoomsDto> findAllRooms(FindAllRoomsRequest request, Pageable pageable);
    Optional<Room> findRoomByTitleRoomAndCodeDepartment(String title, String codeDepartment);
    Optional<Room> findRoomByIdRoom(Integer idRoom);
    Page<StudentSearchRoomDto> findAllRoomStudentSearch(StudentSearchRoomRequest request, Pageable pageable);
    Page<SearchInformationRegisterRoomDto> findInformationRegisterRoom(SearchInformationRegisterRoomRequest request, Pageable pageable);
    Optional<List<Room>> findAllRoomByListCodeRoom(List<String> codesRoom);

    Optional<List<Room>> findAllRoomByIdsRoom(List<Integer> idsRoom);

    int updateRemainQuantityRoomWhenStudentRegisterHoldingRoomByIdRoom(Integer idRoom);

    int updateRemainQuantityRegisterRoomWhenStudentChangeRoom(Integer idRoom);

    Page<FindAllRoomsDto> findAllRoomsRegister(FindAllRoomsRequest request, Pageable pageable);

    List<FindAllRoomsDto> findAllListRoomByCodeDepartment(String codeDepartment);
}
