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
import teamit.hust.ktxcdshustbe.dto.user.AuthenticationDto;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.request.user.FindAllStudentsRequest;
import teamit.hust.ktxcdshustbe.request.user.StudentListRoomHiredRequest;
import teamit.hust.ktxcdshustbe.request.user.UpdateProfileUserRequest;
import teamit.hust.ktxcdshustbe.response.user.*;
import teamit.hust.ktxcdshustbe.service.auth.AuthenticationService;
import teamit.hust.ktxcdshustbe.service.studentRoom.StudentRoomService;
import teamit.hust.ktxcdshustbe.service.user.KtxUserService;

import java.util.List;


@Log4j2
@Tag(name = "User controller", description = "The User API. Contains operations like users, details information, list register room etc.")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {


    @Autowired
    KtxUserService ktxUserService;

    @Autowired
    StudentRoomService studentRoomService;
    @Autowired
    AuthenticationService authenticationService;


    @GetMapping("/hired-room/user-details")
    public ResponseEntity<?> getDetailsStudentHiredRoom(@RequestParam("hired-room-id") Integer hiredRoomId){
        try {
            DetailInformationUserResponse response = ktxUserService.getDetailInformationUserHiredRoomIdByHiredRoomId(hiredRoomId);
            return ApiResponseDto.createdWithState(response, "Get user detail hired room success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/hired-room/details-rooms")
    public ResponseEntity<?> getListRoomStudentHired(@RequestParam("code-user") String codeUser){
        try {
            List<HiredRoomsResponse> getRoomsHiredByUser = studentRoomService.getRoomsHiredByUser(codeUser);
            return ApiResponseDto.createdWithState(getRoomsHiredByUser, "Get list room student hired success!", HttpStatus.OK);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/details/information")
    public ResponseEntity<?> getDetailsInformationStudentByUserId(){
        try {
            DetailInformationUserResponse response = ktxUserService.getDetailInformationUser();
            return ApiResponseDto.createdWithState(response,"Get detail information success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/details/hired-room")
    public ResponseEntity<?> getListStudentHiredRoom(@AuthenticationPrincipal OidcUser principal,@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class)
        }) StudentListRoomHiredRequest request){
        try {
            Page<ListHiredRoomStudentResponse> responses = studentRoomService.getListHiredRoomStudentResponse(principal,request);
            return ApiResponseDto.createdWithState(responses, "Get all room hired success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search-students")
    public ResponseEntity<?> searchInformationStudents(@RequestParam("code-student") String codeStudent){
        try {
            InformationStudentHiredResponse response = ktxUserService.searchInformationStudentByNumberStudent(codeStudent);
            return ApiResponseDto.createdWithState(response,"Search information student hired room success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/information")
    public ResponseEntity<?> getInformationAccount(){
        try {
            AuthenticationDto authenticationDto = authenticationService.getOAuthentication2ByUserName();
            return ApiResponseDto.createdWithState(authenticationService.convertToAuthenticationResponse(authenticationDto),
                    "Get information account success!", HttpStatus.OK);
        }catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/upload-students")
    public ResponseEntity<?> uploadAccountStudentsToSystem(@RequestParam("file")MultipartFile file){
        try {
            ktxUserService.uploadFileAccountStudent(file);
            return ApiResponseDto.createdWithMessage("Upload file student success!", HttpStatus.OK);
        }catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/find-all-students")
    public ResponseEntity<?> findAllStudents(@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class)
    }) FindAllStudentsRequest request){
        try {
            Page<FindAllStudentsResponse> responses = ktxUserService.findAllStudentRequest(request);
            return ApiResponseDto.createdWithState(responses, "Find all students success!", HttpStatus.OK);
        }catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/detail-student")
    public ResponseEntity<?> findAllStudents(@RequestParam("code_user") String codeUser){
        try {
            DetailInformationUserResponse response = ktxUserService.getDetailInformationUserByCodeUser(codeUser);
            return ApiResponseDto.createdWithState(response, "Get detail information student!", HttpStatus.OK);
        }catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> findAllStudents(@RequestBody UpdateProfileUserRequest request){
        try {
            ktxUserService.updateUserProfile(request);
            return ApiResponseDto.createdWithMessage("update profile user success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
