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
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.exception.IsBlankException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidateFiledException;
import teamit.hust.ktxcdshustbe.request.semester.CreateSemesterRequest;
import teamit.hust.ktxcdshustbe.request.semester.FindAllSemesterRequest;
import teamit.hust.ktxcdshustbe.request.semester.UpdateSemesterRequest;
import teamit.hust.ktxcdshustbe.response.semester.DetailSemesterResponse;
import teamit.hust.ktxcdshustbe.service.semester.SemesterService;

@Tag(name = "Semester controller", description = "The semester API. Contains operations for call data etc.")
@RestController
@RequestMapping("/api/v1/semester")
public class SemesterController {


    @Autowired
    SemesterService semesterService;


    @GetMapping("/find-all")
    public ResponseEntity<?> getListSemester(@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class)
        }) FindAllSemesterRequest request) {
        try {
            return ApiResponseDto.createdWithState(semesterService.findAllSemester(request), "Find all semester success", HttpStatus.OK);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/details")
    public ResponseEntity<?> getDetailSemester(@RequestParam("code-semester") String codeSemester) {
        try {
            DetailSemesterResponse semester = semesterService.findSemesterDetailByCode(codeSemester);
            return ApiResponseDto.createdWithState(semester, "Find details semester success!", HttpStatus.OK);
        }
        catch (IsBlankException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSemester(@RequestBody CreateSemesterRequest request) {
        try {
            semesterService.create(request);
            return ApiResponseDto.createdWithMessage("Create semester success", HttpStatus.OK);
        }
        catch (ExitsObjectException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        }
        catch (ValidateFiledException e){
            return  ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateSemester(@RequestBody UpdateSemesterRequest request) {
        try {
            semesterService.update(request);
            return ApiResponseDto.createdWithMessage("Update semester success", HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        }
        catch (ValidateFiledException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<?> deleteSemester(@RequestParam("codeSemester") String codeSemester) {
//        try {
//            semesterService.delete(codeSemester);
//            return ApiResponseDto.createdWithMessage("Delete semester success", HttpStatus.OK);
//        }
//        catch (NotFoundException e) {
//            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
//        }
//        catch (Exception e) {
//            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
