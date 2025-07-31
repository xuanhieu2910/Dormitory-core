package teamit.hust.ktxcdshustbe.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamit.hust.ktxcdshustbe.dto.ApiResponseDto;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.request.batchesRegistrationRoom.FindAllBatchesRegistrationRoomRequest;
import teamit.hust.ktxcdshustbe.service.batchesRegistrationRoom.BatchesRegistrationRoomService;

@Tag(name = "Batches Registration Room API", description = "The Batches Registration Room API. Contains operations like CRUD room.")
@RestController
@RequestMapping("/api/v1/batches-registration-room")
public class BatchesRegistrationRoomController {

    @Autowired
    BatchesRegistrationRoomService batchesRegistrationRoomService;


    @GetMapping("/find-all")
    public ResponseEntity<?> findAllDepartmentBatchesRegistration(@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class)
    }) FindAllBatchesRegistrationRoomRequest request){
        try {
            return ApiResponseDto.createdWithState(batchesRegistrationRoomService.findAllBatchesRegistrationRoom(request),
                    "Find all room batches registration!", HttpStatus.OK);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
