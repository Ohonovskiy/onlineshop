package ua.ohonovskiy.onlineshop.order.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ohonovskiy.onlineshop.order.order.model.Order;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(User user);

    List<Order> findAllByShop_Owner(User shopOwner);

    Optional<Order> findByIdAndShop_Owner(Long id, User shopOwner);

    Optional<Order> findByIdAndUser(Long id, User user);

    Optional<Order> findByIdAndShop(Long id, Shop shop);
}
