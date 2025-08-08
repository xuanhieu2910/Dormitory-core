package teamit.hust.ktxcdshustbe.service.studentRegisterRoom.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.registerRoom.StudentRegisterHoldingRoomDto;
import teamit.hust.ktxcdshustbe.service.room.RoomService;
import teamit.hust.ktxcdshustbe.service.studentRegisterRoom.StudentRegisterRoomService;

import java.util.Date;
import java.util.List;

@EnableScheduling
@Component
@Log4j2
public class StudentRegisterScheduleTask {

    @Autowired
    StudentRegisterRoomService studentRegisterRoomService;
    @Autowired
    RoomService roomService;

    @Scheduled(fixedDelay = 20000)
    public void updateStatusHoldingRoom(){
        log.info("[Schedule - Register Room] - Start update status holding room");
        Long timeCurrent = new Date().getTime();
        boolean isUpdateQuantity =  updateQuantityRegisterRoom(timeCurrent);
        int amountUpdate = 0;
        if (isUpdateQuantity) {
             amountUpdate = studentRegisterRoomService.updateStatusRoomWhenExpiresTime(timeCurrent);
        }
        log.info("[Schedule - Register Room] - End update {} status holding room", amountUpdate);
    }

    private boolean updateQuantityRegisterRoom(Long timeCurrent) {
        List<StudentRegisterHoldingRoomDto> holdingRoomDtoList = studentRegisterRoomService.getStudentRegisterHoldingRoom(timeCurrent);
        if (CollectionUtils.isEmpty(holdingRoomDtoList)){
            return false;
        }
        for (StudentRegisterHoldingRoomDto dto : holdingRoomDtoList){
            roomService.updateQuantityRegisterRoomByIdRoomAndAmount(dto.getIdRoom(), dto.getQuantity());
            log.info("[Schedule - Register Room] - Update quantity room by id {} - quantity - {}", dto.getIdRoom(), dto.getQuantity());
        }
        return true;
    }

}
