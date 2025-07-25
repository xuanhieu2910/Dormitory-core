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
import teamit.hust.ktxcdshustbe.request.priorityGroup.CreatePriorityGroupRequest;
import teamit.hust.ktxcdshustbe.request.priorityGroup.FindAllPriorityGroupRequest;
import teamit.hust.ktxcdshustbe.request.priorityGroup.UpdatePriorityGroupRequest;
import teamit.hust.ktxcdshustbe.response.priorityGroup.PriorityGroupDetailResponse;
import teamit.hust.ktxcdshustbe.service.priorityGroup.PriorityGroupService;

@Tag(name = "Priority group controller", description = "abc")
@RestController
@RequestMapping("/api/v1/priority-group")
public class PriorityGroupController {


    @Autowired
    private PriorityGroupService priorityGroupService;

    @GetMapping("/find-all")
    public ResponseEntity<?> findAllPriorityGroup(@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class),
    })
                                                  FindAllPriorityGroupRequest request){
        try {
            return ApiResponseDto.createdWithState(priorityGroupService.findAllPriorityGroup(request), "Find all priority group success", HttpStatus.OK);
        }
        catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/details")
    public ResponseEntity<?> getPriorityGroupDetail(@RequestParam("priorityGroupCode") String priorityGroupCode){
        try {
            PriorityGroupDetailResponse priorityGroupDetailResponse = priorityGroupService.getPriorityGroupDetail(priorityGroupCode);
            return ApiResponseDto.createdWithState(priorityGroupDetailResponse, "Get priority group detail success", HttpStatus.OK);
        }
        catch(IsBlankException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPriorityGroup(@RequestBody CreatePriorityGroupRequest request) {
        try {
            priorityGroupService.create(request);
            return ApiResponseDto.createdWithMessage("Create priority group success", HttpStatus.OK);
        }
        catch (IsBlankException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        }
        catch (ExitsObjectException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePriorityGroup(@RequestBody UpdatePriorityGroupRequest request) {
        try{
            priorityGroupService.update(request);
            return ApiResponseDto.createdWithMessage("Update priority group success", HttpStatus.OK);
        }
        catch(IsBlankException e) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        }
        catch(NotFoundException e ) {
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<?> deletePriorityGroup(@RequestParam("priorityGroupCode") String priorityGroupCode){
//        try {
//            priorityGroupService.delete(priorityGroupCode);
//            return ApiResponseDto.createdWithMessage("Delete priority group success", HttpStatus.OK);
//        }
//        catch (NotFoundException e) {
//            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
//        }
//        catch (Exception e) {
//            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
