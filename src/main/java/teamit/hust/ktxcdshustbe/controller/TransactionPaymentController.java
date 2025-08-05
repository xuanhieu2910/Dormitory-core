package teamit.hust.ktxcdshustbe.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import teamit.hust.ktxcdshustbe.dto.ApiResponseDto;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.request.transactionPayment.CallBackPaymentRequest;
import teamit.hust.ktxcdshustbe.service.paymentService.PaymentService;

import java.util.Map;


@Tag(name = "Payment controller", description = "The Payment API. Contains operations like register,login, logout, refresh-token etc.")
@RestController
@RequestMapping("/api/v1/transaction-payment")
public class TransactionPaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping(value = "/callback", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> callbackPayment(@RequestParam Map<String,Object> request){
        try {
            paymentService.callBackPayment(request);
            return ApiResponseDto.createdWithMessage("Call back payment success!", HttpStatus.OK);
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (ValidParametersException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
