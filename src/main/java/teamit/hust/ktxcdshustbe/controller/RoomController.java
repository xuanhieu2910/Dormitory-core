package teamit.hust.ktxcdshustbe.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamit.hust.ktxcdshustbe.dto.ApiResponseDto;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.request.room.*;
import teamit.hust.ktxcdshustbe.response.room.FindAllRoomsResponse;
import teamit.hust.ktxcdshustbe.response.room.RoomDetailResponse;
import teamit.hust.ktxcdshustbe.response.room.SearchRoomResponse;
import teamit.hust.ktxcdshustbe.service.room.RoomService;

@Tag(name = "Room API", description = "The room API. Contains operations like CRUD room.")
@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping
    public ResponseEntity<?> findAllRoom( @And({
                    @Spec(path = "page", params = "page", spec = Like.class),
                    @Spec(path = "size", params = "size", spec = Like.class),
                    @Spec(path = "keyword", params = "keyword", spec = Like.class)
        }) FindAllRoomsRequest request) {
        try {
            Page<FindAllRoomsResponse> roomsResponsePage = roomService.findAllRoom(request);
            return ApiResponseDto.createdWithState(roomsResponsePage, "Find all rooms success!", HttpStatus.OK);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/room-details")
    public ResponseEntity<?> getInformationDetailRoom(@RequestParam("code") String codeRoom){
        try {
            RoomDetailResponse room = roomService.findRoomDetailByCodeRoom(codeRoom);
            return ApiResponseDto.createdWithState(room,"Find room detail success!", HttpStatus.OK);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/change-active")
    public ResponseEntity<?> changeActiveRoom(@RequestParam(value = "code") String codeRoom){
        try {
            roomService.changeActiveRoomByCodeRoom(codeRoom);
            return ApiResponseDto.createdWithMessage("Change active room success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ValidParametersException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/change-sex")
    public ResponseEntity<?> changeSexRoom(@RequestParam(value = "code") String codeRoom){
        try {
            roomService.changeSexRoomByCodeRoom(codeRoom);
            return ApiResponseDto.createdWithMessage("Change sex room success!", HttpStatus.OK);
        }  catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ValidParametersException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/create")
    public ResponseEntity<?> createNewRoom(@RequestBody CreateNewRoomRequest createNewRoomRequest){
        try {
            roomService.createNewRoom(createNewRoomRequest);
            return ApiResponseDto.createdWithMessage("Create new room success!", HttpStatus.OK);
        } catch (ExitsObjectException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ValidParametersException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/edit")
    public ResponseEntity<?> editRoom(@RequestBody EditRoomRequest request){
        try {
            roomService.editRoom(request);
            return ApiResponseDto.createdWithMessage("Edit room success!", HttpStatus.OK);
        } catch (ExitsObjectException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ValidParametersException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/search-room")
    public ResponseEntity<?> searchRoom(@RequestBody SearchRoomToTranferRequest request) {
        try {
            SearchRoomResponse response = roomService.searchRoomToTranfer(request);
            return ApiResponseDto.createdWithState(response, "Search room success!", HttpStatus.OK);
        } catch (ValidParametersException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student-search-room")
    public ResponseEntity<?> studentSearchRoom( @And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class)
    }) StudentSearchRoomRequest request){
        try {
            return ApiResponseDto.createdWithState(roomService.studentSearchRoom(request),
                    "Student search room success!", HttpStatus.OK);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
