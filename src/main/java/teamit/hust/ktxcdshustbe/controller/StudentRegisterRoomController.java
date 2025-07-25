package teamit.hust.ktxcdshustbe.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import teamit.hust.ktxcdshustbe.dto.ApiResponseDto;
import teamit.hust.ktxcdshustbe.entity.StudentRegisterRoom;
import teamit.hust.ktxcdshustbe.exception.*;
import teamit.hust.ktxcdshustbe.request.registerRoom.ChangeRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.registerRoom.DeclareInformationRequest;
import teamit.hust.ktxcdshustbe.request.studentRegister.AcceptPaymentRequest;
import teamit.hust.ktxcdshustbe.request.studentRegister.CreateRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRegister.FindAllSearchRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.ApprovedUserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.UserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.user.UserRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.service.studentRegisterRoom.StudentRegisterRoomService;
import teamit.hust.ktxcdshustbe.service.upload.impl.FileUploadService;

@Log4j2
@Tag(name = "Student register room controller", description = "The student register room API. Contains operations like register room, list room for register etc.")
@RestController
@RequestMapping("/api/v1/student-register")
public class StudentRegisterRoomController {


    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    StudentRegisterRoomService studentRegisterRoomService;


    @GetMapping("/verify-register-room")
    public ResponseEntity<?> verifyRegisterRoom(){
        try {
            studentRegisterRoomService.verifyRegisterRoom();
            return ApiResponseDto.createdWithMessage("Verify account to register room success!", HttpStatus.OK);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/register-room")
    public ResponseEntity<?> createRegisterRoom(@RequestBody CreateRegisterRoomRequest request){
        try {
            studentRegisterRoomService.createStudentRegisterRoom(request);
            return ApiResponseDto.createdWithMessage("Register room success!", HttpStatus.OK);
        } catch (SqlExecuteException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ExitsObjectException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/change-room")
    public ResponseEntity<?> changeRoomRegister(@RequestBody ChangeRegisterRoomRequest request){
        try {
            studentRegisterRoomService.changeRegisterRoomStudent(request);
            return ApiResponseDto.createdWithMessage("Change student register room success!", HttpStatus.OK);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/accept-payment")
    public ResponseEntity<?> acceptPaymentRegisterRoom(@RequestBody AcceptPaymentRequest acceptPaymentRequest) {
        try {
            studentRegisterRoomService.acceptPaymentRegisterRoom(acceptPaymentRequest);
            return ApiResponseDto.createdWithMessage("Accept payment register room success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/badge-register-room")
    public ResponseEntity<?> getBadgeRegisterRoom(@AuthenticationPrincipal OidcUser principal){
        try {
            return ApiResponseDto.createdWithState(studentRegisterRoomService.getBadgeRegisterRoom(principal),
                    "Get badge register room success!", HttpStatus.OK);
        }catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/information-register")
    public ResponseEntity<?> getInformationRegisterRoom(@AuthenticationPrincipal OidcUser principal) {
        try {
          return ApiResponseDto.createdWithState(studentRegisterRoomService.getInformationRegisterRoom(principal),
                  "Get information register room success!", HttpStatus.OK);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/find-all")
    public ResponseEntity<?> findAllListUserRegisterRoom(@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class)
    }) UserRegisterRoomRequest request){
        try {
            Page<UserRegisterRoomResponse> userRegisterRoomResponses = studentRegisterRoomService.findAllUserRegisterRoom(request);
            return ApiResponseDto.createdWithState(userRegisterRoomResponses, "Find all user register room success!", HttpStatus.OK);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/approve")
    public ResponseEntity<?> approvedStudentRegisterRoom(@RequestBody ApprovedUserRegisterRoomRequest request){
        try {
            studentRegisterRoomService.approvedStudentRegisterHiredRoom(request);
            return ApiResponseDto.createdWithMessage("Approved student register room success!", HttpStatus.OK);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<?> getRegisterRoomCurrent(){
        try {
            return ApiResponseDto.createdWithState(studentRegisterRoomService.getRegisterRoomCurrent(),
                    "Get register room current", HttpStatus.OK);
        }catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}