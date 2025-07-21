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
import teamit.hust.ktxcdshustbe.request.department.CreateDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.department.EditDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.department.FindAllDepartmentRequest;
import teamit.hust.ktxcdshustbe.service.department.DepartmentService;

@Tag(name = "Department API", description = "The department API. Contains operations like CRUD department.")
@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {


    @Autowired
    DepartmentService departmentService;

    @GetMapping("/find-all")
    public ResponseEntity<?> getAllDepartment(@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class)
    }) FindAllDepartmentRequest request){
        try {
            return ApiResponseDto.createdWithState(departmentService.findAllDepartment(request),
                    "Find all departments success!", HttpStatus.OK);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/statistic")
    public ResponseEntity<?> getDetailsDepartmentByCode(@RequestParam("code") String codeDepartment){
        try {
            return ApiResponseDto.createdWithState(departmentService.findDepartmentStatisticDetailByCode(codeDepartment),
                    "Find detail department success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/change-active")
    public ResponseEntity<?> changeActiveDepartmentByCode(@RequestParam("code") String codeDepartment){
        try {
            departmentService.changeActiveDepartmentByCodeDepartment(codeDepartment);
            return ApiResponseDto.createdWithMessage("Change active department success!", HttpStatus.OK);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/update")
    public ResponseEntity<?> editDepartment(@RequestBody EditDepartmentRequest request){
        try {
            departmentService.editDepartment(request);
            return ApiResponseDto.createdWithMessage("Edit department success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ExitsObjectException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDepartment(@RequestBody CreateDepartmentRequest request){
        try {
            departmentService.createDepartment(request);
            return ApiResponseDto.createdWithMessage("Create department success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ExitsObjectException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping
    public ResponseEntity<?> createDepartment(@RequestParam("code-department") String codeDepartment){
        try {
            departmentService.deleteDepartmentByCodeDepartment(codeDepartment);
            return ApiResponseDto.createdWithMessage("delete department success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ExitsObjectException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        }  catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/details")
    public ResponseEntity<?> findDetailsAdmissionProgram(@RequestParam("code-department")String codeDepartment){
        try {
            return ApiResponseDto.createdWithState(departmentService.findDetailsDepartmentByCodeDepartment(codeDepartment),
                    "Find details department success!", HttpStatus.OK);
        }catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
