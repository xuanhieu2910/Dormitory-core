package teamit.hust.ktxcdshustbe.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamit.hust.ktxcdshustbe.dto.ApiResponseDto;
import teamit.hust.ktxcdshustbe.response.serviceRoom.ServiceRoomResponse;
import teamit.hust.ktxcdshustbe.service.serviceRoom.ServiceRoomService;

import java.util.List;

@Tag(name = "Service Room API", description = "The service room API. Contains operations like CRUD service room.")
@RestController
@RequestMapping("/api/v1/service-room")
public class ServiceRoomController {


    @Autowired
    ServiceRoomService serviceRoomService;


    @GetMapping
    public ResponseEntity<?> findAllServiceRoom(){
        try{
            List<ServiceRoomResponse> serviceRoomResponseList = serviceRoomService.findAllServicesRoom();
            return ApiResponseDto.createdWithState(serviceRoomResponseList,
                    "Find all service room success!", HttpStatus.OK);
        }catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
