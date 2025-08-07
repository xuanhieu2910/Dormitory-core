package teamit.hust.ktxcdshustbe.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamit.hust.ktxcdshustbe.dto.ApiResponseDto;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.request.userInstance.FindAllUserInstanceRequest;
import teamit.hust.ktxcdshustbe.request.userInstance.UserDetailsInstanceRequest;
import teamit.hust.ktxcdshustbe.request.userInstance.UserInstanceRequest;
import teamit.hust.ktxcdshustbe.service.userInstance.KtxUserInstanceService;

import java.util.List;

@Log4j2
@Tag(name = "Student instance Controller", description = "Student instance APIs." +
        " Contains operations like find all, create, edit, delete etc.")
@RestController
@RequestMapping("/api/v1/student-instance")
public class UserInstanceController {
    @Autowired
    KtxUserInstanceService ktxUserInstanceService;

    @GetMapping("/find-all")
    public ResponseEntity<?> findAllAdmissionProgramStudentInstance(@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class)
    }) FindAllUserInstanceRequest request){
        try {
            return ApiResponseDto.createdWithState(ktxUserInstanceService.findAllUserInstance(request),
                    "Find all student instance success!", HttpStatus.OK);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateTrainingProgramInstance(
            @RequestBody List<UserInstanceRequest> requests){
        try {
            ktxUserInstanceService.UpdateUserInstance(requests);
            return ApiResponseDto.createdWithMessage("Update student instance value success!",
                    HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteTrainingProgramInstance(@RequestBody List<UserInstanceRequest> requests){
        try {
            ktxUserInstanceService.deleteUserInstance(requests);
            return ApiResponseDto.createdWithMessage("Delete student instance success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-student")
    public ResponseEntity<?> createStudentRegisterOfficial(@RequestBody List<UserDetailsInstanceRequest>  requests){
        try {
            ktxUserInstanceService.createUserInstance(requests);
            return ApiResponseDto.createdWithMessage("Create student official success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
