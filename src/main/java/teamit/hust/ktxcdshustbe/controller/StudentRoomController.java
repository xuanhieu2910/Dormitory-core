package teamit.hust.ktxcdshustbe.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamit.hust.ktxcdshustbe.dto.ApiResponseDto;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.request.studentRoom.ListStudentHiredRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRoom.RemoveStudentInRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRoom.StudentToRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRoom.TransferRoomRequest;
import teamit.hust.ktxcdshustbe.service.studentRoom.StudentRoomService;

@Tag(name = "Student Hired Room API", description = "The student hired room API. Contains operations like CRUD student hired room.")
@RestController
@RequestMapping("/api/v1/student-room")
public class StudentRoomController {

    @Autowired
    StudentRoomService studentRoomService;


    @GetMapping("/find-all")
    public ResponseEntity<?> getListStudentHiredRoom(@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class)
    })ListStudentHiredRoomRequest request){
        try {
            return ApiResponseDto.createdWithState(studentRoomService.getListStudentHiredRoomResponse(request), "Get list student hired room success!", HttpStatus.OK);
        }catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/search-student")
    public ResponseEntity<?> searchStudentToAddNewRoom(@RequestParam("number-student") String numberStudent) {
        try {
            return ApiResponseDto.createdWithState(studentRoomService.searchStudentAddNewRoom(numberStudent),
                    "Search student success!", HttpStatus.OK);
        }catch (ValidParametersException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-student")
    public ResponseEntity<?> addStudentToRoom(@RequestBody StudentToRoomRequest request) {
        try {
            studentRoomService.addStudentToRoom(request);
            return ApiResponseDto.createdWithMessage("Add student to room success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove-student")
    public ResponseEntity<?> removeStudentInRoom(@RequestBody RemoveStudentInRoomRequest request) {
        try {
            studentRoomService.removeStudentRoom(request);
            return ApiResponseDto.createdWithMessage("Remove student to room success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/room-transfer")
    public ResponseEntity<?> transferRoom(@RequestBody TransferRoomRequest request){
        try {
            studentRoomService.transferRoom(request);
            return ApiResponseDto.createdWithMessage("Transfer student to another room success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
