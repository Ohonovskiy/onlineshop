package ua.ohonovskiy.onlineshop.order.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.order.order.model.UserOrderInfo;
import ua.ohonovskiy.onlineshop.order.order.repository.UserOrderInfoRepository;

@Service
@RequiredArgsConstructor
public class UserOrderInfoService {
    private final UserOrderInfoRepository userOrderInfoRepository;

    public UserOrderInfo save(UserOrderInfo info) {
        return userOrderInfoRepository.save(info);
    }
}
