package teamit.hust.ktxcdshustbe.service.orders.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
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
import teamit.hust.ktxcdshustbe.dto.orderItems.DetailOrderItemsDto;
import teamit.hust.ktxcdshustbe.dto.orders.DetailOrderDto;
import teamit.hust.ktxcdshustbe.entity.*;
import teamit.hust.ktxcdshustbe.exception.*;
import teamit.hust.ktxcdshustbe.repository.orders.OrdersRepository;
import teamit.hust.ktxcdshustbe.request.orders.ConfirmOrderRequest;
import teamit.hust.ktxcdshustbe.request.orders.CreateOrdersRequest;
import teamit.hust.ktxcdshustbe.response.orderItems.DetailOrderItemsResponse;
import teamit.hust.ktxcdshustbe.response.orders.ConfirmOrdersResponse;
import teamit.hust.ktxcdshustbe.response.orders.DetailOrderResponse;
import teamit.hust.ktxcdshustbe.response.studentRegister.StudentRegisterRoomCurrentResponse;
import teamit.hust.ktxcdshustbe.response.studentRegister.StudentRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.service.orderItems.OrderItemsService;
import teamit.hust.ktxcdshustbe.service.orderSession.OrderSessionService;
import teamit.hust.ktxcdshustbe.service.orders.OrdersService;
import teamit.hust.ktxcdshustbe.service.room.RoomService;
import teamit.hust.ktxcdshustbe.service.studentRegisterRoom.StudentRegisterRoomService;
import teamit.hust.ktxcdshustbe.service.transactionPayment.TransactionPaymentService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.DateUtil;
import teamit.hust.ktxcdshustbe.utility.PropertiesUtil;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Log4j2
@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    OrdersRepository ordersRepository;
    @Autowired
    StudentRegisterRoomService studentRegisterRoomService;
    @Autowired
    OrderItemsService orderItemsService;
    @Autowired
    RoomService roomService;
    @Autowired
    TransactionPaymentService transactionPaymentService;
    @Autowired
    OrderSessionService orderSessionService;
    @Autowired
    RestTemplate restTemplate;



    @Transactional
    @Override
    public String createOrder(CreateOrdersRequest request) {
        verifyCreateOrder(request);
        StudentRegisterRoomResponse registerRoomCurrentResponse = studentRegisterRoomService.getRegisterRoomCurrent();
        if (!request.getCodeRoom().equals(registerRoomCurrentResponse.getCodeRoom())){
            throw new ValidParametersException();
        }
        Orders orders = storeOrder(initializeOrder(registerRoomCurrentResponse, request));
        List<OrderItems> orderItems = orderItemsService.saveListOrderItems(initializeOrderItems(orders, registerRoomCurrentResponse));
        TransactionPayment transactionPayment = transactionPaymentService.saveTransactionPayment(initializeTransactionPayment(orders));
        OrderSessions orderSessions = orderSessionService.saveOrderSessions(initializeOrderSession(orders));
        return orders.getCodeOrder();
    }

    @Override
    public Orders findOrdersByCodeOrder(String codeOrder) {
        Optional<Orders> orders = ordersRepository.findOrdersByCodeOrder(codeOrder);
        if (orders.isEmpty()){
            throw new NotFoundException();
        }
        return orders.get();
    }

    @Override
    public Orders findOrdersByIdOrder(Integer idOrder) {
        Optional<Orders> orders = ordersRepository.findOrdersByIdOrder(idOrder);
        if (orders.isEmpty()){
            throw new NotFoundException();
        }
        return orders.get();
    }

    @Override
    public Orders saveOrder(Orders orders) {
        return ordersRepository.save(orders);
    }


    @Transactional
    @Modifying
    @Override
    public ConfirmOrdersResponse confirmOrder(ConfirmOrderRequest request) throws JsonProcessingException {
        verifyConfirmOrder(request);
        log.info("[GET BILL] : {} - {}", request.toString(), DateUtil.formatToPattern(new Date(), DateUtil.DATE_FORMAT));
        Orders orders = findOrdersByCodeOrder(request.getCodeOrder());
        if (!orders.getStatusOrder().equals(Constants.STATUS_ORDER_INIT)) {
            throw new ValidParametersException();
        }
        List<OrderItems> orderItems = orderItemsService.findOrderItemsByIdOrder(orders.getIdOrder());
        Map<String, Object> dataBody = initializeDataBody(request.getTypePayment(), orders, orderItems);
        ConfirmOrdersResponse response = fetchToConfirmOrder(dataBody);
        String dataJsonFetch = new ObjectMapper().writeValueAsString(response);
        TransactionPayment transactionPayment =
                transactionPaymentService.saveTransactionPayment(initializeTransactionPaymentConfirmOrder(orders, dataBody, dataJsonFetch));
        updateOrdersToConfirmOrders(orders,transactionPayment, dataJsonFetch);
        updateStudentRegisterRoom(orders);
        return response;
    }

    @Override
    public DetailOrderResponse findOrdersDetailByCodeOrders(String codeOrders) {
        Optional<DetailOrderDto> detailOrderDto = ordersRepository.findOrdersDetailByCodeOrders(codeOrders);
        if (detailOrderDto.isEmpty()){
            throw new NotFoundException();
        }
        return convertToFindOrdersDetailByCodeOrders(detailOrderDto.get());
    }

    private DetailOrderResponse convertToFindOrdersDetailByCodeOrders(DetailOrderDto detailOrderDto) {
        DetailOrderResponse response = new DetailOrderResponse();
        response.setTitleOrder(detailOrderDto.getTitleOrder());
        response.setCodeOrder(detailOrderDto.getCodeOrder());
        response.setStatus(detailOrderDto.getStatus());
        response.setTotalMoney(detailOrderDto.getTotalMoney());
        response.setTimeCreated(detailOrderDto.getTimeCreated());
        response.setTimeModified(detailOrderDto.getTimeModified());
        List<DetailOrderItemsResponse> items = new ArrayList<>();
        for (DetailOrderItemsDto detailOrderItemsDto : detailOrderDto.getItems()){
            DetailOrderItemsResponse item = new DetailOrderItemsResponse();
            item.setCodeOrderItem(detailOrderItemsDto.getCodeOrderItem());
            item.setCodeRoom(detailOrderItemsDto.getCodeRoom());
            item.setTitleRoom(detailOrderItemsDto.getTitleRoom());
            item.setQuantity(detailOrderItemsDto.getQuantity());
            item.setTotalMoney(detailOrderDto.getTotalMoney());
            items.add(item);
        }
        response.setItems(items);
        return response;
    }

    private void updateStudentRegisterRoom(Orders orders) {
        StudentRegisterRoomResponse registerRoomCurrentResponse = studentRegisterRoomService.getRegisterRoomCurrent();
        StudentRegisterRoom studentRegisterRoom = studentRegisterRoomService.findStudentRegisterRoomById(registerRoomCurrentResponse.getIdStudentRegisterRoom());
        studentRegisterRoom.setIdOrder(orders.getIdOrder());
        studentRegisterRoom.setStatus(Constants.STATUS_STUDENT_REGISTER_ROOM_CONFIRM_ORDER);
        studentRegisterRoom.setTimeModified(new Date().getTime());
        KtxUser ktxUser = (KtxUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        studentRegisterRoom.setIdUserModified(ktxUser.getIdKtxUser());
        studentRegisterRoomService.saveStudentRoomRegisterRoom(studentRegisterRoom);
    }

    private TransactionPayment initializeTransactionPaymentConfirmOrder(Orders orders, Map<String, Object> dataBody,
                                                                        String dataJsonFetch) {
        Long timeCurrent = new Date().getTime();
        TransactionPayment transactionPayment = new TransactionPayment();
        transactionPayment.setIdOrder(orders.getIdOrder());
        transactionPayment.setCodeTransactionPayment(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()));
        transactionPayment.setStatus(Constants.STATUS_INIT_TRANSACTION_PAYMENT);
        transactionPayment.setType(Constants.TYPE_REQ_TRANSACTION_PAYMENT);
        transactionPayment.setTimeCreated(timeCurrent);
        transactionPayment.setTimeModified(timeCurrent);
        transactionPayment.setJsonData(dataJsonFetch);
        transactionPayment.setCheckSum(String.valueOf(dataBody.get("signature")));
        transactionPayment.setReturnUrl(String.valueOf(dataBody.get("return_url")));
        transactionPayment.setCancelUrl(String.valueOf(dataBody.get("cancel_url")));
        MethodPaymentToGetBillDto methodPaymentToGetBillDto = (MethodPaymentToGetBillDto) dataBody.get("method");
        transactionPayment.setMethodPayment(methodPaymentToGetBillDto.getValue());
        transactionPayment.setMerchantId(String.valueOf(dataBody.get("merchant_id")));
        transactionPayment.setTerminalId(String.valueOf(dataBody.get("terminal_id")));
        return transactionPayment;
    }

    private void updateOrdersToConfirmOrders(Orders orders, TransactionPayment transactionPayment, String dataJsonFetch) {
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orders.setStatusOrder(Constants.STATUS_ORDER_CONFIRM_PAYMENT);
        orders.setTimeModified(new Date().getTime());
        orders.setIdUserModified(ktxUser.getIdKtxUser());
        orders.setValue(dataJsonFetch);
        orders.setIdTransactionPayment(transactionPayment.getIdTransactionPayment());
        saveOrder(orders);
    }

    private ConfirmOrdersResponse fetchToConfirmOrder(Map<String, Object> payload) {
        try {
            ResponseEntity<Object> responseEntity = restTemplate.exchange(
                    "https://portal-v2-staging.foxpay.vn/payment/card/initiate/",
                    HttpMethod.POST,
                    setEntityFetchToConfirmPayment(payload),
                    Object.class
            );
            return convertToConfirmPayment(responseEntity);
        } catch (HttpClientErrorException | JsonProcessingException e){
            e.printStackTrace();
            throw new ValidateFiledException();
        }
    }

    private ConfirmOrdersResponse convertToConfirmPayment(ResponseEntity<Object> responseEntity) {
        LinkedHashMap<Object, Object> responses = (LinkedHashMap<Object, Object>) responseEntity.getBody();
        ConfirmOrdersResponse response = new ConfirmOrdersResponse();
        if (ObjectUtils.isEmpty(responses.get("session_id"))){
            throw new ValidParametersException();
        }
        response.setSessionId(String.valueOf(responses.get("session_id")));
        response.setPaymentUrl(String.valueOf(responses.get("payment_url")));
        response.setCollectionUrl(String.valueOf(responses.get("collection_url")));
        response.setExpTime(String.valueOf(responses.get("exp_time")));
        return response;
    }

    private HttpEntity<?> setEntityFetchToConfirmPayment(Map<String, Object> payload) throws JsonProcessingException {
        MultiValueMap<String, String> requestHeader = new LinkedMultiValueMap<>();
        requestHeader.add("Content-Type", "application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        String dataSend = objectMapper.writeValueAsString(payload);
        log.info("[GET BILL] : Header - {} ;Payload - {}; Date-time - {} ",
                objectMapper.writeValueAsString(requestHeader),
                dataSend ,DateUtil.formatToPattern(new Date(),
                        DateUtil.DATE_FORMAT));
        return new HttpEntity<>(dataSend, requestHeader);
    }

    private void verifyConfirmOrder(ConfirmOrderRequest request) throws IsBlankException, IsNullException {
        if (ObjectUtils.isEmpty(request.getCodeOrder())){
            throw new IsBlankException();
        }
        if (StringUtils.isBlank(request.getTypePayment())
                || (!request.getTypePayment().equals(Constants.TYPE_PAYMENT_VIET_QR)
                && !request.getTypePayment().equals(Constants.TYPE_PAYMENT_INTERNATIONAL)
                && !request.getTypePayment().equals(Constants.TYPE_PAYMENT_DOMESTIC))){
            throw new ValidParametersException();
        }
    }


    private OrderSessions initializeOrderSession(Orders orders) {
        Long timeCurrent = new Date().getTime();
        OrderSessions orderSessions = new OrderSessions();
        orderSessions.setIdOrder(orders.getIdOrder());
        orderSessions.setTimeCreated(timeCurrent);
        orderSessions.setTimeModified(timeCurrent);
        orderSessions.setExpiresAt(timeCurrent + Long.parseLong(PropertiesUtil.getProperty("time-expires-at-order")));
        orderSessions.setRetry(Constants.ORDER_SESSION_RETRY);
        orderSessions.setStatus(Constants.ORDER_SESSION_STATUS_ACTIVE);
        return orderSessions;
    }

    private TransactionPayment initializeTransactionPayment(Orders orders) {
        Long timeCurrent = new Date().getTime();
        TransactionPayment transactionPayment = new TransactionPayment();
        transactionPayment.setIdOrder(orders.getIdOrder());
        transactionPayment.setCodeTransactionPayment(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()));
        transactionPayment.setStatus(Constants.STATUS_INIT_TRANSACTION_PAYMENT);
        transactionPayment.setType(Constants.TYPE_REQ_TRANSACTION_PAYMENT);
        transactionPayment.setTimeCreated(timeCurrent);
        transactionPayment.setTimeModified(timeCurrent);
        return transactionPayment;
    }

    private List<OrderItems> initializeOrderItems(Orders orders, StudentRegisterRoomResponse registerRoomCurrentResponse) {
        Room room = roomService.findRoomByCodeRoom(registerRoomCurrentResponse.getCodeRoom()).get();
        Long timeCurrent = new Date().getTime();
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OrderItems orderItems = new OrderItems();
        orderItems.setCodeOrderItem(UUID.nameUUIDFromBytes(registerRoomCurrentResponse.getCodeRoom().getBytes()).toString());
        orderItems.setIdOrder(orders.getIdOrder());
        orderItems.setIdObject(room.getIdRoom());
        orderItems.setIdUser(ktxUser.getIdKtxUser());
        orderItems.setQuantity(Constants.QUANTITY_DEFAULT_ORDER_ITEM);
        orderItems.setTotalMoney(room.getPrice());
        orderItems.setTimeCreated(timeCurrent);
        orderItems.setTimeModified(timeCurrent);
        orderItems.setIdUserCreated(ktxUser.getIdKtxUser());
        orderItems.setIdUserModified(ktxUser.getIdKtxUser());
        orderItems.setNotes(null);
        orderItems.setStatus(Constants.STATUS_ORDER_ITEM_ACTIVE);
        orderItems.setTypeOrder(Constants.TYPE_ORDER_HIRED_ROOM);
        List<OrderItems> orderItemsList = new ArrayList<>();
        orderItemsList.add(orderItems);
        return orderItemsList;
    }

    private Orders storeOrder(Orders orders) {
        return ordersRepository.save(orders);
    }

    private void verifyCreateOrder(CreateOrdersRequest request) {
        if (StringUtils.isBlank(request.getTitleOrder()) || StringUtils.isBlank(request.getCodeRoom())
            || StringUtils.isBlank(request.getMethodPayment())){
            throw new ValidParametersException();
        }
        if (!request.getMethodPayment().equals(Constants.TYPE_PAYMENT_VIET_QR)
            && !request.getMethodPayment().equals(Constants.TYPE_PAYMENT_INTERNATIONAL)
            && !request.getMethodPayment().equals(Constants.TYPE_PAYMENT_DOMESTIC)){
            throw new ValidParametersException();
        }

    }

    private Orders initializeOrder(StudentRegisterRoomResponse registerRoomCurrentResponse,
                                   CreateOrdersRequest request) {
        Long timeCurrent = new Date().getTime();
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Orders orders = new Orders();
        orders.setTitleOrder(request.getTitleOrder());
        orders.setCodeOrder(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()));
        orders.setIdUser(ktxUser.getIdKtxUser());
        orders.setStatusOrder(Constants.STATUS_ORDER_INIT);
        orders.setTimeCreated(timeCurrent);
        orders.setTimeModified(timeCurrent);
        orders.setIdUserCreated(ktxUser.getIdKtxUser());
        orders.setIdUserModified(ktxUser.getIdKtxUser());
        orders.setTotalMoney(registerRoomCurrentResponse.getPrice());
        orders.setTypeOrder(Constants.TYPE_ORDER_HIRED_ROOM);
        return orders;
    }

    private Map<String, Object> initializeDataBody(String typePayment, Orders orders, List<OrderItems> orderItems) {
        Map<String, Object> dataBody = new HashMap<>();
        CustomerToGetBillDto customerToGetBillDto = initializeCustomerToGetBill();
        DeviceToGetBillDto deviceToGetBillDto = initializeDeviceToGetBill();
        MethodPaymentToGetBillDto methodPaymentToGetBillDto = initializeMethodToGetBill(typePayment);
        OrderToGetBillDto orderToGetBillDto =  initializeOrderToGetBill(orders, orderItems);
        dataBody.put("merchant_id", PropertiesUtil.getProperty("pft.pay-gate.merchant-id"));
        dataBody.put("terminal_id", PropertiesUtil.getProperty("pft.pay-gate.terminal-id"));
        dataBody.put("method", methodPaymentToGetBillDto);
        dataBody.put("return_token", "true");
        dataBody.put("device", deviceToGetBillDto);
        dataBody.put("customer", customerToGetBillDto);
        dataBody.put("order", orderToGetBillDto);
        dataBody.put("return_url", PropertiesUtil.getProperty("pft.pay-gate.return-url"));
        dataBody.put("cancel_url", PropertiesUtil.getProperty("pft.pay-gate.cancel-url"));
        dataBody.put("signature", initializeSignatureToGetBillQR(orderToGetBillDto,
                customerToGetBillDto,
                deviceToGetBillDto,
                methodPaymentToGetBillDto));
        return dataBody;
    }

    private CustomerToGetBillDto initializeCustomerToGetBill() {
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomerToGetBillDto customerToGetBillDto = new CustomerToGetBillDto();
        customerToGetBillDto.setFirst_name(ktxUser.getCodeUser());
        customerToGetBillDto.setLast_name(ktxUser.getCodeUser());
        customerToGetBillDto.setPhone_number("1234567899");
        customerToGetBillDto.setEmail(ktxUser.getUsername());
        return customerToGetBillDto;
    }

    private DeviceToGetBillDto initializeDeviceToGetBill() {
        return new DeviceToGetBillDto();
    }

    private MethodPaymentToGetBillDto initializeMethodToGetBill(String typePayment) {
        MethodPaymentToGetBillDto methodPaymentToGetBillDto = new MethodPaymentToGetBillDto();
        methodPaymentToGetBillDto.setValue(typePayment);
        methodPaymentToGetBillDto.setOption(0);
        return methodPaymentToGetBillDto;
    }

    private OrderToGetBillDto initializeOrderToGetBill(Orders orders, List<OrderItems> orderItems){
        OrderToGetBillDto orderToGetBillDto = new OrderToGetBillDto();
        orderToGetBillDto.setId(String.valueOf(orders.getIdOrder()) + new Date().getTime());
        orderToGetBillDto.setAmount(orders.getTotalMoney());
        orderToGetBillDto.setCurrency("VND");
        orderToGetBillDto.setDescription(orders.getCodeOrder());
        List<OrderItemsToGetBillDto> orderItemsToGetBillDtos = new ArrayList<>();
        for (OrderItems orderItem : orderItems){
            OrderItemsToGetBillDto toGetBillDto = new OrderItemsToGetBillDto();
            toGetBillDto.setId(String.valueOf(orderItem.getIdOrderItem()));
            toGetBillDto.setName(orderItem.getCodeOrderItem());
            toGetBillDto.setDescription(orderItem.getCodeOrderItem());
            toGetBillDto.setUnit_price(orderItem.getTotalMoney());
            toGetBillDto.setQuantity(String.valueOf(orderItem.getQuantity()));
            orderItemsToGetBillDtos.add(toGetBillDto);
        }
        orderToGetBillDto.setItems(orderItemsToGetBillDtos);
        return orderToGetBillDto;
    }

    private String initializeSignatureToGetBillQR(OrderToGetBillDto orderToGetBillDto,
                                                  CustomerToGetBillDto customerToGetBillDto,
                                                  DeviceToGetBillDto deviceToGetBillDto,
                                                  MethodPaymentToGetBillDto methodPaymentToGetBillDto) {
        String merchantId = PropertiesUtil.getProperty("pft.pay-gate.merchant-id");
        String  terminalId = PropertiesUtil.getProperty("pft.pay-gate.terminal-id");
        String methodValue = methodPaymentToGetBillDto.getValue();
        Integer methodOption = methodPaymentToGetBillDto.getOption();
        String orderId = orderToGetBillDto.getId();
        String orderAmount = orderToGetBillDto.getAmount();
        String orderCurrency = orderToGetBillDto.getCurrency();
        String description = orderToGetBillDto.getDescription();
        String cardNumber = "";
        String holderName = "";
        String cardMonth = "";
        String cardYear = "";
        String cardCvv = "";
        String returnToken = "true";
        String tokenValue = "";
        String tokenTransactionType = "";
        String customerFirstName = customerToGetBillDto.getFirst_name();
        String customerLastName = customerToGetBillDto.getLast_name();
        String customerEmail = customerToGetBillDto.getEmail();
        String customerPhoneNumber = customerToGetBillDto.getPhone_number();
        String deviceOs = deviceToGetBillDto.getOs();
        String deviceApp = deviceToGetBillDto.getApp();
        String deviceId = deviceToGetBillDto.getId();
        String returnUrl = PropertiesUtil.getProperty("pft.pay-gate.return-url");
        String cancelUrl = PropertiesUtil.getProperty("pft.pay-gate.cancel-url");
        String secretKey = PropertiesUtil.getProperty("pft.pay-gate.secret-key");
        String originalString = merchantId + terminalId + methodValue + methodOption +
                orderId + orderAmount + orderCurrency + description + cardNumber +
                holderName + cardMonth + cardYear + cardCvv +
                returnToken + tokenValue + tokenTransactionType +
                customerFirstName + customerLastName + customerEmail +
                customerPhoneNumber + deviceOs +
                deviceApp + deviceId + returnUrl + cancelUrl + secretKey;
        return Hashing.sha256()
                .hashString(originalString, StandardCharsets.UTF_8)
                .toString();
    }

}
