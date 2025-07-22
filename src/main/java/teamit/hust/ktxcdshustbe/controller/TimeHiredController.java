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
import teamit.hust.ktxcdshustbe.request.timeHired.CreateTimeHiredRequest;
import teamit.hust.ktxcdshustbe.request.timeHired.FindAllTimeHiredRequest;
import teamit.hust.ktxcdshustbe.request.timeHired.UpdateTimeHiredRequest;
import teamit.hust.ktxcdshustbe.service.timeHired.TimeHiredService;

@Tag(name = "Time Hired API", description = "The Time Hired API. Contains operations like CRUD department.")
@RestController
@RequestMapping("/api/v1/time-hired")
public class TimeHiredController {
    @Autowired
    TimeHiredService timeHiredService;
    @GetMapping("/find-all")
    public ResponseEntity<?> findAllTimeHired(@And({
            @Spec(path = "page", params = "page", spec = Like.class),
            @Spec(path = "size", params = "size", spec = Like.class),
            @Spec(path = "keyword", params = "keyword", spec = Like.class)
    }) FindAllTimeHiredRequest request){
        try {
            return ApiResponseDto.createdWithState(timeHiredService.findAllTimeHired(request),
                    "Find all time hired success!", HttpStatus.OK);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTimeHired(@RequestBody UpdateTimeHiredRequest request){
        try {
            timeHiredService.updateTimeHired(request);
            return ApiResponseDto.createdWithMessage("Edit time hired success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTimeHired(@RequestBody CreateTimeHiredRequest request){
        try {
            timeHiredService.createTimeHired(request);
            return ApiResponseDto.createdWithMessage("Create time hired success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteTimeHiredByCode(@RequestParam("code-time-hired")String codeTimeHired){
        try {
            timeHiredService.deleteTimeHiredByCode(codeTimeHired);
            return ApiResponseDto.createdWithMessage("delete time hired  success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/details")
    public ResponseEntity<?> findDetailsTimeHiredByCode(@RequestParam("code-time-hired")String codeTimeHired){
        try {
            return ApiResponseDto.createdWithState(timeHiredService.findDetailsTimeHiredByCode(codeTimeHired),
                    "Find details time hired success!", HttpStatus.OK);
        }catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
