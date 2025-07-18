package teamit.hust.ktxcdshustbe.service.serviceRoom;

import teamit.hust.ktxcdshustbe.dto.serviceRoom.ServiceRoomDto;
import teamit.hust.ktxcdshustbe.entity.Service;
import teamit.hust.ktxcdshustbe.entity.ServiceRoom;
import teamit.hust.ktxcdshustbe.response.serviceRoom.ServiceRoomResponse;

import java.util.List;

public interface ServiceRoomService {


    List<ServiceRoomDto> findAllServicesRoomByRoomId(Integer roomId);
    List<ServiceRoomResponse> findAllServicesRoom();
    void storedServiceRooms(List<ServiceRoom> serviceRoom);
    List<ServiceRoomDto> findServicesRoomByCodeRoomAndCodesService(String codeRoom, List<String> codesService);
}
