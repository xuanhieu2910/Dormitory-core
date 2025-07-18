package teamit.hust.ktxcdshustbe.repository.serviceRoom;

import teamit.hust.ktxcdshustbe.dto.serviceRoom.ServiceRoomDto;
import teamit.hust.ktxcdshustbe.entity.ServiceRoom;

import java.util.List;

public interface ServiceRoomRepositoryCustom {

    List<ServiceRoomDto> findAllServicesRoomByRoomId(Integer roomId);

    List<ServiceRoom> findServicesRoomByRoomId(Integer roomId);

    List<ServiceRoomDto> findServicesRoomByCodeRoomAndCodesService(String codeRoom, List<String> codesService);
}
