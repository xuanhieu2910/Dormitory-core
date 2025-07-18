package teamit.hust.ktxcdshustbe.repository.serviceRoom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.serviceRoom.ServiceRoomDto;
import teamit.hust.ktxcdshustbe.entity.ServiceRoom;
import teamit.hust.ktxcdshustbe.repository.serviceRoom.ServiceRoomRepositoryCustom;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;

public class ServiceRoomRepositoryImpl implements ServiceRoomRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public List<ServiceRoomDto> findAllServicesRoomByRoomId(Integer roomId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select  ser.code_service, ro.code_room, " +
                "        ser.title, ser.description, serviceRoom.status   " +
                "from service_room serviceRoom " +
                "     inner join service ser on ser.id_service = serviceRoom.id_service " +
                "     inner join room ro on ro.id_room = serviceRoom.id_room " +
                "where ro.id_room = :idRoom ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idRoom", roomId);
        List<Object[]> result = query.getResultList();
        List<ServiceRoomDto> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj: result){
                ServiceRoomDto serviceRoomDto = new ServiceRoomDto();
                serviceRoomDto.setCodeService(ValueUtil.getStringByObject(obj[0]));
                serviceRoomDto.setCodeRoom(ValueUtil.getStringByObject(obj[1]));
                serviceRoomDto.setTitle(ValueUtil.getStringByObject(obj[2]));
                serviceRoomDto.setDescription(ValueUtil.getStringByObject(obj[3]));
                serviceRoomDto.setStatus(ValueUtil.getIntegerByObject(obj[4]));
                responses.add(serviceRoomDto);
            }
        }
        return responses;
    }

    @Override
    public List<ServiceRoom> findServicesRoomByRoomId(Integer roomId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select serviceRoom.id_service_room, serviceRoom.id_service,  " +
                "       serviceRoom.id_room, serviceRoom.time_created,  " +
                "       serviceRoom.time_modified, serviceRoom.status,  " +
                "       serviceRoom.id_user_created, serviceRoom.id_user_modified  " +
                "from service_room serviceRoom  " +
                "    inner join room ro on serviceRoom.id_room = ro.id_room  " +
                "    inner join service ser on serviceRoom.id_service = ser.id_service  " +
                "where serviceRoom.id_room = :roomId  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("roomId", roomId);
        List<Object[]> result = query.getResultList();
        List<ServiceRoom> serviceRooms = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result){
                serviceRooms.add(writeDataServiceRoom(obj));
            }
        }
        return serviceRooms;
    }

    @Override
    public List<ServiceRoomDto> findServicesRoomByCodeRoomAndCodesService(String codeRoom,
                                                                       List<String> codesService) {
        StringBuilder sb = new StringBuilder();
        sb.append("select sr.id_service_room, sr.id_service, sr.id_room,  " +
                "           sr.time_created, sr.time_modified, sr.status,  " +
                "           sr.id_user_created, sr.id_user_modified,  " +
                "           ro.code_room, ser.code_service, ser.title, ser.description  " +
                "from service_room sr  " +
                "        inner join room ro on sr.id_room = ro.id_room  " +
                "        inner join service ser on sr.id_service = ser.id_service  " +
                "where ro.code_room = :codeRoom  " +
                "and ser.code_service in (:codeService) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeRoom", codeRoom);
        query.setParameter("codeService", codesService);
        List<Object[]> result = query.getResultList();
        List<ServiceRoomDto> serviceRooms = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                serviceRooms.add(writeDataServiceRoomDto(obj));
            }
        }
        return serviceRooms;
    }

    private ServiceRoomDto writeDataServiceRoomDto(Object[] obj) {
        ServiceRoomDto serviceRoomDto = new ServiceRoomDto();
        serviceRoomDto.setIdServiceRoom(ValueUtil.getIntegerByObject(obj[0]));
        serviceRoomDto.setIdService(ValueUtil.getIntegerByObject(obj[1]));
        serviceRoomDto.setIdRoom(ValueUtil.getIntegerByObject(obj[2]));
        serviceRoomDto.setTimeCreated(ValueUtil.getLongByObject(obj[3]));
        serviceRoomDto.setTimeModified(ValueUtil.getLongByObject(obj[4]));
        serviceRoomDto.setStatus(ValueUtil.getIntegerByObject(obj[5]));
        serviceRoomDto.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
        serviceRoomDto.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
        serviceRoomDto.setCodeRoom(ValueUtil.getStringByObject(obj[8]));
        serviceRoomDto.setCodeService(ValueUtil.getStringByObject(obj[9]));
        serviceRoomDto.setTitle(ValueUtil.getStringByObject(obj[10]));
        serviceRoomDto.setDescription(ValueUtil.getStringByObject(obj[11]));
        return serviceRoomDto;
    }

    private ServiceRoom writeDataServiceRoom(Object[] obj) {
        ServiceRoom serviceRoom = new ServiceRoom();
        serviceRoom.setIdServiceRoom(ValueUtil.getIntegerByObject(obj[0]));
        serviceRoom.setIdService(ValueUtil.getIntegerByObject(obj[1]));
        serviceRoom.setIdRoom(ValueUtil.getIntegerByObject(obj[2]));
        serviceRoom.setTimeCreated(ValueUtil.getLongByObject(obj[3]));
        serviceRoom.setTimeModified(ValueUtil.getLongByObject(obj[4]));
        serviceRoom.setStatus(ValueUtil.getIntegerByObject(obj[5]));
        serviceRoom.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
        serviceRoom.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
        return serviceRoom;
    }
}
