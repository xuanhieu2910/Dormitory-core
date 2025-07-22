package teamit.hust.ktxcdshustbe.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamit.hust.ktxcdshustbe.dto.ApiResponseDto;
import teamit.hust.ktxcdshustbe.exception.IsBlankException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.request.department.FindAllDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.room.FindAllRoomsForRentRequest;
import teamit.hust.ktxcdshustbe.response.room.RoomsForStudentRentResponse;
import teamit.hust.ktxcdshustbe.service.department.DepartmentService;
import teamit.hust.ktxcdshustbe.service.room.RoomService;
import teamit.hust.ktxcdshustbe.service.semester.SemesterService;
import teamit.hust.ktxcdshustbe.service.timeHired.TimeHiredService;

@Tag(name = "Data controller", description = "The data API. Contains operations for call data etc.")
@RestController
@RequestMapping("/api/v1/data")
public class DataController {

    @Autowired
    TimeHiredService timeHiredService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    RoomService roomService;
//    @Autowired
//    SemesterService semesterService;



    @GetMapping("/time-hired")
    public ResponseEntity<?> getTimeHiredActive(){
        try {
            return ApiResponseDto.createdWithState(timeHiredService.getTimeHiredActived(),
                    "Find time hired success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/departments")
    public ResponseEntity<?> getListDepartment(@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class)})FindAllDepartmentRequest request){
        try {
            return ApiResponseDto.createdWithState(departmentService.findAllDepartment(request),
                    "Find all department success!", HttpStatus.OK);
        }catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/rooms-for-rent")
    public ResponseEntity<?> getListRoomForRent(@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class)
    }) FindAllRoomsForRentRequest request){
        try {
            Page<RoomsForStudentRentResponse> getRoomsForStudentRent = roomService.getRoomsForStudentRent(request);
            return ApiResponseDto.createdWithState(getRoomsForStudentRent,
                    "Find all room for student hired success!", HttpStatus.OK);
        } catch (IsBlankException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
