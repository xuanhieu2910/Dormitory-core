package teamit.hust.ktxcdshustbe.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import teamit.hust.ktxcdshustbe.config.WebSecurityConfig;
import teamit.hust.ktxcdshustbe.dto.ApiResponseDto;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.service.paymentService.PaymentService;

import java.util.Map;


@Tag(name = "Payment controller", description = "The Payment API. Contains operations like register,login, logout, refresh-token etc.")
@RestController
@RequestMapping("/api/v1/transaction-payment")
public class TransactionPaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping(value = "/callback", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView callbackPayment(@RequestParam Map<String,Object> request){
        try {
            paymentService.callBackPayment(request);
            return new RedirectView(WebSecurityConfig.DOMAIN_FE + "/create-registration/payment-endpoint");
        } catch (Exception e){
            return new RedirectView(WebSecurityConfig.DOMAIN_FE + "/create-registration/payment-endpoint");
        }
    }

}
