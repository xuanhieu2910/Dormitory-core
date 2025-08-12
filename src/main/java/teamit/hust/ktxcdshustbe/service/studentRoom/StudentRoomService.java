package teamit.hust.ktxcdshustbe.service.studentRoom;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import teamit.hust.ktxcdshustbe.entity.StudentRoom;
import teamit.hust.ktxcdshustbe.exception.ValidateFiledException;
import teamit.hust.ktxcdshustbe.request.studentRoom.ListStudentHiredRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRoom.RemoveStudentInRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRoom.StudentToRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRoom.TransferRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.StudentListRoomHiredRequest;
import teamit.hust.ktxcdshustbe.response.studentRoom.ListStudentHiredRoomResponse;
import teamit.hust.ktxcdshustbe.response.studentRoom.StudentSearchAddNewRoomResponse;
import teamit.hust.ktxcdshustbe.response.user.HiredRoomsResponse;
import teamit.hust.ktxcdshustbe.response.user.ListHiredRoomStudentResponse;

import java.io.IOException;
import java.util.List;

public interface StudentRoomService {

    void saveStudentRoom(StudentRoom room);
    List<HiredRoomsResponse> getRoomsHiredByUser(String codeUser) throws ValidateFiledException;
    Page<ListHiredRoomStudentResponse> getListHiredRoomStudentResponse(OidcUser principal,StudentListRoomHiredRequest request);
    Page<ListStudentHiredRoomResponse> getListStudentHiredRoomResponse(ListStudentHiredRoomRequest request);
    StudentRoom findStudentRoomByStudentRoomId(Integer studentRoomId);
    StudentSearchAddNewRoomResponse searchStudentAddNewRoom(String numberStudent) throws Exception;
    void addStudentToRoom(StudentToRoomRequest request) throws Exception;
    void removeStudentRoom(RemoveStudentInRoomRequest request) throws Exception;
    void transferRoom(TransferRoomRequest request);

    String downloadStudentHiredRoomList(ListStudentHiredRoomRequest request) throws IOException;


}
