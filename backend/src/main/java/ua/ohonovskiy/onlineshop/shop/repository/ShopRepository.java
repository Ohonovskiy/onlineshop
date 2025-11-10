package ua.ohonovskiy.onlineshop.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ohonovskiy.onlineshop.shop.model.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
}
