package teamit.hust.ktxcdshustbe.service.paymentService.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import teamit.hust.ktxcdshustbe.dto.bankingService.*;
import teamit.hust.ktxcdshustbe.entity.*;
import teamit.hust.ktxcdshustbe.exception.*;
import teamit.hust.ktxcdshustbe.request.transactionPayment.CallBackPaymentRequest;
import teamit.hust.ktxcdshustbe.response.payment.PaymentGetBillQrResponse;
import teamit.hust.ktxcdshustbe.response.studentRegister.StudentRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.service.orderItems.OrderItemsService;
import teamit.hust.ktxcdshustbe.service.orders.OrdersService;
import teamit.hust.ktxcdshustbe.service.paymentService.PaymentService;
import teamit.hust.ktxcdshustbe.service.studentRegisterRoom.StudentRegisterRoomService;
import teamit.hust.ktxcdshustbe.service.transactionPayment.TransactionPaymentService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.DateUtil;
import teamit.hust.ktxcdshustbe.utility.PropertiesUtil;

import java.nio.charset.StandardCharsets;

import java.util.*;

@Log4j2
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    OrdersService ordersService;
    @Autowired
    OrderItemsService orderItemsService;
    @Autowired
    TransactionPaymentService transactionPaymentService;
    @Autowired
    StudentRegisterRoomService studentRegisterRoomService;


    @Transactional
    @Override
    public void callBackPayment(CallBackPaymentRequest request) throws JsonProcessingException {
        verifyCallBackPayment(request);
        log.info("[CALL BACK] : {} - {}", request.toString(), DateUtil.formatToPattern(new Date(), DateUtil.DATE_FORMAT));
        TransactionPayment transactionPayment =
                transactionPaymentService.findByIdOrderAndStatusAndType(Integer.valueOf(request.getOrder_id()), Constants.STATUS_INIT_TRANSACTION_PAYMENT,
                        Constants.TYPE_REQ_TRANSACTION_PAYMENT);
        verifyTransactionPaymentCallBack(transactionPayment, request);
        Orders orders = ordersService.findOrdersByIdOrder(transactionPayment.getIdOrder());
        updateTransactionPaymentCallBack(transactionPayment, request);
        updateOrdersCallBack(orders, request);
        updateStudentRegisterRoomCallBack(orders, request);
        createNewTransactionResponse(orders, transactionPayment, request);
    }

    private void updateStudentRegisterRoomCallBack(Orders orders, CallBackPaymentRequest request) {
        StudentRegisterRoom studentRegisterRoom = studentRegisterRoomService.getStudentRegisterRoomByIdOrder(orders.getIdOrder());
        if (request.getResult_code().equals(Constants.CALL_BACK_SUCCESS)) {
            studentRegisterRoom.setStatus(Constants.STATUS_SUCCESS_PAYMENT_STUDENT_ROOM_REGISTER);
        } else {
            studentRegisterRoom.setStatus(Constants.STATUS_FALSE_PAYMENT_STUDENT_ROOM_REGISTER);
        }
        studentRegisterRoom.setTimeModified(new Date().getTime());
        studentRegisterRoomService.saveStudentRoomRegisterRoom(studentRegisterRoom);
    }

    private void createNewTransactionResponse(Orders orders,
                                              TransactionPayment transactionPayment,
                                              CallBackPaymentRequest request)
            throws JsonProcessingException {
        Long timeCurrent = new Date().getTime();
        TransactionPayment transactionPaymentResponse = new TransactionPayment();
        transactionPaymentResponse.setIdOrder(orders.getIdOrder());
        transactionPaymentResponse.setCodeTransactionPayment(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()));
        transactionPaymentResponse.setStatus(transactionPayment.getStatus());
        transactionPaymentResponse.setType(Constants.TYPE_RES_TRANSACTION_PAYMENT);
        transactionPaymentResponse.setTimeCreated(timeCurrent);
        transactionPaymentResponse.setTimeModified(timeCurrent);
        transactionPaymentResponse.setJsonData(new ObjectMapper().writeValueAsString(request));
        transactionPaymentResponse.setCheckSum(transactionPayment.getCheckSum());
        transactionPaymentResponse.setReturnUrl(transactionPayment.getReturnUrl());
        transactionPaymentResponse.setCancelUrl(transactionPayment.getCancelUrl());
        transactionPaymentResponse.setMethodPayment(transactionPayment.getMethodPayment());
        transactionPaymentResponse.setMerchantId(transactionPayment.getMerchantId());
        transactionPaymentResponse.setTerminalId(transactionPayment.getTerminalId());
        transactionPaymentService.saveTransactionPayment(transactionPaymentResponse);
    }

    private void updateTransactionPaymentCallBack(TransactionPayment transactionPayment, CallBackPaymentRequest request) {
        if (request.getResult_code().equals(Constants.CALL_BACK_SUCCESS)) {
            transactionPayment.setStatus(Constants.STATUS_COMPLETE_TRANSACTION_PAYMENT);
        } else {
            transactionPayment.setStatus(Constants.STATUS_COMPLETE_TRANSACTION_FALSE);
        }
        transactionPayment.setTimeModified(new Date().getTime());
        transactionPaymentService.saveTransactionPayment(transactionPayment);
    }

    private Orders updateOrdersCallBack(Orders orders, CallBackPaymentRequest request) {
        if (request.getResult_code().equals(Constants.CALL_BACK_SUCCESS)) {
            orders.setStatusOrder(Constants.STATUS_ORDER_COMPLETE_PAYMENT);
        } else {
            orders.setStatusOrder(Constants.STATUS_ORDER_PAYMENT_FALSE);
        }
        orders.setTimeModified(new Date().getTime());
        return ordersService.saveOrder(orders);
    }

    private void verifyTransactionPaymentCallBack(TransactionPayment transactionPayment, CallBackPaymentRequest request) {
        if (!transactionPayment.getCheckSum().equals(request.getSignature())){
            throw new ChecksumException();
        }
        if (!transactionPayment.getMerchantId().equals(request.getMerchant_id())
                || !transactionPayment.getTerminalId().equals(request.getTerminal_id())){
            throw new ValidParametersException();
        }
    }

    private void verifyCallBackPayment(CallBackPaymentRequest request) {
        if (StringUtils.isBlank(request.getMerchant_id()) || StringUtils.isBlank(request.getTerminal_id())
        || StringUtils.isBlank(request.getOrder_id()) || StringUtils.isBlank(request.getOrder_status())
        || StringUtils.isBlank(request.getResult_code()) || StringUtils.isBlank(request.getResult_explicit_code())
        || StringUtils.isBlank(request.getSignature())){
            throw new ValidParametersException();
        }
    }

}
