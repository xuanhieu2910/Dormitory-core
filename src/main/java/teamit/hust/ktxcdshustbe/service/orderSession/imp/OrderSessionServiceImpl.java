package teamit.hust.ktxcdshustbe.service.orderSession.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.entity.OrderSessions;
import teamit.hust.ktxcdshustbe.repository.orderSession.OrderSessionRepository;
import teamit.hust.ktxcdshustbe.service.orderSession.OrderSessionService;

@Service
public class OrderSessionServiceImpl implements OrderSessionService {

    @Autowired
    OrderSessionRepository orderSessionRepository;


    @Override
    public OrderSessions saveOrderSessions(OrderSessions orderSessions) {
        return orderSessionRepository.save(orderSessions);
    }
}
