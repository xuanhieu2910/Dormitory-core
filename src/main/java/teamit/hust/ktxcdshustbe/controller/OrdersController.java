package teamit.hust.ktxcdshustbe.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamit.hust.ktxcdshustbe.dto.ApiResponseDto;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.request.orders.ConfirmOrderRequest;
import teamit.hust.ktxcdshustbe.request.orders.CreateOrdersRequest;
import teamit.hust.ktxcdshustbe.service.orders.OrdersService;

@Log4j2
@Tag(name = "Orders Controller", description = "The orders APIs. Contains operations like find all, create, edit, delete etc.")
@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {

    @Autowired
    OrdersService ordersService;


    @PostMapping("/create")
    public ResponseEntity<?> createOrders(@RequestBody CreateOrdersRequest request){
        try {
            return ApiResponseDto.createdWithState(ordersService.createOrder(request),
                    "Create order success!",
                    HttpStatus.OK);
        } catch (NotFoundException e){
            e.printStackTrace();
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody ConfirmOrderRequest request){
        try {
            return ApiResponseDto.createdWithState(ordersService.confirmOrder(request),
                    "Confirm order success!", HttpStatus.OK);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getOrdersDetailBy(@RequestParam("code") String codeOrders){
        try {
            return ApiResponseDto.createdWithState(ordersService.findOrdersDetailByCodeOrders(codeOrders),
                    "Get detail order success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
