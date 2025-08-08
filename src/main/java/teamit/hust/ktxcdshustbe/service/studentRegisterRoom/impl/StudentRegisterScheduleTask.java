package teamit.hust.ktxcdshustbe.service.studentRegisterRoom.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import teamit.hust.ktxcdshustbe.service.studentRegisterRoom.StudentRegisterRoomService;

@EnableScheduling
@Component
@Log4j2
public class StudentRegisterScheduleTask {

    @Autowired
    StudentRegisterRoomService studentRegisterRoomService;

    @Scheduled(fixedDelay = 10000)
    public void updateStatusHoldingRoom(){
        log.info("[Schedule] - Start update status holding room");
        int amountUpdate = studentRegisterRoomService.updateStatusRoomWhenExpiresTime();
        log.info("[Schedule] - End update {} status holding room", amountUpdate);
    }

}
