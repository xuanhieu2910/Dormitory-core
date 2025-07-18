package teamit.hust.ktxcdshustbe.service.room;

import org.springframework.data.domain.Page;
import teamit.hust.ktxcdshustbe.entity.Room;
import teamit.hust.ktxcdshustbe.exception.ValidateFiledException;
import teamit.hust.ktxcdshustbe.request.room.*;
import teamit.hust.ktxcdshustbe.response.room.FindAllRoomsResponse;
import teamit.hust.ktxcdshustbe.response.room.RoomDetailResponse;
import teamit.hust.ktxcdshustbe.response.room.RoomsForStudentRentResponse;
import teamit.hust.ktxcdshustbe.response.room.SearchRoomResponse;

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
    void updateQuantityStudentRegisterRoom(String codeRoom);
    SearchRoomResponse searchRoomToTranfer(SearchRoomToTranferRequest searchRoom);
    void updateQuantityRegisterOriginRoom(Integer originRoomId);
    void updateQuantityStudentRegisterDestinationRoom(Integer roomId);
    int updateRemainQuantityRoomWhenToRemoveStudent(String codeRoom, Integer idKtxUser);
    Optional<Room> findRoomByCodeRoom(String codeRoom);
    Page<FindAllRoomsResponse> findAllRoom(FindAllRoomsRequest request);
}
