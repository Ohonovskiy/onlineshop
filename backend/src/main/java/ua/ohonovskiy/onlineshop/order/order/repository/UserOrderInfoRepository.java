package ua.ohonovskiy.onlineshop.order.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ohonovskiy.onlineshop.order.order.model.UserOrderInfo;

@Repository
public interface UserOrderInfoRepository extends JpaRepository<UserOrderInfo, Long> {
}
