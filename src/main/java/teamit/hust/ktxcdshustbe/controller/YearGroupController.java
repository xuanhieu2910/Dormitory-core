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
import teamit.hust.ktxcdshustbe.request.yearGroup.CreateYearGroupRequest;
import teamit.hust.ktxcdshustbe.request.yearGroup.FindAllYearGroupsRequest;
import teamit.hust.ktxcdshustbe.request.yearGroup.UpdateYearGroupRequest;
import teamit.hust.ktxcdshustbe.response.yearGroup.FindAllYearGroupsResponse;
import teamit.hust.ktxcdshustbe.response.yearGroup.YearGroupDetailResponse;
import teamit.hust.ktxcdshustbe.service.yearGroup.YearGroupService;

@Tag(name = "Year Group API", description = "The Year Group API. Contains CRUD operations for year groups.")
@RestController
@RequestMapping("/api/v1/year-group")
public class YearGroupController {

    @Autowired
    private YearGroupService yearGroupService;

    @GetMapping("/find-all")
    public ResponseEntity<?> findAllYearGroup(
            @And({
                    @Spec(path = "page", params = "page", spec = Like.class),
                    @Spec(path = "size", params = "size", spec = Like.class),
                    @Spec(path = "keyword", params = "keyword", spec = Like.class)
            }) FindAllYearGroupsRequest request) {
        try {
            Page<FindAllYearGroupsResponse> yearGroupPage = yearGroupService.findAllYearGroup(request);
            return ApiResponseDto.createdWithState(yearGroupPage, "Find all year groups success!", HttpStatus.OK);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/details")
    public ResponseEntity<?> getYearGroupDetails(@RequestParam("code") String code) {
        try {
            YearGroupDetailResponse response = yearGroupService.findYearGroupDetailsByCode(code);
            return ApiResponseDto.createdWithState(response, "Find year group detail success!", HttpStatus.OK);
        } catch (NotFoundException e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ValidParametersException e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createYearGroup(@RequestBody CreateYearGroupRequest request) {
        try {
            yearGroupService.createYearGroup(request);
            return ApiResponseDto.createdWithMessage("Create new year group success!", HttpStatus.CREATED);
        } catch (ValidParametersException | ExitsObjectException e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateYearGroup(@RequestBody UpdateYearGroupRequest request) {
        try {
            yearGroupService.updateYearGroup(request);
            return ApiResponseDto.createdWithMessage("Update year group success!", HttpStatus.OK);
        } catch (NotFoundException e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ValidParametersException | ExitsObjectException e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteYearGroup(@RequestParam("code") String code) {
        try {
            yearGroupService.deleteYearGroup(code);
            return ApiResponseDto.createdWithMessage("Delete year group success!", HttpStatus.OK);
        } catch (NotFoundException e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ValidParametersException e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage("An error occurred while deleting the year group: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
