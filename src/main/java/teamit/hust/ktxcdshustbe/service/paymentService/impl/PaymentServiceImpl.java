package teamit.hust.ktxcdshustbe.service.paymentService.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import teamit.hust.ktxcdshustbe.entity.Orders;
import teamit.hust.ktxcdshustbe.entity.StudentRegisterRoom;
import teamit.hust.ktxcdshustbe.entity.TransactionPayment;
import teamit.hust.ktxcdshustbe.exception.ChecksumException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.request.transactionPayment.CallBackPaymentRequest;
import teamit.hust.ktxcdshustbe.service.orderItems.OrderItemsService;
import teamit.hust.ktxcdshustbe.service.orders.OrdersService;
import teamit.hust.ktxcdshustbe.service.paymentService.PaymentService;
import teamit.hust.ktxcdshustbe.service.studentRegisterRoom.StudentRegisterRoomService;
import teamit.hust.ktxcdshustbe.service.transactionPayment.TransactionPaymentService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.DateUtil;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.*;

@Log4j2
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Map<String, String> CANCEL_PAYMENT;
    private static final Map<String, String> PENDING_PAYMENT;
    private static final Map<String, String> SUCCESS_PAYMENT;
    private static final Map<String, String> FAILURE_PAYMENT;

    static {
        CANCEL_PAYMENT = new HashMap<>();
        CANCEL_PAYMENT.put("-61", "PAYMENT_EXPIRED");
        CANCEL_PAYMENT.put("-62", "PAYMENT_CANCELED");
        CANCEL_PAYMENT.put("102", "PENDING_FOR_OTP");
        CANCEL_PAYMENT.put("107", "PENDING_FOR_PAYMENT");
    }

    static {
        PENDING_PAYMENT = new HashMap<>();
        PENDING_PAYMENT.put("4", "PAYMENT_PROCESSOR_TIMEOUT");
        PENDING_PAYMENT.put("11", "PENDING_FOR_SETTLEMENT");
        PENDING_PAYMENT.put("13", "PARTIAL_APPROVAL");
        PENDING_PAYMENT.put("100", "PENDING_TRANSACTION");
    }

    static {
        SUCCESS_PAYMENT = new HashMap<>();
        SUCCESS_PAYMENT.put("200", "SUCCESS");
    }

    static {
        FAILURE_PAYMENT = new HashMap<>();
        FAILURE_PAYMENT.put("500", "INTERNAL_SERVER_ERROR");
        FAILURE_PAYMENT.put("502", "INTEGRATION_ERROR");
        FAILURE_PAYMENT.put("9", "PENDING_FOR_PURCHASE");
        FAILURE_PAYMENT.put("-1", "PROCESS_FAILURE");
        FAILURE_PAYMENT.put("-13", "LOCKED_CARD");
        FAILURE_PAYMENT.put("-25", "INVALID_CARD_INFO");
        FAILURE_PAYMENT.put("-6", "ISSUING_BANK_TIMEOUT");
        FAILURE_PAYMENT.put("-9", "ISSUING_BANK_ERROR");
        FAILURE_PAYMENT.put("-31", "LIMIT_VIOLATED");
        FAILURE_PAYMENT.put("-28", "UNREGISTERED_CARD");
        FAILURE_PAYMENT.put("-8", "BANK_ERROR");
        FAILURE_PAYMENT.put("-80", "INVALID_ACCOUNT_INFO");
        FAILURE_PAYMENT.put("-81", "INVALID_ACC_NAME");
        FAILURE_PAYMENT.put("-82", "ACCOUNT_NOT_ALLOWED_1");
        FAILURE_PAYMENT.put("-2", "UNIDENTIFIED_FAILURE");
        FAILURE_PAYMENT.put("-3", "PAYMENT_PROCESSOR_FAILURE");
        FAILURE_PAYMENT.put("-4", "PAYMENT_PROCESSOR_TIMEOUT");
        FAILURE_PAYMENT.put("-10", "REJECTED_BY_AVS");
        FAILURE_PAYMENT.put("-11", "CARD_EXPIRED");
        FAILURE_PAYMENT.put("-12", "CARD_REJECTED");
        FAILURE_PAYMENT.put("-14", "STOLEN_LOST_CARD");
        FAILURE_PAYMENT.put("-15", "INSUFFICIENT_FUNDS");
        FAILURE_PAYMENT.put("-5", "ISSUING_BANK_UNAVAILABLE");
        FAILURE_PAYMENT.put("-7", "AUTHENTICATION_FAILED");
        FAILURE_PAYMENT.put("-17", "INVALID_CARD");
        FAILURE_PAYMENT.put("-18", "INVALID_CARD_CVN");
        FAILURE_PAYMENT.put("-32", "CREDIT_LIMIT_EXCEEDED");
        FAILURE_PAYMENT.put("-39", "INVALID_EXP_DATE");
        FAILURE_PAYMENT.put("-40", "REJECTED_BY_PAYMENT_PROCESSOR");
        FAILURE_PAYMENT.put("-23", "ACCOUNT_NOT_ALLOWED");
        FAILURE_PAYMENT.put("-26", "INVALID_CARD_NAME");
        FAILURE_PAYMENT.put("-19", "INVALID_CARD_NO");
        FAILURE_PAYMENT.put("-20", "INVALID_CARD_TYPE");
        FAILURE_PAYMENT.put("-21", "INVALID_OTP");
        FAILURE_PAYMENT.put("-22", "OTP_TIMEOUT");
        FAILURE_PAYMENT.put("-24", "INVALID_ISSUE_DATE");
        FAILURE_PAYMENT.put("-33", "DEBIT_LIMIT_EXCEEDED");
        FAILURE_PAYMENT.put("-41", "FRAUD_DETECTED");
        FAILURE_PAYMENT.put("-107", "AUTHENTICATION_FAILED_2");
        FAILURE_PAYMENT.put("-43", "ORDER_MARKED_FOR_REVIEW");
        FAILURE_PAYMENT.put("-42", "ORDER_REJECTED");
        FAILURE_PAYMENT.put("-44", "TRANSACTION_REJECTED");
        FAILURE_PAYMENT.put("-84", "NOT_GET_STATUS_RISK");
        FAILURE_PAYMENT.put("-83", "DETECTED_INTERNAL_RISK");
        FAILURE_PAYMENT.put("-2014", "CBS14");
        FAILURE_PAYMENT.put("-2015", "CBS15");
        FAILURE_PAYMENT.put("-87", "FAILED_BIOMETRIC_AUTH ");
        FAILURE_PAYMENT.put("-88", "BIOMETRIC_UNCOMPLETED");
    }


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
    public void callBackPayment(Map<String,Object> request) throws JsonProcessingException {
        CallBackPaymentRequest callBackPaymentRequest = convertToCallBackPaymentRequest(request);
        verifyCallBackPayment(callBackPaymentRequest);
        log.info("[CALL BACK] : {} - {}", callBackPaymentRequest.toString(), DateUtil.formatToPattern(new Date(), DateUtil.DATE_FORMAT));
        TransactionPayment transactionPayment =
                transactionPaymentService.findByIdOrderAndStatusAndType(Integer.valueOf(callBackPaymentRequest.getOrder_id()), Constants.STATUS_INIT_TRANSACTION_PAYMENT,
                        Constants.TYPE_REQ_TRANSACTION_PAYMENT);
        verifyTransactionPaymentCallBack(transactionPayment, callBackPaymentRequest);
        Orders orders = ordersService.findOrdersByIdOrder(transactionPayment.getIdOrder());
        updateTransactionPaymentCallBack(transactionPayment, callBackPaymentRequest);
        updateOrdersCallBack(orders, callBackPaymentRequest);
        updateStudentRegisterRoomCallBack(orders, callBackPaymentRequest);
        createNewTransactionResponse(orders, transactionPayment, callBackPaymentRequest);
    }

    private CallBackPaymentRequest convertToCallBackPaymentRequest(Map<String, Object> request) {
        CallBackPaymentRequest callBackPaymentRequest = new CallBackPaymentRequest();
        callBackPaymentRequest.setPayment_method(request.containsKey("merchant_id") ? ValueUtil.getStringByObject(request.get("merchant_id")) : null);
        callBackPaymentRequest.setTerminal_id(request.containsKey("terminal_id") ? ValueUtil.getStringByObject(request.get("terminal_id")) : null);
        callBackPaymentRequest.setTxn_code(request.containsKey("txn_code") ? ValueUtil.getStringByObject(request.get("txn_code")) : null);
        callBackPaymentRequest.setTxn_amount(request.containsKey("txn_amount") ? ValueUtil.getStringByObject(request.get("txn_amount")) : null);
        callBackPaymentRequest.setTxn_fee(request.containsKey("txn_fee") ? ValueUtil.getStringByObject(request.get("txn_fee")) : null);
        callBackPaymentRequest.setTxn_currency(request.containsKey("txn_currency") ? ValueUtil.getStringByObject(request.get("txn_currency")) : null);
        callBackPaymentRequest.setOrder_id(request.containsKey("order_id") ? ValueUtil.getStringByObject(request.get("order_id")) : null);
        callBackPaymentRequest.setOrder_status(request.containsKey("order_status") ? ValueUtil.getStringByObject(request.get("order_status")) : null);
        callBackPaymentRequest.setPayment_method(request.containsKey("payment_method") ? ValueUtil.getStringByObject(request.get("payment_method")) : null);
        callBackPaymentRequest.setPayment_card_brand(request.containsKey("payment_card_brand") ? ValueUtil.getStringByObject(request.get("payment_card_brand")) : null);
        callBackPaymentRequest.setPayment_card_bin(request.containsKey("payment_card_bin") ? ValueUtil.getStringByObject(request.get("payment_card_bin")) : null);
        callBackPaymentRequest.setPayment_card_suffix(request.containsKey("payment_card_suffix") ? ValueUtil.getStringByObject(request.get("payment_card_suffix")) : null);
        callBackPaymentRequest.setToken_card_id(request.containsKey("token_card_id") ? ValueUtil.getStringByObject("token_card_id") : null);
        callBackPaymentRequest.setToken_card_brand(request.containsKey("token_card_brand") ? ValueUtil.getStringByObject("token_card_brand") : null);
        callBackPaymentRequest.setToken_card_bin(request.containsKey("token_card_bin") ? ValueUtil.getStringByObject("token_card_bin") : null);
        callBackPaymentRequest.setToken_card_suffix(request.containsKey("token_card_suffix") ? ValueUtil.getStringByObject("token_card_suffix") : null);
        callBackPaymentRequest.setToken_value(request.containsValue("token_value") ? ValueUtil.getStringByObject("token_value") : null);
        callBackPaymentRequest.setResult_code(request.containsKey("result_code") ? ValueUtil.getStringByObject("result_code") : null);
        callBackPaymentRequest.setResult_explicit_code(request.containsKey("result_explicit_code") ? ValueUtil.getStringByObject("result_explicit_code") : null);
        callBackPaymentRequest.setSignature(request.containsKey("signature") ? ValueUtil.getStringByObject("signature") : null);
        return callBackPaymentRequest;
    }

    private void updateStudentRegisterRoomCallBack(Orders orders, CallBackPaymentRequest request) {
        StudentRegisterRoom studentRegisterRoom = studentRegisterRoomService.getStudentRegisterRoomByIdOrder(orders.getIdOrder());
        if (StringUtils.isNotBlank(request.getResult_code()) && SUCCESS_PAYMENT.containsKey(request.getResult_code())) {
            studentRegisterRoom.setStatus(Constants.STATUS_SUCCESS_PAYMENT_STUDENT_ROOM_REGISTER);
        } else if (StringUtils.isNotBlank(request.getResult_code()) && CANCEL_PAYMENT.containsKey(request.getResult_code())) {
            studentRegisterRoom.setStatus(Constants.STATUS_CANCEL_PAYMENT_STUDENT_ROOM_REGISTER);
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
        if (StringUtils.isNotBlank(request.getResult_code()) && SUCCESS_PAYMENT.containsKey(request.getResult_code())) {
            transactionPayment.setStatus(Constants.STATUS_COMPLETE_TRANSACTION_PAYMENT);
        }else if (StringUtils.isNotBlank(request.getResult_code()) && CANCEL_PAYMENT.containsKey(request.getResult_code())){
            transactionPayment.setStatus(Constants.STATUS_COMPLETE_TRANSACTION_CANCEL);
        }else {
            transactionPayment.setStatus(Constants.STATUS_COMPLETE_TRANSACTION_FALSE);
        }
        transactionPayment.setTimeModified(new Date().getTime());
        transactionPaymentService.saveTransactionPayment(transactionPayment);
    }

    private Orders updateOrdersCallBack(Orders orders, CallBackPaymentRequest request) {
        if (StringUtils.isNotBlank(request.getResult_code()) && SUCCESS_PAYMENT.containsKey(request.getResult_code())) {
            orders.setStatusOrder(Constants.STATUS_ORDER_COMPLETE_PAYMENT);
        } else if (StringUtils.isNotBlank(request.getResult_code()) && CANCEL_PAYMENT.containsKey(request.getResult_code())) {
            orders.setStatusOrder(Constants.STATUS_ORDER_PAYMENT_FALSE);
        }else {
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
