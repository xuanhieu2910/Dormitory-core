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
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.CreateBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.FindAllBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.FindAllDepartmentInBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.UpdateBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.service.batchesRegistration.BatchesRegistrationService;

@Tag(name = "Batches Registration API", description = "The Batches Registration API. Contains operations like CRUD room.")
@RestController
@RequestMapping("/api/v1/batches-registration")
public class BatchesRegistrationController {

    @Autowired
    BatchesRegistrationService batchesRegistrationService;

    @GetMapping("/find-all")
    public ResponseEntity<?> findAllBatchesRegistration(@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class)
    }) FindAllBatchesRegistrationRequest request) {
        try {
            return ApiResponseDto.createdWithState(batchesRegistrationService.findAll(request),
                    "Find all batches registration", HttpStatus.OK);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/detail")
    public ResponseEntity<?> getDetailBatchesRegistration(@RequestParam("code") String codeBatchesRegistration){
        try {
            return ApiResponseDto.createdWithState(batchesRegistrationService.getDetailBatchesRegistration(codeBatchesRegistration),
                    "Find detail batches registration", HttpStatus.OK);
        } catch (ValidParametersException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBatchesRegistration(@RequestBody CreateBatchesRegistrationRequest request){
        try {
            batchesRegistrationService.createBatchesRegistration(request);
            return ApiResponseDto.createdWithMessage("Create batches registration success!", HttpStatus.OK);
        } catch (ValidParametersException e){
            e.printStackTrace();
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            e.printStackTrace();
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ExitsObjectException e){
            e.printStackTrace();
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateBatchesRegistration(@RequestBody UpdateBatchesRegistrationRequest request){
        try {
            batchesRegistrationService.updateBatchesRegistration(request);
            return ApiResponseDto.createdWithMessage("Update batches registration success!", HttpStatus.OK);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ExitsObjectException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/find-all-department")
    public ResponseEntity<?> findAllDepartmentBatchesRegistration(@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class)
    }) FindAllDepartmentInBatchesRegistrationRequest request){
        try {
            return ApiResponseDto.createdWithState(batchesRegistrationService.findAllDepartmentBatchesRegistration(request),
                    "Find all department batches registration!", HttpStatus.OK);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
