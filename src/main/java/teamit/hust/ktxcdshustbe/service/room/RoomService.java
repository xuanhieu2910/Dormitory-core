package teamit.hust.ktxcdshustbe.service.room;

import org.springframework.data.domain.Page;
import teamit.hust.ktxcdshustbe.entity.Room;
import teamit.hust.ktxcdshustbe.exception.ValidateFiledException;
import teamit.hust.ktxcdshustbe.request.room.*;
import teamit.hust.ktxcdshustbe.request.studentRegister.FindAllSearchRoomRequest;
import teamit.hust.ktxcdshustbe.response.room.FindAllRoomsResponse;
import teamit.hust.ktxcdshustbe.response.room.RoomDetailResponse;
import teamit.hust.ktxcdshustbe.response.room.RoomsForStudentRentResponse;
import teamit.hust.ktxcdshustbe.response.room.SearchRoomResponse;
import teamit.hust.ktxcdshustbe.response.room.*;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    void changeActiveRoomByCodeRoom(String codeRoom);
    void changeSexRoomByCodeRoom(String codeRoom);
    RoomDetailResponse findRoomDetailByCodeRoom(String codeRoom) throws ValidateFiledException;
    void createNewRoom(CreateNewRoomRequest createNewRoomRequest);
    void updateQuantityAndRemainAmountCancelRegisterRoom(Integer idRoom, Integer quantity, Integer userIdModified);
    void updateQuantityAndRemainAmountAcceptRegisterAndHiredRoom(Integer idRoom, Integer quantity, Integer userIdModified);
    void editRoom(EditRoomRequest request);
    Page<RoomsForStudentRentResponse> getRoomsForStudentRent(FindAllRoomsForRentRequest request);
    SearchRoomResponse searchRoomToTranfer(SearchRoomToTranferRequest searchRoom);
    int updateRemainQuantityRoomWhenToRemoveStudent(String codeRoom, Integer idKtxUser);
    Optional<Room> findRoomByCodeRoom(String codeRoom);
    Page<FindAllRoomsResponse> findAllRoom(FindAllRoomsRequest request);

    Page<StudentSearchRoomResponse> studentSearchRoom(StudentSearchRoomRequest request);

    Optional<Room> findRoomByIdRoom(Integer idRoom);
    List<Room> findAllRoomByListCodeRoom(List<String> codesRoom);
    List<Room> findAllRoomByListIdsRoom(List<Integer> idsRoom);

    Page<SearchInformationRegisterRoomResponse> SearchInformationRegisterRoom(SearchInformationRegisterRoomRequest request);

    void updateRemainQuantityRegisterRoomByIdRoom(Integer idRoom);

    void updateRemainQuantityRegisterRoomWhenStudentChangeRoom(Integer idRoom);
}
